package top.mrxiaom.bili.live.client;

import top.mrxiaom.bili.live.client.BApi;
import top.mrxiaom.bili.live.client.data.AppStartInfo;
import top.mrxiaom.bili.live.client.data.EmptyInfo;

import java.util.List;

import static top.mrxiaom.bili.live.client.BApi.gson;

public class BApiClient {
    public static AppStartInfo startInteractivePlay(String code, String appId) {
        String json = BApi.startInteractivePlay(code, appId);
        return gson.fromJson(json, AppStartInfo.class);
    }

    public static EmptyInfo endInteractivePlay(String appId, String gameId) {
        String json = BApi.endInteractivePlay(appId, gameId);
        return gson.fromJson(json, EmptyInfo.class);
    }

    public static EmptyInfo heartBeatInteractivePlay(String gameId) {
        String json = BApi.heartBeatInteractivePlay(gameId);
        return gson.fromJson(json, EmptyInfo.class);
    }

    public static EmptyInfo batchHeartBeatInteractivePlay(List<String> gameIds) {
        String json = BApi.batchHeartBeatInteractivePlay(gameIds);
        return gson.fromJson(json, EmptyInfo.class);
    }
}
