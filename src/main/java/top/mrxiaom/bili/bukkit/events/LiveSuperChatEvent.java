package top.mrxiaom.bili.bukkit.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import top.mrxiaom.bili.live.runtime.data.SuperChat;
import top.mrxiaom.bili.live.runtime.data.SuperChatDel;

public class LiveSuperChatEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    SuperChat data;
    public LiveSuperChatEvent(SuperChat data) {
        this.data = data;
    }

    public SuperChat getData() {
        return data;
    }
}
