package me.frostythedev.bungeeutils.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import me.frostythedev.bungeeutils.BungeeUtils;
import me.frostythedev.bungeeutils.config.FlatFile;
import me.frostythedev.bungeeutils.managers.PermManager;
import me.frostythedev.bungeeutils.managers.PlayerManager;
import me.frostythedev.bungeeutils.perms.PermGroup;
import me.frostythedev.bungeeutils.perms.PermUser;
import me.frostythedev.bungeeutils.utils.ColorUtils;
import me.frostythedev.bungeeutils.utils.MessageUtils;
import me.frostythedev.bungeeutils.ux.Lang;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Programmed by Tevin on 1/25/2016.
 */
public class BungeeCommands {

    private static FlatFile config = BungeeUtils.getInstance().getPerms();

    @Command(
            aliases = {"hub", "lobby"},
            desc = "Teleport to the lobby"
    )
    public static void hub(CommandSender sender) throws CommandException {
        if (sender instanceof ProxiedPlayer) {

            ServerInfo info = ProxyServer.getInstance().getServerInfo(config.getString("hub-server"));

            if (info != null) {
                ((ProxiedPlayer) sender).connect(info);
                Lang.message(sender, Lang.HUB_MESSAGE);
            } else {
                Lang.message(sender, "&cUnable to connect to lobby. Try again later!");
            }
        } else {
            Lang.message(sender, Lang.COMMAND_MUST_BE_PLAYER);
        }
    }

    @Command(
            aliases = {"m", "reply", "message", "msg"},
            desc = "Cross server messaging",
            usage = "<player> [message]",
            min = 1
    )
    public static void message(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args.getString(0));

