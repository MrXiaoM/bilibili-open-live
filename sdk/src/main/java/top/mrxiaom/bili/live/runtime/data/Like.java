package top.mrxiaom.bili.live.runtime.data;

import com.google.gson.annotations.SerializedName;

/**
 * 点赞数据
 *
 * @see <a href="https://open-live.bilibili.com/document/f9ce25be-312e-1f4a-85fd-fef21f1637f8">文档</a>
 */
public class Like {
    /**
     * 用户昵称
     */
    @SerializedName("uname")
    public String userName;

    /**
     * 用户UID (即将弃用)
     */
    @Deprecated
    @SerializedName("uid")
    public long uid = -1;

    /**
     * 用户唯一标识
     */
    @SerializedName("open_id")
    public String openId;

    /**
     * 用户头像
     */
    @SerializedName("uface")
    public String userFace;

    /**
     * 时间秒级时间戳
     */
    @SerializedName("timestamp")
    public long timestamp;

    /**
     * 发生的直播间
     */
    @SerializedName("room_id")
    public long roomId;

    /**
     * 点赞文案( “xxx点赞了”)
     */
    @SerializedName("like_text")
    public String likeText;

    /**
     * 对单个用户最近2秒的点赞次数聚合
     */
    @SerializedName("like_count")
    public long likeCount;

    /**
     * 佩戴的粉丝勋章佩戴状态
     */
    @SerializedName("fans_medal_wearing_status")
    public boolean fansMedalWearingStatus;

    /**
     * 粉丝勋章名
     */
    @SerializedName("fans_medal_name")
    public String fansMedalName;

    /**
     * 粉丝勋章等级
     */
    @SerializedName("fans_medal_level")
    public long fansMedalLevel;
}
