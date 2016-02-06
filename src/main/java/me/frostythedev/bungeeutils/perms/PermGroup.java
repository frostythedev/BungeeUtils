package me.frostythedev.bungeeutils.perms;

import me.frostythedev.bungeeutils.BungeeUtils;
import me.frostythedev.bungeeutils.config.FlatFile;
import me.frostythedev.bungeeutils.managers.PermManager;
import me.frostythedev.bungeeutils.ux.Lang;

import java.util.ArrayList;
import java.util.List;

/**
 * Programmed by Tevin on 1/25/2016.
 */
public class PermGroup {

    private final FlatFile perms = BungeeUtils.getInstance().getPerms();

    private String name;
    private List<String> permissions;

    public PermGroup(String name) {
        setName(name);
        setPermissions(loadPermissions());
    }

    public PermGroup(String name, List<String> permissions) {
        setName(name);
        setPermissions(permissions);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> loadPermissions(){
        List<String> list = perms.getConfig().getStringList("Groups." + getName());

        return list != null ? list : new ArrayList<>();
    }

    public void update(){
        perms.getConfig().set("Groups." + getName(), getPermissions());
        perms.saveFile();

        PermManager.getInstance().updateGroups();
    }

    public boolean voidObject(){
        if (perms.getConfig().getStringList("Groups." + getName()) != null) {
            perms.getConfig().set("Groups." + getName(), null);
            PermManager.getInstance().updateGroups();
            return true;
        } else {
            PermManager.getInstance().updateGroups();
            return false;
        }
    }

    public void setGroup(PermUser user){
        List<String> list = perms.getConfig().getStringList("Players." + user.getUuid());
        if(list == null) list = new ArrayList<>();

        list.add(getName());

        perms.getConfig().set("Players." + user.getUuid(), list);
        perms.saveFile();

        PermManager.getInstance().updateGroups();

    }

    public void removeGroup(PermUser user){
        List<String> list = perms.getConfig().getStringList("Players." + user.getUuid());
        if(list == null) return;

        if(list.contains(getName())){
            list.remove(getName());
        }

        perms.getConfig().set("Players." + user, list);
        perms.saveFile();

        PermManager.getInstance().updateGroups();
    }
}
