package top.mrxiaom.bili.bukkit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import top.mrxiaom.bili.bukkit.events.*;
import top.mrxiaom.bili.live.client.BApiClient;
import top.mrxiaom.bili.live.client.WebSocketBLiveClient;
import top.mrxiaom.bili.live.client.data.AppStartInfo;
import top.mrxiaom.bili.live.runtime.data.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import static top.mrxiaom.bili.bukkit.utils.Util.stackTrace;

public class CraftBLiveClient extends WebSocketBLiveClient {
    BukkitMain plugin;
    String appId;
    AppStartInfo startInfo;
    int popularity = 0;
    int online = 0;

    public CraftBLiveClient(BukkitMain plugin, String appId, AppStartInfo startInfo) {
        super(plugin.getLogger(), startInfo.data.gameInfo.gameId, startInfo.data.websocketInfo.wssLink, startInfo.data.websocketInfo.authBody);
        this.plugin = plugin;
        this.startInfo = startInfo;
        this.appId = appId;
    }

    public int getPopularity() {
        return popularity;
    }

    public int getOnline() {
        return online;
    }

    public AppStartInfo getStartInfo() {
        return startInfo;
    }

    @Override
    public void close() {
        super.close();
        BApiClient.endInteractivePlay(appId, startInfo.data.gameInfo.gameId);
    }

    @Override
    public void onOpenConnection() {
        logger.info("长连接建立成功");
        super.onOpenConnection();
    }

    @Override
    protected void processPacket(Packet packet) {
        Bukkit.getScheduler().runTask(plugin, () -> super.processPacket(packet));
    }

    @Override
    public void onReceivedDanmaku(Dm data) {
        if (plugin.debug) {
            logger.info("收到弹幕 " + data.userName + "(" + data.openId + "): " + data.msg);
        }
        Bukkit.getPluginManager().callEvent(new LiveDanmakuEvent(data));
    }

    @Override
    public void onReceivedGift(SendGift data) {
        if (plugin.debug) {
            logger.info("收到礼物 " + data.userName + "(" + data.openId + "): " + data.giftName + "(" + data.giftId + ") * " + data.giftNum);
        }
        Bukkit.getPluginManager().callEvent(new LiveGiftEvent(data));
    }

    @Override
    public void onReceivedGuardBuy(Guard data) {
        if (plugin.debug) {
            logger.info("收到大航海 " + data.userInfo.userName + "(" + data.userInfo.openId + "): " + data.guardUnit + " (" + data.guardLevel + "级) " + data.guardNum);
        }
        Bukkit.getPluginManager().callEvent(new LiveGuardBuyEvent(data));
    }

    @Override
    public void onReceivedSuperChat(SuperChat data) {
        if (plugin.debug) {
            logger.info("收到SC " + data.userName + "(" + data.openId + "): ￥" + data.rmb + " " + data.message);
        }
        Bukkit.getPluginManager().callEvent(new LiveSuperChatEvent(data));
    }

    @Override
    public void onReceivedSuperChatDel(SuperChatDel data) {
        Bukkit.getPluginManager().callEvent(new LiveSuperChatDelEvent(data));
    }

    @Override
    public void onReceivedLike(Like data) {
        if (plugin.debug) {
            logger.info("收到 " + data.userName + "(" + data.openId + ") 的 " + data.likeCount + " 个点赞");
        }
        Bukkit.getPluginManager().callEvent(new LiveLikeEvent(data));
    }

    @Override
    public void onPopularityUpdate(int data) {
        this.popularity = data;
        JsonObject roomData = getRoomDataById(String.valueOf(startInfo.data.anchorInfo.roomId));
        if (roomData != null) {
            online = roomData.get("online").getAsInt();
            if (plugin.debug) {
                plugin.getLogger().info("热度更新: " + popularity + ", 在线人数更新: " + online);
            }
        }
        Bukkit.getPluginManager().callEvent(new LivePopularityUpdateEvent(popularity, online));
    }

    @Override
    public void onReceivedRawNotice(String raw, JsonObject json) {
        super.onReceivedRawNotice(raw, json);
    }

    public JsonObject getRoomDataById(String roomId) {
        try {
            String url = "https://api.live.bilibili.com/xlive/web-room/v1/index/getRoomBaseInfo?room_ids=" + roomId + "&req_biz=video";
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            String str = readString(conn);
            JsonObject json = new JsonParser().parse(str).getAsJsonObject();
            JsonObject data = json.get("data").getAsJsonObject();
            JsonObject byRoomIds = data.get("by_room_ids").getAsJsonObject();
            return byRoomIds.get(roomId).getAsJsonObject();
        } catch (Throwable t) {
            stackTrace(plugin.getLogger(), t);
            return null;
        }
    }

    private static String readString(URLConnection conn) throws IOException {
        try (InputStream is = conn.getInputStream()) {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                for (int length; (length = is.read(buffer)) != -1;) {
                    os.write(buffer, 0, length);
                }
                return new String(os.toByteArray(), StandardCharsets.UTF_8);
            }
        }
    }

    public static CraftBLiveClient connect(BukkitMain plugin, String code, String appId) {
        AppStartInfo startInfo = BApiClient.startInteractivePlay(code, appId);
        if (startInfo.code != 0) {
            throw new IllegalStateException(startInfo.message);
        }
        CraftBLiveClient client = new CraftBLiveClient(plugin, appId, startInfo);
        client.connect();
        return client;
    }
}
