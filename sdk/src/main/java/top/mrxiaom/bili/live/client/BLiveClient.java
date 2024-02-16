package top.mrxiaom.bili.live.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import top.mrxiaom.bili.live.client.data.EmptyInfo;
import top.mrxiaom.bili.live.runtime.data.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public abstract class BLiveClient {
    protected final Logger logger;
    protected Timer projectTimer = null;
    protected Timer timer = null;
    /**
     * 长连心跳间隔时间
     */
    protected long heartbeatTimeWs = 30 * 1000L;
    /**
     * 项目心跳间隔时间
     */
    protected long heartbeatTime = 20 * 1000L;
    protected String token;
    protected String gameId;
    public BLiveClient(Logger logger, String gameId) {
        this.logger = logger;
        this.gameId = gameId;
    }

    public abstract boolean getStatus();

    public abstract void onOpen();

    public abstract void onReceivedDanmaku(Dm data);

    public abstract void onReceivedGift(SendGift data);

    public abstract void onReceivedGuardBuy(Guard data);

    public abstract void onReceivedSuperChat(SuperChat data);

    public abstract void onReceivedSuperChatDel(SuperChatDel data);
    
    public abstract void onReceivedLike(Like data);

    public abstract void onReceivedRawNotice(String raw, JsonObject json);

    public abstract void onPopularityUpdate(int popularity);

    public void connect() {
        if (projectTimer != null) projectTimer.cancel();
        projectTimer = new Timer();
        // 项目心跳
        projectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendProjectHeartbeat();
            }
        }, 0, heartbeatTime);
    }

    public void disconnect() {
        if (projectTimer != null) {
            projectTimer.cancel();
            projectTimer = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public abstract void close();

    public abstract void send(byte[] packet);

    public abstract void send(Packet packet);

    public void onOpenConnection() {
        send(Packet.authority(token));

        if (timer != null) timer.cancel();
        timer = new Timer();
        // 长连心跳
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getStatus()) {
                    send(Packet.heartbeat());
                    logger.info("长连心跳成功");
                }
            }
        }, 0, heartbeatTimeWs);
    }

    /**
     * 发送项目心跳。
     * 注意事项：项目心跳**必须**在长连连接之前发送，否则长连连接后过期。
     */
    protected void sendProjectHeartbeat() {
        EmptyInfo result = BApiClient.heartBeatInteractivePlay(gameId);
        if (result.code != 0) {
            logger.warning("项目心跳失败, code=" + result.code + ", message=" + result.message);
        } else {
            logger.info("项目心跳成功");
        }
    }

    protected void processPacket(Packet packet) {
        PacketHeader header = packet.header;
        switch (header.protocolVersion) {
            case UnCompressed:
            case HeartBeat:
                break;
            case Zlib:
                //no Zlib compress in OpenBLive wss
                //await foreach (var packet1 in ZlibDeCompressAsync(packet.PacketBody))
                //ProcessPacketAsync(packet1);
                return;
            case Brotli:
                //no Brotli compress in OpenBLive wss
                //await foreach (var packet1 in BrotliDecompressAsync(packet.PacketBody))
                //ProcessPacketAsync(packet1);
                return;
            default:
                throw new UnsupportedOperationException(
                        "New bilibili danmaku protocol appears, please contact the author if you see this Exception.");
        }

        switch (header.operation) {
            case AuthorityResponse: {
                onOpen();
                break;
            }
            case HeartBeatResponse: {
                // reverse byte[]
                for (int i = 0, j = packet.packetBody.length - 1; i < j; i++, j--) {
                    byte tmp = packet.packetBody[i];
                    packet.packetBody[i] = packet.packetBody[j];
                    packet.packetBody[j] = tmp;
                }
                int popularity = bytesToInt(packet.packetBody, 0);
                onPopularityUpdate(popularity);
                break;
            }
            case ServerNotify: {
                processNotice(new String(packet.packetBody, StandardCharsets.UTF_8));
                break;
            }

            // HeartBeat packet request, only send by client
            // This operation key only used for sending authority packet by client
            // case HeartBeat, Authority, default -> {}
        }
    }

    protected void processNotice(String rawMessage) {
        JsonObject json = JsonParser.parseString(rawMessage).getAsJsonObject();
        onReceivedRawNotice(rawMessage, json);
        JsonElement data = json.get("data");
        if (data == null)
            return;
        try {
            switch (json.get("cmd").getAsString()) {
                case "LIVE_OPEN_PLATFORM_DM": {
                    Dm dm = BApi.gson.fromJson(data, Dm.class);
                    onReceivedDanmaku(dm);
                    break;
                }
                case "LIVE_OPEN_PLATFORM_SUPER_CHAT": {
                    SuperChat superChat = BApi.gson.fromJson(data, SuperChat.class);
                    onReceivedSuperChat(superChat);
                    break;
                }
                case "LIVE_OPEN_PLATFORM_SUPER_CHAT_DEL": {
                    SuperChatDel superChatDel = BApi.gson.fromJson(data, SuperChatDel.class);
                    onReceivedSuperChatDel(superChatDel);
                    break;
                }
                case "LIVE_OPEN_PLATFORM_SEND_GIFT": {
                    SendGift gift = BApi.gson.fromJson(data, SendGift.class);
                    onReceivedGift(gift);
                    break;
                }
                case "LIVE_OPEN_PLATFORM_GUARD": {
                    Guard guard = BApi.gson.fromJson(data, Guard.class);
                    onReceivedGuardBuy(guard);
                    break;
                }
                case "LIVE_OPEN_PLATFORM_LIKE": {
                    Like like = BApi.gson.fromJson(data, Like.class);
                    onReceivedLike(like);
                    break;
                }
            }
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    protected void printStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            t.printStackTrace(pw);
        }
        logger.warning(sw.toString());
    }

    public static int bytesToInt(byte[] bytes, int offset) {
        return (bytes[offset + 3] & 0xFF) << 24
                | (bytes[offset + 2] & 0xFF) << 16
                | (bytes[offset + 1] & 0xFF) << 8
                | (bytes[offset] & 0xFF);
    }
}