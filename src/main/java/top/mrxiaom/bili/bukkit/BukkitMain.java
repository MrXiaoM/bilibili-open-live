package top.mrxiaom.bili.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import top.mrxiaom.bili.bukkit.utils.Util;
import top.mrxiaom.bili.live.runtime.utils.SignHolder;

import static top.mrxiaom.bili.bukkit.utils.Util.registerPlaceholder;

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
                Util.t(sender, "&a配置文件已重载");
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("debug")) {
                debug = !debug;
                return Util.t(sender, "&a调试模式已" + (debug ? "开启" : "关闭"));
            }
        }
        if (sender.hasPermission("bili.live")) {
            if (args.length >= 1 && args[0].equalsIgnoreCase("connect")) {
                if (client != null) client.close();
                String code = args.length == 2 ? args[1] : defaultCode;
                Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                    Util.t(sender, "&7正在连接到开放平台");
                    try {
                        client = CraftBLiveClient.connect(this, code, appId);
                        Util.t(sender, "&a连接成功");
                    } catch (Throwable t) {
                        t.printStackTrace();
                        Util.t(sender, "&4连接失败: &c" + t.getMessage(),
                                "&e请联系管理员查阅控制台日志获取详细信息");
                    }
                });
            }
        }
        return true;
    }
}