            if (target == null) {
                Lang.message(sender, Lang.formulate(Lang.CANNOT_FIND_PLAYER, args.getString(0)));
            } else {
                String message = args.getRemainingString(1);

                Lang.message(player, "&aYou -> " + target.getName() + ": " + message);
                Lang.message(target, "&a" + player.getName() + " -> You: " + message);

                Lang.broadcast("bungeeutils.socialspy", "&9" + player.getName() + " &7-> &e" + target.getName() + ":&a " + message);
            }
        }

    }

    @Command(
            aliases = {"find"},
            desc = "Finds a player with that name",
            usage = "<name>",
            min = 1,
            max = 1
    )
    public static void find(final CommandContext args, CommandSender sender) throws CommandException {
        String name = args.getString(0);
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);

        if (player == null) {
            Lang.message(sender, Lang.formulate(Lang.CANNOT_FIND_PLAYER, name));
        } else {
            Lang.message(sender, "&6" + name + " &7is currently connected to &9" + player.getServer().getInfo().getName() + "!");
        }
    }

    @Command(
            aliases = {"kick"},
            desc = "Kicks a player from the proxy",
            usage = "<name> [reason]",
            min = 1
    )
    public static void kick(final CommandContext args, CommandSender sender) throws CommandException {
        String name = args.getString(0);
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);

        if (player == null || player.getName().equals(sender.getName())) {
            Lang.message(sender, Lang.formulate(Lang.CANNOT_FIND_PLAYER, name));
        } else {
            String reason = args.getRemainingString(1);
            if (reason.equals("") || reason.equals(" ")) {
                reason = Lang.KICK_MESSAGE;
            }

            player.disconnect(ColorUtils.toColors(reason));
            Lang.broadcast("bungeeutils.admin", "");
        }
    }

    @Command(
            aliases = {"report"},
            desc = "Reports a player",
            usage = "<name> <reason>",
            min = 2
    )
    public static void report(final CommandContext args, CommandSender sender) throws CommandException {
        String name = args.getString(0);
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);

        if (player == null || player.getName().equals(sender.getName())) {
            Lang.message(sender, Lang.formulate(Lang.CANNOT_FIND_PLAYER, name));
        } else {
            String reason = args.getRemainingString(1);
            Lang.broadcast("bungeeutils.report.receive", "&c" + name + " has been reported by " + sender.getName() + " for " + reason);
        }
    }

    @Command(
            aliases = {"reqmod", "staffhelp"},
            desc = "Send staff a message if you need any help",
            usage = "<reason>",
            min = 1
    )
    public static void reqmod(final CommandContext args, CommandSender sender) throws CommandException {
        if (!(sender instanceof ProxiedPlayer)) {
            Lang.message(sender, Lang.COMMAND_MUST_BE_PLAYER);
        } else {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            String reason = args.getRemainingString(0);
            String server = player.getServer().getInfo().getName();

            Lang.broadcast("bungeeutils.reqmod.receive", "&6" + sender.getName() + " &7has requested help with &d" + reason
                    + " on &9" + server + "!");
        }
    }

    @Command(
            aliases = {"staffchat", "sc", "modchat", "mc"},
            desc = "Sends a message to staffchat",
            usage = "<message>",
            min = 1
    )
    public static void staffchat(final CommandContext args, CommandSender sender) throws CommandException {
        if (!(sender instanceof ProxiedPlayer)) {
            Lang.message(sender, Lang.COMMAND_MUST_BE_PLAYER);
        } else {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            String message = args.getRemainingString(0);

            Lang.broadcast("bungeeutils.staffchat", "&6[SC] " + player.getName() + ": " + message);
        }
    }

    @Command(
            aliases = {"alert", "massbc", "sm"},
            desc = "Broadcasts a message to the entire server",
            usage = "<message>",
            min = 1
    )
    public static void alert(final CommandContext args) throws CommandException {
        String message = args.getRemainingString(0);
        Lang.broadcast(null, "&8[&4ALERT&8] &f&l-> &6" + message);
    }

    @Command(
            aliases = {"perms", "permsex"},
            desc = "Perms-Bungee Edition",
            usage = "help",
            min = 1
    )
    @CommandPermissions("bungeeutils.admin")
    public static void perms(final CommandContext args, CommandSender sender) throws CommandException {
        FlatFile perms = new FlatFile("perms.yml", "plugins/BungeeUtils");
        perms.createFile();
        perms.loadFile();

        if (args.getString(0).equalsIgnoreCase("help")) {
            Lang.message(sender, "&d[&9Perms&d] &6Help");
            Lang.message(sender, "&9/perms user &f<user> &7group set &d<group> &f- Sets the group of that player");
            Lang.message(sender, "&9/perms user &f<user> &7group remove &d<group> &f- Removes the group of that player");
            Lang.message(sender, "&9/perms user &f<user> &dinfo &7- Shows info for that specific player");
            Lang.message(sender, "&9/perms group &f<group> &acreate &7- Creates or clears a group's permissions");
            Lang.message(sender, "&9/perms group &f<group> &6delete &7- Deletes a group and clears its permissions");
            Lang.message(sender, "&9/perms group &f<group> &dinfo &7- Shows information about that group");
            Lang.message(sender, "&9/perms &dgroups &7- Lists all groups and their information");


        } else if (args.getString(0).equalsIgnoreCase("user")) {

            if (args.argsLength() == 3) {

                String name = args.getString(1);
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);

                if (target == null) {
                    Lang.message(sender, Lang.formulate(Lang.CANNOT_FIND_PLAYER));
                } else {
                    if (args.getString(2).equalsIgnoreCase("info")) {

                        PermUser user = PlayerManager.getInstance().getPlayer(target);

                        Lang.message(sender, "&6&l&nPlayer Information:");
                        Lang.message(sender, " ");
                        Lang.message(sender, "&eName: " + target);
                        Lang.message(sender, "&eGroups:");
                        for (PermGroup pg : user.getPlayerGroups()) {
                            Lang.message(sender, "&f&l* " + pg.getName());
                        }
                    }
                }
            }

            if (args.argsLength() == 5) {

                String name = args.getString(1);
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);

                if (target == null) {
                    Lang.message(sender, Lang.formulate(Lang.CANNOT_FIND_PLAYER));
                    return;
                }

                PermUser user = PlayerManager.getInstance().getPlayer(target.getUniqueId());

                if (args.getString(2).equalsIgnoreCase("group")) {

                    String groupName = args.getString(4);
                    PermGroup pg = PermManager.getInstance().getGroupByName(groupName);

                    if (args.getString(3).equalsIgnoreCase("set")) {

                        if (pg != null) {
                            pg.setGroup(user);
                            Lang.message(sender, "&9You have just add group &d'" + pg.getName() + "' &9to '" + target.getName() + "'!");
                        } else {
                            Lang.message(sender, "&cCould not find group with name '" + groupName + "'!");
                        }

                    } else if (args.getString(3).equalsIgnoreCase("remove")) {

                        if (pg != null) {
                            pg.removeGroup(user);
                            Lang.message(sender, "&9You have just removed group &d'" + pg.getName() + "' &9from '" + target.getName() + "'!");
                        } else {
                            Lang.message(sender, "&cCould not find group with name '" + groupName + "'!");
                        }
                    }
                }
            }

        } else if (args.getString(0).equalsIgnoreCase("group")) {


            if (args.argsLength() == 3) {

                String groupName = args.getString(1);

                if (args.getString(2).equalsIgnoreCase("create")) {

                    PermGroup group = new PermGroup(groupName);
                    group.update();
                    Lang.message(sender, "&aYou have just created/refreshed a group with name '" + groupName + "'.");

                } else if (args.getString(2).equalsIgnoreCase("delete")) {

                    PermGroup group = PermManager.getInstance().getGroupByName(groupName);
                    if (group.voidObject()) {
                        Lang.message(sender, "&aYou have just removed group with name '" + groupName + "'.");
                    } else {
                        Lang.message(sender, "&cCould not find group with name '" + groupName + "'.");
                    }

                } else if (args.getString(2).equalsIgnoreCase("info")) {

                    if (perms.getConfig().getStringList("Groups." + groupName) != null) {
                        Lang.message(sender, "&6&l&nGroup Information:");

                        List<String> permissions = perms.getConfig().getStringList("Groups." + groupName);
                        for (String perm : permissions) {
                            Lang.message(sender, "&f&l* " + perm);
                        }
                    }
                }
            }
        } else if (args.getString(0).equalsIgnoreCase("groups")) {

            Lang.message(sender, "&6&l&nAll Groups Information:");
            Lang.message(sender, " ");

            Collection<String> groups = PermManager.getInstance().getGroupNames();
            for (String group : groups) {
                Lang.message(sender, "&e&l* " + group);
            }

        } else if (args.getString(0).equalsIgnoreCase("reload")) {

        }
    }
}
