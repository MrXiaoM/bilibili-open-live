package top.mrxiaom.bili.live.client.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppStartInfo {
    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public AppStartData data;

    public static class AppStartData {
        @SerializedName("game_info")
        public AppStartGameInfo gameInfo;
        @SerializedName("websocket_info")
        public AppStartWebsocketInfo websocketInfo;
        @SerializedName("anchor_info")
        public AppStartAnchorInfo anchorInfo;
    }

    public static class AppStartGameInfo {
        @SerializedName("game_id")
        public String gameId;
    }

    public static class AppStartWebsocketInfo {
        @SerializedName("auth_body")
        public String authBody;
        @SerializedName("wss_link")
        public List<String> wssLink;
    }

    public static class AppStartAnchorInfo {
        /**
         * 主播房间号
         */
        @SerializedName("room_id")
        public long roomId;
        /**
         * 主播昵称
         */
        @SerializedName("uname")
        public String uName;
        /**
         * 主播头像
         */
        @SerializedName("uface")
        public String uFace;
        /**
         * 主播Uid
         */
        @SerializedName("uid")
        public String uid;
    }
}
