package me.frostythedev.bungeeutils.managers;

import io.netty.util.internal.ConcurrentSet;
import me.frostythedev.bungeeutils.config.FlatFile;
import me.frostythedev.bungeeutils.perms.PermGroup;
import me.frostythedev.bungeeutils.perms.PermUser;

import java.util.*;

/**
 * Programmed by Tevin on 1/25/2016.
 */
public class PermManager {

    private static PermManager instance;

    public static PermManager getInstance() {
        if (instance == null) {
            instance = new PermManager();
        }
        return instance;
    }

    private FlatFile perms = new FlatFile("perms.yml", "plugins/BungeeUtils");

    public PermManager() {
    }

    private ArrayList<PermGroup> loadedGroups = new ArrayList<>();
    private Map<UUID, PermUser> loadedUsers = new HashMap<>();

    public PermGroup getGroupByName(String name) {
        for (PermGroup pg : getLoadedGroups()) {
            if (pg.getName().equalsIgnoreCase(name)) return pg;
        }
        return null;
    }

    public Collection<String> getGroupNames() {
        Collection<String> names = new ArrayList<>();
        getLoadedGroups().forEach(group -> names.add(group.getName()));

        return names;
    }

    public void updateGroups() {
        getLoadedGroups().clear();
        for (String keys : perms.getSection("Groups").getKeys()) {
            PermGroup group = new PermGroup(keys);
            getLoadedGroups().add(group);
        }

        updateUsers();
    }

    public void updateUsers(){
        getLoadedUsers().clear();
        for (String keys : perms.getSection("Players").getKeys()) {
            UUID uuid = UUID.fromString(keys);
            PermUser user = PermUser.getUserFromUUID(uuid);

            getLoadedUsers().put(uuid, user);
        }
    }

    public ArrayList<PermGroup> getLoadedGroups() {
        return loadedGroups;
    }

    public Map<UUID, PermUser> getLoadedUsers() {
        return loadedUsers;
    }
}
