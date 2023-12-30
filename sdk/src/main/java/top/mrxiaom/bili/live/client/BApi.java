package top.mrxiaom.bili.live.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import top.mrxiaom.bili.live.runtime.data.GameIds;
import top.mrxiaom.bili.live.runtime.utils.SignHolder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BApi {
    public static final Gson gson = new GsonBuilder().create();

    /**
     * 是否为测试环境的api
     */
    public static boolean isTestEnv = false;

    /**
     * 开放平台域名
     */
    private static String getOpenLiveDomain() {
        return isTestEnv ? "http://test-live-open.biliapi.net" : "https://live-open.biliapi.com";
    }

    /**
     * 应用开启
     */
    private static final String k_InteractivePlayStart = "/v2/app/start";

    /**
     * 应用关闭
     */
    private static final String k_InteractivePlayEnd = "/v2/app/end";

    /**
     * 应用心跳
     */
    private static final String k_InteractivePlayHeartBeat = "/v2/app/heartbeat";

    /**
     * 应用批量心跳
     */
    private static final String k_InteractivePlayBatchHeartBeat = "/v2/app/batchHeartbeat";

    public static String requestWebUTF8(String url, String param) {
        return requestWebUTF8(url, param, null);
    }

    public static String requestWebUTF8(String url, String param, String cookie) {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpEntityEnclosingRequestBase post = new HttpPost(url);
            if (param != null) {
                SignHolder.setReqHeader(post, param, cookie);
            }
            CloseableHttpResponse resp = client.execute(post);
            try (InputStream input = resp.getEntity().getContent()) {
                StringWriter writer = new StringWriter();

                char[] buffer = new char[1024];
                try (Reader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
                    int n;
                    while ((n = reader.read(buffer)) != -1) {
                        writer.write(buffer, 0, n);
                    }
                }
                return writer.toString();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String startInteractivePlay(String code, String appId) {
        var postUrl = getOpenLiveDomain() + k_InteractivePlayStart;
        var param = "{\"code\":\"" + code + "\",\"app_id\":" + appId + "}";

        return requestWebUTF8(postUrl, param);
    }

    public static String endInteractivePlay(String appId, String gameId) {
        var postUrl = getOpenLiveDomain() + k_InteractivePlayEnd;
        var param = "{\"app_id\":" + appId + ",\"game_id\":\"" + gameId + "\"}";

        return requestWebUTF8(postUrl, param);
    }

    public static String heartBeatInteractivePlay(String gameId) {
        var postUrl = getOpenLiveDomain() + k_InteractivePlayHeartBeat;
        String param = "";
        if (gameId != null) {
            param = "{\"game_id\":\"" + gameId + "\"}";
        }

        return requestWebUTF8(postUrl, param);
    }

    public static String batchHeartBeatInteractivePlay(List<String> gameIds) {
        var postUrl = getOpenLiveDomain() + k_InteractivePlayBatchHeartBeat;
        GameIds games = new GameIds();
        games.gameIds = gameIds;

        var param = gson.toJson(games);
        return requestWebUTF8(postUrl, param);
    }
}
