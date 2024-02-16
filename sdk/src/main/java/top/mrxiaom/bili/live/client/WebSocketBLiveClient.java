package top.mrxiaom.bili.live.client;

import com.google.gson.JsonObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;
import top.mrxiaom.bili.live.runtime.data.*;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class WebSocketBLiveClient extends BLiveClient {
    private static final Map<Integer, String> codes = new HashMap<Integer, String>() {{
        put(1000, "NORMAL");
        put(1001, "GOING_AWAY");
        put(1002, "PROTOCOL_ERROR");
        put(1003, "REFUSE");
        put(1005, "NOCODE");
        put(1006, "ABNORMAL_CLOSE");
        put(1007, "NO_UTF8");
        put(1008, "POLICY_VALIDATION");
        put(1009, "TOOBIG");
        put(1010, "EXTENSION");
        put(1011, "UNEXPECTED_CONDITION");
        put(1012, "SERVICE_RESTART");
        put(1013, "TRY_AGAIN_LATER");
        put(1014, "BAD_GATEWAY");
        put(1015, "TLS_ERROR");
        put(-1, "NEVER_CONNECTED");
        put(-2, "BUGGYCLOSE");
        put(-3, "FLASHPOLICY");
    }};
    List<String> wssLink;
    WebSocketClient webSocketClient;

    public WebSocketBLiveClient(Logger logger, String gameId, List<String> wssLink, String token) {
        super(logger, gameId);
        this.wssLink = wssLink;
        this.token = token;
    }

    @Override
    public void connect() {
        String url = wssLink.get(0);
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalStateException("wsslink is invalid");
        }

        disconnect(); // 关闭连接会关掉项目心跳的定时器，super.connect(); 必须在 disconnect(); 后面
        super.connect();

        URI uri = null;
        try {
            uri = new URI(url);
        } catch (Throwable t) {
            printStackTrace(t);
        }
        if (uri == null) throw new IllegalStateException("Can't parse URI " + url);
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake ignored) {
                WebSocketBLiveClient.this.onOpenConnection();
            }

            @Override
            public void onMessage(ByteBuffer buffer) {
                processPacket(new Packet(buffer.array()));
            }

            public void onMessage(String message) {
                logger.info(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if (code == CloseFrame.NORMAL) {
                    logger.info("WS CLOSED" + (remote ? " (remote)" : "") + " " + reason);
                } else {
                    logger.warning("WS ERROR: " + (remote ? "(remote) " : "") + codes.getOrDefault(code, "UNKNOWN("+code+")") + " " + reason);
                }
            }

            @Override
            public void onError(Exception ex) {
                printStackTrace(ex);
            }
        };
        webSocketClient.connect();
    }

    @Override
    public boolean getStatus() {
        return webSocketClient != null && webSocketClient.isOpen();
    }

    @Override
    public void disconnect() {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
        super.disconnect();
    }

    @Override
    public void close() {
        disconnect();
        System.gc();
    }

    @Override
    public void send(byte[] packet) {
        if (webSocketClient != null) {
            webSocketClient.send(packet);
        }
    }

    @Override
    public void send(Packet packet) {
        send(packet.toBytes());
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onReceivedDanmaku(Dm data) {

    }

    @Override
    public void onReceivedGift(SendGift data) {

    }

    @Override
    public void onReceivedGuardBuy(Guard data) {

    }

    @Override
    public void onReceivedSuperChat(SuperChat data) {

    }

    @Override
    public void onReceivedSuperChatDel(SuperChatDel data) {

    }

    @Override
    public void onReceivedLike(Like data) {

    }

    @Override
    public void onReceivedRawNotice(String raw, JsonObject json) {

    }

    @Override
    public void onPopularityUpdate(int popularity) {

    }

}
