package top.mrxiaom.bili.live.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import top.mrxiaom.bili.live.runtime.data.*;

import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BLiveClient {
    Timer timer = null;
    protected long heartbeatTime = 20 * 1000;
    protected String token;

    public abstract void onOpen();

    public abstract void onReceivedDanmaku(Dm data);

    public abstract void onReceivedGift(SendGift data);

    public abstract void onReceivedGuardBuy(Guard data);

    public abstract void onReceivedSuperChat(SuperChat data);

    public abstract void onReceivedSuperChatDel(SuperChatDel data);
    
    public abstract void onReceivedLike(Like data);

    public abstract void onReceivedRawNotice(String raw, JsonObject json);

    public abstract void onPopularityUpdate(int popularity);

    public abstract void connect();

    public abstract void disconnect();

    public abstract void close();

    public abstract void send(byte[] packet);

    public abstract void send(Packet packet);

    public void onOpenConnection() {
        send(Packet.authority(token));

        if (timer != null) timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                send(Packet.heartbeat());
            }
        }, 0, heartbeatTime);
    }

    protected void processPacket(Packet packet) {
        var header = packet.header;
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
            case AuthorityResponse -> onOpen();
            case HeartBeatResponse -> {
                // reverse byte[]
                for (int i = 0, j = packet.packetBody.length - 1; i < j; i++, j--) {
                    byte tmp = packet.packetBody[i];
                    packet.packetBody[i] = packet.packetBody[j];
                    packet.packetBody[j] = tmp;
                }
                var popularity = bytesToInt(packet.packetBody, 0);
                onPopularityUpdate(popularity);
            }
            case ServerNotify -> processNotice(new String(packet.packetBody, StandardCharsets.UTF_8));

            // HeartBeat packet request, only send by client
            // This operation key only used for sending authority packet by client
            // case HeartBeat, Authority, default -> {}
        }
    }

    protected void processNotice(String rawMessage) {
        var json = JsonParser.parseString(rawMessage).getAsJsonObject();
        onReceivedRawNotice(rawMessage, json);
        var data = json.get("data");
        if (data == null)
            return;
        try {
            switch (json.get("cmd").getAsString()) {
                case "LIVE_OPEN_PLATFORM_DM" -> {
                    var dm = BApi.gson.fromJson(data, Dm.class);
                    onReceivedDanmaku(dm);
                }
                case "LIVE_OPEN_PLATFORM_SUPER_CHAT" -> {
                    var superChat = BApi.gson.fromJson(data, SuperChat.class);
                    onReceivedSuperChat(superChat);
                }
                case "LIVE_OPEN_PLATFORM_SUPER_CHAT_DEL" -> {
                    var superChatDel = BApi.gson.fromJson(data, SuperChatDel.class);
                    onReceivedSuperChatDel(superChatDel);
                }
                case "LIVE_OPEN_PLATFORM_SEND_GIFT" -> {
                    var gift = BApi.gson.fromJson(data, SendGift.class);
                    onReceivedGift(gift);
                }
                case "LIVE_OPEN_PLATFORM_GUARD" -> {
                    var guard = BApi.gson.fromJson(data, Guard.class);
                    onReceivedGuardBuy(guard);
                }
                case "LIVE_OPEN_PLATFORM_LIKE" -> {
                    var like = BApi.gson.fromJson(data, Like.class);
                    onReceivedLike(like);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int bytesToInt(byte[] bytes, int offset) {
        return (bytes[offset + 3] & 0xFF) << 24
                | (bytes[offset + 2] & 0xFF) << 16
                | (bytes[offset + 1] & 0xFF) << 8
                | (bytes[offset] & 0xFF);
    }
}