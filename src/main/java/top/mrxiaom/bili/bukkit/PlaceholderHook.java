package top.mrxiaom.bili.bukkit;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
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
        CraftBLiveClient client = plugin.getClient();
        if (params.equalsIgnoreCase("status")) {
            return bool(client != null && client.getStatus());
        }
        if (params.equalsIgnoreCase("status_display")) {
            boolean status = client != null && client.getStatus();
            return status ? "§a已连接" : "§7未连接";
        }

        if (client == null) {
            return "§7未连接";
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
        if (params.equalsIgnoreCase("online")) {
            return String.valueOf(client.getOnline());
        }
        return super.onRequest(player, params);
    }

    public static String bool(boolean value) {
        return value ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
    }
}
