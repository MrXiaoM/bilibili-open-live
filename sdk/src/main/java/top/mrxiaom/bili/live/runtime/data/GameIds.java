package top.mrxiaom.bili.live.runtime.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 互动玩法心跳
 *
 * @see <a href="https://open-live.bilibili.com/doc/2/1/3">文档</a>
 */
public class GameIds {
    /**
     * 游戏场次
     */
    @SerializedName("game_ids")
    public List<String> gameIds;
}
