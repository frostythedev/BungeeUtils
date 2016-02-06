package me.frostythedev.bungeeutils.listeners;

import me.frostythedev.bungeeutils.managers.PlayerManager;
import me.frostythedev.bungeeutils.ux.Lang;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Programmed by Tevin on 1/25/2016.
 */
public class GeneralListeners implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        PlayerManager.getInstance().getPlayer(player.getUniqueId());

        if (player.getName().equalsIgnoreCase("frostythedev") || player.hasPermission("bungeeutils.announce")){
            Lang.broadcast(null, "&c&l[&4BM&c&l] &2" + player.getName() + " &7has &econnected &7to -> &a" + player.getServer().getInfo().getName() + "!");
        }
    }

}
