package me.frostythedev.bungeeutils;

import me.frostythedev.bungeeutils.commands.*;
import me.frostythedev.bungeeutils.config.FlatFile;
import me.frostythedev.bungeeutils.managers.PermManager;
import me.frostythedev.bungeeutils.ui.SendConsole;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import com.sk89q.bungee.util.BungeeCommandsManager;
import com.sk89q.bungee.util.CommandExecutor;
import com.sk89q.bungee.util.CommandRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

/**
 * Programmed by Tevin on 12/5/2015.
 */
public class BungeeUtils extends Plugin implements Listener, CommandExecutor<CommandSender> {

    private FlatFile perms;
    private BungeeCommandsManager commands;

    private static BungeeUtils instance;

    // Socket Implementation

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;

        new SendConsole(this);

        SendConsole.info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        SendConsole.info("Plugin: " + getDescription().getName() + " v" + getDescription().getVersion());
        SendConsole.info("Author: frostythedev");
        SendConsole.info("Site: http://www.visioncoding.org/");
        SendConsole.info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");
        File storage = new File(getDataFolder(), "perms.yml");


        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!storage.exists()) {
            try (InputStream in = getResourceAsStream("perms.yml")) {
                Files.copy(in, storage.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        perms = new FlatFile("perms.yml", "plugins/BungeeUtils");

        registerCommands();

        PermManager.getInstance().updateGroups();
    }

    public FlatFile getPerms() {
        return perms;
    }

    public void setPerms(FlatFile perms) {
        this.perms = perms;
    }

    private void registerCommands() {
        PluginManager pluginManager = this.getProxy().getPluginManager();

        this.commands = new BungeeCommandsManager();

        CommandRegistration registrar = new CommandRegistration(this, pluginManager, this.commands, this);
        registrar.register(BungeeCommands.class);
    }

    @Override
    public void onCommand(CommandSender sender, String commandName, String[] args) {
        try {
            this.commands.execute(commandName, args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "You do not have permission to execute this command."));
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "An error has occurred, check Console for more information."));
        } catch (CommandUsageException e) {
            sender.sendMessage(new TextComponent(ChatColor.RED + e.getMessage()));
            sender.sendMessage(new TextComponent(ChatColor.RED + e.getUsage()));
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Number expected, string recieved instead."));
            } else {
                sender.sendMessage(new TextComponent(ChatColor.RED + "An error has occurred, check Console for more information."));
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "An error has occurred, " + e.getMessage()));

        }
    }

    public BungeeCommandsManager getCommands() {
        return commands;
    }

    public static BungeeUtils getInstance() {
        return instance;
    }
}
