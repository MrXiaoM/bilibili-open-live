package top.mrxiaom.bili.live.runtime.data;

import com.google.gson.annotations.SerializedName;

/**
 * 礼物数据
 *
 * @see <a href="https://open-live.bilibili.com/document/f9ce25be-312e-1f4a-85fd-fef21f1637f8">文档</a>
 */
public class SendGift {

    /**
     * 房间号
     */
    @SerializedName("room_id")
    public long roomId;

    /**
     * 送礼用户UID
     */
    @SerializedName("uid")
    public long uid;

    /**
     * 送礼用户昵称
     */
    @SerializedName("uname")
    public String userName;

    /**
     * 送礼用户头像
     */
    @SerializedName("uface")
    public String userFace;

    /**
     * 道具id(盲盒:爆出道具id)
     */
    @SerializedName("gift_id")
    public long giftId;

    /**
     * 道具名(盲盒:爆出道具名)
     */
    @SerializedName("gift_name")
    public String giftName;

    /**
     * 赠送道具数量
     */
    @SerializedName("gift_num")
    public long giftNum;

    /**
     * 支付金额(1000 = 1元 = 10电池),盲盒:爆出道具的价值
     */
    @SerializedName("price")
    public long price;

    /**
     * 是否真的花钱(电池道具)
     */
    @SerializedName("paid")
    public boolean paid;

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
     * 收礼时间秒级时间戳
     */
    @SerializedName("timestamp")
    public long timestamp;

    /**
     * 主播信息
     */
    @SerializedName("anchor_info")
    public AnchorInfo anchorInfo;


    /**
     * 礼物数据中的主播数据
     *
     * @see <a href="https://open-live.bilibili.com/doc/2/2/1">文档</a>
     */
    public static class AnchorInfo {
        /**
         * 收礼主播UID
         */
        @SerializedName("uid")
        public long uid;

        /**
         * 收礼主播昵称
         */
        @SerializedName("uname")
        public String userName;

        /**
         * 收礼主播头像
         */
        @SerializedName("uface")
        public String userFace;
    }

}
