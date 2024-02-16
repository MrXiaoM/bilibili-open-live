package top.mrxiaom.bili.live.runtime.data;

import com.google.gson.annotations.SerializedName;

/**
 * 付费留言数据
 *
 * @see <a href="https://open-live.bilibili.com/document/f9ce25be-312e-1f4a-85fd-fef21f1637f8">文档</a>
 */
public class SuperChat {
    /**
     * 直播间ID
     */
    @SerializedName("room_id")
    public long roomId;

    /**
     * 购买用户UID (即将弃用)
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
     * 购买的用户昵称
     */
    @SerializedName("uname")
    public String userName;

    /**
     * 购买用户头像
     */
    @SerializedName("uface")
    public String userFace;

    /**
     * 留言id(风控场景下撤回留言需要)
     */
    @SerializedName("message_id")
    public long messageId;

    /**
     * 留言内容
     */
    @SerializedName("message")
    public String message;

    /**
     * 支付金额(元)
     */
    @SerializedName("rmb")
    public long rmb;

    /**
     * 赠送时间秒级
     */
    @SerializedName("timestamp")
    public long timeStamp;

    /**
     * 生效开始时间
     */
    @SerializedName("start_time")
    public long startTime;

    /**
     * 生效结束时间
     */
    @SerializedName("end_time")
    public long endTime;

    /**
     * 对应房间大航海等级
     */
    @SerializedName("guard_level")
    public long guardLevel;

    /**
     * 对应房间勋章信息
     */
    @SerializedName("fans_medal_level")
    public long fansMedalLevel;

    /**
     * 对应房间勋章名字
     */
    @SerializedName("fans_medal_name")
    public String fansMedalName;

    /**
     * 当前佩戴的粉丝勋章佩戴状态
     */
    @SerializedName("fans_medal_wearing_status")
    public boolean fansMedalWearingStatus;
}
