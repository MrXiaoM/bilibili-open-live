package top.mrxiaom.bili.bukkit;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderHook extends PlaceholderExpansion {
    BukkitMain plugin;

    public PlaceholderHook(BukkitMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bili";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        CraftBLiveClient client = plugin.client;
        if (client == null) {
            return "§7未开播";
        }
        if (params.equalsIgnoreCase("name")) {
            return client.startInfo.data.anchorInfo.uName;
        }
        if (params.equalsIgnoreCase("uid")) {
            return String.valueOf(client.startInfo.data.anchorInfo.uid);
        }
        if (params.equalsIgnoreCase("room_id")) {
            return String.valueOf(client.startInfo.data.anchorInfo.roomId);
        }
        if (params.equalsIgnoreCase("pop")) {
            return String.valueOf(client.getPopularity());
        }
        return super.onRequest(player, params);
    }
}
