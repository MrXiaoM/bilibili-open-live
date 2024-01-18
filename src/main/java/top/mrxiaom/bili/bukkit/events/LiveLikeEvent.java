package top.mrxiaom.bili.bukkit.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import top.mrxiaom.bili.live.runtime.data.Dm;
import top.mrxiaom.bili.live.runtime.data.Like;

public class LiveLikeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    Like data;
    public LiveLikeEvent(Like data) {
        this.data = data;
    }

    public Like getData() {
        return data;
    }
}
