package me.frostythedev.bungeeutils.ux;

import me.frostythedev.bungeeutils.utils.ColorUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Objects;

/**
 * Programmed by Tevin on 12/5/2015.
 */
public class Lang {
    public static String PREFIX = "&6[&e&lBUNGEE&6] &7";

    public static String CANNOT_FIND_PLAYER = "&cUnable to find player with name '%s'.";
    public static String CANNOT_FIND_SERVER = "&cCould not perform action to %s because it is either offline or does not exist.";

    public static String COMMAND_MUST_BE_PLAYER = "&cYou must be a player to execute this command.";

    public static String HUB_MESSAGE = "&aConnecting to the hub...";
    public static String KICK_MESSAGE = "&cYou have been kicked from this proxy.";

    public static String SERVER_CREATE_MESSAGE = "&aYou have just created server '%s'!";
    public static String SERVER_DELETE_MESSAGE = "&eYou have just deleted server '%s'!";


    public static String formulate(String message, Object... objects) {
        return String.format(message, objects);
    }

    public static void message(CommandSender sender, String message) {
        sender.sendMessage(ColorUtils.toColors(message));
    }

    public static void broadcast(String permission, String message) {
        for (ProxiedPlayer pp : ProxyServer.getInstance().getPlayers()) {
            if (permission != null) {
                if (pp.hasPermission(permission)) {
                    message(pp, message);
                }
            } else {
                message(pp, message);
            }
        }
    }


}
