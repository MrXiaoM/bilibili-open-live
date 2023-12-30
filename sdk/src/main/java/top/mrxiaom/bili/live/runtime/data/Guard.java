package top.mrxiaom.bili.live.runtime.data;

import com.google.gson.annotations.SerializedName;

public class Guard {
    /**
     * 大航海等级
     */
    @SerializedName("guard_level")
    public long guardLevel;

    /**
     * 大航海数量
     */
    @SerializedName("guard_num")
    public long guardNum;

    /**
     * 大航海单位 "月"
     */
    @SerializedName("guard_unit")
    public String guardUnit;

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
     * 赠送大航海的用户数据
     */
    @SerializedName("user_info")
    public UserInfo userInfo;

    /**
     * 房间号
     */
    @SerializedName("room_id")
    public long roomID;

    /**
     * 赠送大航海的用户数据
     *
     * @see <a href="https://open-live.bilibili.com/doc/2/2/2">文档</a>
     */
    public static class UserInfo {
        /**
         * 购买大航海的用户UID
         */
        @SerializedName("uid")
        public long uid;

        /**
         * 购买大航海的用户昵称
         */
        @SerializedName("uname")
        public String userName;

        /**
         * 购买大航海的用户头像
         */
        @SerializedName("uface")
        public String userFace;
    }
}
