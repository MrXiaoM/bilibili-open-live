package top.mrxiaom.bili.live.runtime.data;

import com.google.gson.annotations.SerializedName;

/**
 * 服务器返回的Websocket长链接信息
 *
 * @see <a href="https://open-live.bilibili.com/doc/2/1">文档</a>
 */
public class WebsocketInfo {
    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public WebsocketInfoData data;
}
