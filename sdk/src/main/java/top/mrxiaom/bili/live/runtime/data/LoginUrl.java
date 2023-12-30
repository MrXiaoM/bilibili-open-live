package top.mrxiaom.bili.live.runtime.data;

import com.google.gson.annotations.SerializedName;

public class LoginUrl {
    @SerializedName("data")
    public LoginUrlData data;
    @SerializedName("status")
    public boolean status;

    public static class LoginUrlData {
        @SerializedName("oauthKey")
        public String oauthKey;
        @SerializedName("url")
        public String url;
    }
}
