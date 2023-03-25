package bd.pablo.roidmc.machine.api;

import com.roidmc.core.api.inventory.nbt.NBTItem;
import com.roidmc.core.api.item.RoidItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Machine extends RoidItem {
 
    private String name;
    private List<String> holograms;
    private ItemStack itemStack;
    private List<MachineDrop> drops;
    private int delay;

    public Machine(String name) {
        this.name = name;
        this.holograms = new ArrayList<>();
        this.drops = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Machine setName(String name) {
        this.name = name;
        return this;
    }

    public Machine setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public List<String> getHolograms() {
        return holograms;
    }

    public Machine setHolograms(List<String> holograms) {
        this.holograms.clear();
        this.holograms.addAll(holograms);
        return this;
    }

    public List<MachineDrop> getDrops() {
        return drops;
    }

    public Machine setDrops(List<MachineDrop> drops) {
        this.drops = drops;
        return this;
    }

    public int getDelay() {
        return delay;
    }

    public Machine setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    @Override
    public String getId() {
        return "machine:"+this.name;
    }

    @Override
    public ItemStack create(int i) {
        ItemStack itemStack = this.itemStack.clone();
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.getTag().setString("machine_name",this.name);
        return nbtItem.build();
    }

    @Override
    public boolean isSimilar(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if(nbtItem.getTag().hasKey("machine_name")){
            String name = nbtItem.getTag().getString("machine_name");
            return name.equalsIgnoreCase(this.name);
        }
        return false;
    }

}
