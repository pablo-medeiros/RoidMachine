package bd.pablo.roidmc.machine.api;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class MachinePlayer {

    private final String name;
    private final String uuidString;
    private UUID uuid;
    private OfflinePlayer offlinePlayer;
    private final List<MachinePermission> permissions = new ArrayList<>();
    private boolean trusted;
    public MachinePlayer(OfflinePlayer player, boolean trusted) {
        this(player.getName(),player.getUniqueId().toString(),trusted);
        this.offlinePlayer = player;
        this.uuid = player.getUniqueId();
    }

    public MachinePlayer(String name, String uuidString, boolean trusted) {
        this.name = name;
        this.uuidString = uuidString;
        this.trusted = trusted;
    }

    public String getName() {
        return name;
    }

    public UUID getUniqueId() {
        if(uuid==null)uuid=UUID.fromString(uuidString);
        return uuid;
    }

    public String getUniqueIdString() {
        return uuidString;
    }

    public MachinePlayer setTrusted(boolean trusted) {
        this.trusted = trusted;
        return this;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public boolean hasPermission(MachinePermission permission){
        return trusted||permissions.contains(permission);
    }

    public void addPermission(MachinePermission permission){
        if(!permissions.contains(permission))permissions.add(permission);
    }

    public void removePermission(MachinePermission permission){
        permissions.remove(permission);
    }

    public void togglePermission(MachinePermission permission){
        if(!permissions.contains(permission))permissions.add(permission);
        else permissions.remove(permission);
    }

    public OfflinePlayer getOfflinePlayer() {
        if(offlinePlayer==null)offlinePlayer= Bukkit.getOfflinePlayer(getUniqueId());
        return offlinePlayer;
    }
}
