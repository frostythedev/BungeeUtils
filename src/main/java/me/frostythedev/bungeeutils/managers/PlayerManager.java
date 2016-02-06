package me.frostythedev.bungeeutils.managers;

import me.frostythedev.bungeeutils.perms.PermUser;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Programmed by Tevin on 1/25/2016.
 */
public class PlayerManager {

    private static PlayerManager instance;

    public static PlayerManager getInstance() {
        if(instance == null){
            instance = new PlayerManager();
        }
        return instance;
    }

    private Map<UUID, PermUser> cachedPlayers = new HashMap<>();

    public PermUser getPlayer(UUID uuid){
        if(cachedPlayers.containsKey(uuid)){
            return cachedPlayers.get(uuid);
        }else{
            cachedPlayers.put(uuid, PermUser.getUserFromUUID(uuid));
            return getPlayer(uuid);
        }
    }

    public PermUser getPlayer(ProxiedPlayer player){
        return getPlayer(player.getUniqueId());
    }

    public Map<UUID, PermUser> getCachedPlayers() {
        return cachedPlayers;
    }
}
