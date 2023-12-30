package top.mrxiaom.bili.bukkit.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import top.mrxiaom.bili.live.runtime.data.Dm;

public class LiveDanmakuEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    Dm data;
    public LiveDanmakuEvent(Dm data) {
        this.data = data;
    }

    public Dm getData() {
        return data;
    }
}
