package me.frostythedev.bungeeutils.ui;

import me.frostythedev.bungeeutils.BungeeUtils;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Programmed by Tevin on 12/5/2015.
 */
public class SendConsole {

    private static Plugin plugin;
    private static String prefix;

    public SendConsole(Plugin plugin) {
        SendConsole.plugin = plugin;
        prefix = "[" + plugin.getDescription().getName() + "]";
    }

    public static void info(String message){
        plugin.getLogger().info(prefix + message);
    }

    public static void warning(String message){
        plugin.getLogger().warning(prefix + message);
    }

    public static void servere(String message){
        plugin.getLogger().severe(prefix + message);
    }
}
