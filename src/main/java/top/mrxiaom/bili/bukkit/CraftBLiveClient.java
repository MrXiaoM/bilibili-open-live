package top.mrxiaom.bili.bukkit;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import top.mrxiaom.bili.bukkit.events.*;
import top.mrxiaom.bili.live.client.BApiClient;
import top.mrxiaom.bili.live.client.WebSocketBLiveClient;
import top.mrxiaom.bili.live.runtime.data.*;
import top.mrxiaom.bili.live.client.data.AppStartInfo;

public class CraftBLiveClient extends WebSocketBLiveClient {
    BukkitMain plugin;
    String appId;
    AppStartInfo startInfo;
    int popularity = 0;
    public CraftBLiveClient(BukkitMain plugin, String appId, AppStartInfo startInfo) {
        super(plugin.getLogger(), startInfo.data.websocketInfo.wssLink, startInfo.data.websocketInfo.authBody);
        this.plugin = plugin;
        this.startInfo = startInfo;
        this.appId = appId;
    }

    public int getPopularity() {
        return popularity;
    }

    @Override
    public void disconnect() {
        BApiClient.endInteractivePlay(appId, startInfo.data.gameInfo.gameId);
        super.disconnect();
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
            logger.info("收到弹幕 " + data.userName + "(" + data.uid + "): " + data.msg);
        }
        Bukkit.getPluginManager().callEvent(new LiveDanmakuEvent(data));
    }

    @Override
    public void onReceivedGift(SendGift data) {
        if (plugin.debug) {
            logger.info("收到礼物 " + data.userName + "(" + data.uid + "): " + data.giftName + "(" + data.giftId + ") * " + data.giftNum);
        }
        Bukkit.getPluginManager().callEvent(new LiveGiftEvent(data));
    }

    @Override
    public void onReceivedGuardBuy(Guard data) {
        if (plugin.debug) {
            logger.info("收到大航海 " + data.userInfo.userName + "(" + data.userInfo.uid + "): " + data.guardUnit + " (" + data.guardLevel + "级) " + data.guardNum);
        }
        Bukkit.getPluginManager().callEvent(new LiveGuardBuyEvent(data));
    }

    @Override
    public void onReceivedSuperChat(SuperChat data) {
        if (plugin.debug) {
            logger.info("收到SC " + data.userName + "(" + data.uid + "): ￥" + data.rmb + " " + data.message);
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
            logger.info("收到 " + data.userName + "(" + data.uid + ") 的 " + data.likeCount + " 个点赞");
        }
        Bukkit.getPluginManager().callEvent(new LiveLikeEvent(data));
    }

    @Override
    public void onPopularityUpdate(int popularity) {
        Bukkit.getPluginManager().callEvent(new LivePopularityUpdateEvent(this.popularity = popularity));
    }

    @Override
    public void onReceivedRawNotice(String raw, JsonObject json) {
        super.onReceivedRawNotice(raw, json);
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
