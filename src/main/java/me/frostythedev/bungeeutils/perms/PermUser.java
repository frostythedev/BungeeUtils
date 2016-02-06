package me.frostythedev.bungeeutils.perms;

import me.frostythedev.bungeeutils.BungeeUtils;
import me.frostythedev.bungeeutils.config.FlatFile;
import me.frostythedev.bungeeutils.managers.PermManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Programmed by Tevin on 1/25/2016.
 */
public class PermUser {

    private String uuid;
    private List<PermGroup> playerGroups;

    public PermUser(String uuid) {
        this(uuid, new ArrayList<>());
    }

    public PermUser(String uuid, List<PermGroup> playerGroups) {
        this.uuid = uuid;
        this.playerGroups = playerGroups;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<PermGroup> getPlayerGroups() {
        return playerGroups;
    }

    public void setPlayerGroups(List<PermGroup> playerGroups) {
        this.playerGroups = playerGroups;
    }

    public boolean hasPermission(String permission){
        for(PermGroup pg : getPlayerGroups()){
            for(String str : pg.getPermissions()){
                if(str.equalsIgnoreCase(permission)){
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String toString(){

        String groups = "";
        for(PermGroup group : getPlayerGroups()){
          if(groups.equalsIgnoreCase("")){
              groups = groups + group.getName();
          }else{
              groups = groups + "," + group.getName();
          }
        }

        return "PermUser{" +
                "uuid=" + getUuid() +
                "groups=" + groups +
                "}";
    }

    public static PermUser getUserFromUUID(UUID uuid) {

        PermUser user = new PermUser(uuid.toString());

        for (String keys : BungeeUtils.getInstance().getPerms().getStringList("Players." + uuid.toString())) {
            PermGroup group = PermManager.getInstance().getGroupByName(keys);
            if(group != null){
                user.getPlayerGroups().add(group);
            }
        }

        return user;
    }
}
