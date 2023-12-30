package top.mrxiaom.bili.bukkit.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import top.mrxiaom.bili.live.runtime.data.Guard;
import top.mrxiaom.bili.live.runtime.data.SendGift;

public class LiveGuardBuyEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    Guard data;

    public LiveGuardBuyEvent(Guard data) {
        this.data = data;
    }

    public Guard getData() {
        return data;
    }
}
