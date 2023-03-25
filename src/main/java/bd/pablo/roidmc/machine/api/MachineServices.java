package bd.pablo.roidmc.machine.api;

import com.roidmc.core.api.RoidService;
import com.roidmc.core.util.RoidCache;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;

public class MachineServices implements RoidService<Machine> {

    public static MachineServices inst = new MachineServices();

    private RoidCache<String,Machine> cache = new RoidCache<String, Machine>() {
        @Override
        protected Machine search(String s, LinkedHashMap<String, Machine> linkedHashMap) {
            return null;
        }
    };

    @Override
    public boolean register(String s, Machine machine) {
        if(s==null||s.isEmpty()||machine==null)return false;
        cache.put(s,machine);
        return true;
    }

    public boolean unRegister(String s) {
        if(s==null||s.isEmpty())return false;
        cache.remove(s);
        return true;
    }

    public boolean register(Machine machine){
        if(machine==null||machine.getId()==null||machine.getId().isEmpty()||machine.getName()==null||machine.getName().isEmpty())return false;
        cache.put(machine.getId(),machine);
        cache.put(machine.getName(),machine);
        return true;
    }

    @Override
    public Machine find(String s) {
        if(s==null||s.isEmpty())return null;
        return cache.find(s);
    }

    public Machine find(ItemStack itemStack){
        for(Machine machine : cache.values()){
            if(machine.isSimilar(itemStack))return machine;
        }
        return null;
    }

    @Override
    public String name() {
        return "machine";
    }
}
