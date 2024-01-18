package top.mrxiaom.bili.live.runtime.data;

import com.google.gson.annotations.SerializedName;

/**
 * 弹幕数据
 *
 * @see <a href="https://open-live.bilibili.com/document/f9ce25be-312e-1f4a-85fd-fef21f1637f8">文档</a>
 */
public class Dm {
    /**
     * 用户UID
     */
    @SerializedName("uid")
    public long uid;

    /**
     * 用户昵称
     */
    @SerializedName("uname")
    public String userName;

    /**
     * 用户头像
     */
    @SerializedName("uface")
    public String userFace;

    /**
     * 弹幕发送时间秒级时间戳
     */
    @SerializedName("timestamp")
    public long timestamp;


    /**
     * 弹幕内容
     */
    @SerializedName("msg")
    public String msg;

    /**
     * 粉丝勋章等级
     */
    @SerializedName("fans_medal_level")
    public long fansMedalLevel;

    /**
     * 粉丝勋章名
     */
    @SerializedName("fans_medal_name")
    public String fansMedalName;

    /**
     * 佩戴的粉丝勋章佩戴状态
     */
    @SerializedName("fans_medal_wearing_status")
    public boolean fansMedalWearingStatus;

    /**
     * 大航海等级
     */
    @SerializedName("guard_level")
    public long guardLevel;

    /**
     * 弹幕接收的直播间
     */
    @SerializedName("room_id")
    public long roomId;
}
