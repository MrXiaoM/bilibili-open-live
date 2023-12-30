package top.mrxiaom.bili.live.runtime.data;

import com.google.gson.annotations.SerializedName;

/**
 * 扫码中未登录
 */
public class LoginStatusScanning {
    @SerializedName("status")
    public boolean status;
    @SerializedName("data")
    public int data;
    @SerializedName("message")
    public String message;
}
