package bd.pablo.roidmc.machine.api;

import java.util.function.Predicate;

public enum MachinePermission {

    MANAGER_USERS("01","Gerenciar acessos","Pode remover usuarios da maquina e atribuir permissões á usuarios");

    private final String id;
    private final String displayName;
    private final String info;

    MachinePermission(String id, String displayName, String info) {
        this.id = id;
        this.displayName = displayName;
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getInfo() {
        return info;
    }

    public static MachinePermission findById(String id){
        return find(machinePermission -> machinePermission.id.equalsIgnoreCase(id));
    }

    public static MachinePermission findByDisplayName(String name){
        return find(machinePermission -> machinePermission.displayName.equalsIgnoreCase(name));
    }

    public static MachinePermission findByInfo(String info){
        return find(machinePermission -> machinePermission.info.equalsIgnoreCase(info));
    }

    public static MachinePermission find(Predicate<MachinePermission> machinePermission){
        for(MachinePermission permission : values()){
            if(machinePermission.test(permission))return permission;
        }
        return null;
    }
}
