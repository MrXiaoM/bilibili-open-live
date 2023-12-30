package top.mrxiaom.bili.live.runtime.data;

import com.google.gson.annotations.SerializedName;

/**
 * 登录成功
 */
public class LoginStatusReady {
    @SerializedName("code")
    public int code;
    @SerializedName("status")
    public boolean status;
    @SerializedName("data")
    public LoginStatusData data;

    public static class LoginStatusData {
        @SerializedName("url")
        public String url;
    }

}
