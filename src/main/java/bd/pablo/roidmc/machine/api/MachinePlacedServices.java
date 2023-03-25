package bd.pablo.roidmc.machine.api;

import com.roidmc.core.api.RoidService;
import com.roidmc.core.util.RoidCache;

import java.util.*;

public class MachinePlacedServices implements RoidService<MachinePlaced> {

    private RoidCache<String,MachinePlaced> cache = new RoidCache<String, MachinePlaced>() {
        @Override
        protected MachinePlaced search(String s, LinkedHashMap<String, MachinePlaced> linkedHashMap) {
            return null;
        }
    };
    public static final MachinePlacedServices inst = new MachinePlacedServices();

    @Override
    public boolean register(String s, MachinePlaced machine) {
        if(s==null||s.isEmpty()||machine==null)return false;
        cache.put(s,machine);
        return true;
    }

    public void unRegister(String s) {
        if(s==null||s.isEmpty())return;
        cache.remove(s);
        return;
    }

    @Override
    public MachinePlaced find(String s) {
        if(s==null||s.isEmpty())return null;
        return cache.find(s);
    }

    public Collection<MachinePlaced> findAll(){
        return new ArrayList<>(cache.values());
    }

    @Override
    public String name() {
        return "machine_placed";
    }
}
