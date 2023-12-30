package top.mrxiaom.bili.live.runtime.data;

public class BToken {

    /**
     * -1=non oauthKey<br/>
     * -2=oauthKey not marching<br/>
     * -4=未扫码<br/>
     * -5=已扫码<br/>
     * 0=登录成功
     */
    public int code;
    /**
     * 登录用户的uid
     */
    public long dedeUserID;
    /**
     * 用户md5key
     */
    public String dedeUserIDCkMd5;
    /**
     * accesskey 或 浏览器中的cookie
     */
    public String sessData;
    /**
     * 登录cookie
     */
    public String biliJct;
}
