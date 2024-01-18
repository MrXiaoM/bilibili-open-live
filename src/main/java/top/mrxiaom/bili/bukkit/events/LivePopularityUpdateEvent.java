package top.mrxiaom.bili.bukkit.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class LivePopularityUpdateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    int data;
    int online;
    public LivePopularityUpdateEvent(int data, int online) {
        this.data = data;
        this.online = online;
    }

    public int getData() {
        return data;
    }

    public int getOnline() {
        return online;
    }
}
