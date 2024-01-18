package top.mrxiaom.bili.live.runtime.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 互动玩法心跳
 *
 * @see <a href="https://open-live.bilibili.com/document/657d8e34-f926-a133-16c0-300c1afc6e6b">文档</a>
 */
public class GameIds {
    /**
     * 游戏场次
     */
    @SerializedName("game_ids")
    public List<String> gameIds;
}
