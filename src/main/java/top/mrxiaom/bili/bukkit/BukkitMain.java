package top.mrxiaom.bili.bukkit;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.bili.bukkit.utils.Util;
import top.mrxiaom.bili.live.runtime.utils.SignHolder;

import java.util.List;

import static top.mrxiaom.bili.bukkit.utils.Util.*;

public final class BukkitMain extends JavaPlugin {
    String appId = "";
    String defaultCode = "";
    CraftBLiveClient client = null;
    public boolean debug = false;
    @Override
    public void onEnable() {
        reloadConfig();
        registerPlaceholder(getLogger(), new PlaceholderHook(this));
    }

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();

        FileConfiguration config = getConfig();
        appId = config.getString("app-id");
        defaultCode = config.getString("code");

        String accessKeyId = config.getString("access-key-id");
        String accessKeySecret = config.getString("access-key-secret");
        SignHolder.push(accessKeyId, accessKeySecret);
    }

    @Override
    public void onDisable() {
        if (client != null) {
            client.close();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                t(sender, "&a配置文件已重载");
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("debug")) {
                debug = !debug;
                return t(sender, "&a调试模式已" + (debug ? "开启" : "关闭"));
            }
        }
        if (sender.hasPermission("bili.live")) {
            if (args.length >= 1 && args[0].equalsIgnoreCase("connect")) {
                if (client != null) client.close();
                String code = args.length == 2 ? args[1] : defaultCode;
                Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                    t(sender, "&7正在连接到开放平台");
                    try {
                        client = CraftBLiveClient.connect(this, code, appId);
                        t(sender, "&a连接成功");
                    } catch (Throwable t) {
                        t.printStackTrace();
                        t(sender, "&4连接失败: &c" + t.getMessage(),
                                "&e请联系管理员查阅控制台日志获取详细信息");
                    }
                });
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("close")) {
                if (client != null) client.close();
                return t(sender, "&a已断开连接");
            }
        }
        return true;
    }
    List<String> empty = Lists.newArrayList();
    List<String> arg0_op = Lists.newArrayList("connect", "close", "debug", "reload");
    List<String> arg0_live = Lists.newArrayList("connect", "close");
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            if (sender.isOp()) return startsWith(args[0], arg0_op);
            if (sender.hasPermission("bili.live")) return startsWith(args[0], arg0_live);
        }
        return empty;
    }
}
