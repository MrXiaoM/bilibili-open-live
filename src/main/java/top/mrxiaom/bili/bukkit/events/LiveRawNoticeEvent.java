package top.mrxiaom.bili.bukkit.events;

import com.google.gson.JsonObject;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class LiveRawNoticeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    String data;
    JsonObject json;
    public LiveRawNoticeEvent(String data, JsonObject json) {
        this.data = data;
        this.json = json;
    }

    public String getData() {
        return data;
    }
    public JsonObject getJson() {
        return json;
    }
}
