package top.mrxiaom.bili.live.runtime.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 付费留言数据下线
 *
 * @see <a href="https://open-live.bilibili.com/document/f9ce25be-312e-1f4a-85fd-fef21f1637f8">文档</a>
 */
public class SuperChatDel {
    /**
     * 直播间ID
     */
    @SerializedName("room_id")
    public long roomId;

    /**
     * 留言id
     */
    @SerializedName("message_ids")
    public List<Long> messageIds;
}
