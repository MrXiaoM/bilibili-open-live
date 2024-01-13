package top.mrxiaom.bili.bukkit.utils;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Util {
    public static void registerPlaceholder(Logger logger, PlaceholderExpansion placeholder) {
        PlaceholderAPIPlugin.getInstance()
                .getLocalExpansionManager()
                .findExpansionByIdentifier(placeholder.getIdentifier())
                .ifPresent(PlaceholderExpansion::unregister);
        if (placeholder.isRegistered()) return;
        if (!placeholder.register()) {
            logger.info("无法注册 " + placeholder.getName() + " PAPI 变量");
        }
    }

    public static boolean t(CommandSender sender, String... s) {
        sender.sendMessage(ColorHelper.parseColor(String.join("\n§r", s)));
        return true;
    }

    public static List<String> startsWith(String s, List<String> list) {
        List<String> result = new ArrayList<>();
        for (String s1 : list) {
            if (s1.toLowerCase().startsWith(s.toLowerCase())) result.add(s1);
        }
        return result;
    }
}
