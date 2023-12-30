package top.mrxiaom.bili.live.runtime.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 服务器返回的Websocket长链接信息
 *
 * @see <a href="https://open-live.bilibili.com/doc/2/1">文档</a>
 */
public class WebsocketInfoData {
    /**
     * ip地址
     */
    @SerializedName("ip")
    public List<String> ip;
    /**
     * host地址 可能是ip 也可能是域名
     */
    @SerializedName("host")
    public List<String> host;
    /**
     * 长连使用的请求json体 第三方无需关注内容,建立长连时使用即可
     */
    @SerializedName("auth_body")
    public String authBody;
    /**
     * tcp 端口号
     */
    @SerializedName("tcp_port")
    public List<Integer> tcpPort;
    /**
     * ws 端口号
     */
    @SerializedName("ws_port")
    public List<Integer> wsPort;
    /**
     * wss 端口号
     */
    @SerializedName("wss_port")
    public List<Integer> wssPort;
}
