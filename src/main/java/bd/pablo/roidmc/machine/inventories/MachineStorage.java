package bd.pablo.roidmc.machine.inventories;

import bd.pablo.roidmc.machine.RoidMachine;
import bd.pablo.roidmc.machine.api.MachineDrop;
import bd.pablo.roidmc.machine.api.MachinePlaced;
import bd.pablo.roidmc.machine.api.MachinePlayer;
import bd.pablo.roidmc.machine.util.Formatter;
import com.roidmc.core.api.inventory.RoidInventory;
import com.roidmc.core.api.inventory.RoidSlot;
import com.roidmc.core.api.item.RoidItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MachineStorage {

    private MachinePlaced machinePlaced;
    private Map<MachineDrop, Long> storage = new LinkedHashMap<>();
    private List<StorageInventory> inventories = new ArrayList<>();
    private boolean enable;
    private double minimumSale = -1;

    public MachineStorage(MachinePlaced machinePlaced) {
        this.machinePlaced = machinePlaced;
    }

    public void reload(){
        inventories.forEach(StorageInventory::reload);
    }

    public boolean isEnable() {
        return enable;
    }

    public MachineStorage setEnable(boolean enable) {
        this.enable = enable;
        return this;
    }

    public StorageInventory getInventory(MachinePlayer player){
        for(StorageInventory storageInventory : inventories){
            if(storageInventory.owner.toString().equals(player.getUniqueIdString()))return storageInventory;
        }
        StorageInventory storageInventory = new StorageInventory(player.getUniqueId());
        inventories.add(storageInventory);
        return storageInventory;
    }

    public void open(MachinePlayer player){
        if(player.getOfflinePlayer().getPlayer()==null)return;
        player.getOfflinePlayer().getPlayer().openInventory(getInventory(player).roidInventory.getInventory());
    }

    public void store(MachineDrop drop, long amount){
        long total = storage.getOrDefault(drop,0L) + amount;
        storage.put(drop,total);
        minimumSale=-1;
        reload();
        machinePlaced.getPanel().reload();
    }

    public long get(MachineDrop drop){
        return storage.getOrDefault(drop,0L);
    }

    public double getMinimumSale() {
        if(minimumSale!=-1)return minimumSale;
        double total = 0;
        for(Map.Entry<MachineDrop,Long> entry : storage.entrySet()){
            total = entry.getKey().getPrice()*entry.getValue();
        }
        return minimumSale=total;
    }

    public Map<MachineDrop, Long> getStorage() {
        return storage;
    }

    public void remove(MachineDrop drop, long amount){
        long value = storage.getOrDefault(drop,0L);
        minimumSale=-1;
        if(value<=amount){
            storage.remove(drop);
        }else {
            storage.put(drop,value-amount);
        }
        reload();
        machinePlaced.getPanel().reload();
    }

    private class StorageInventory {

        UUID owner;
        boolean reloading;
        RoidInventory roidInventory;
        int increment;

        public StorageInventory(UUID owner) {
            this.owner = owner;
            this.increment = Bukkit.getPlayer(owner).isOp()?50:0;
            this.roidInventory = RoidInventory.create(RoidMachine.getInstance(),"§8Armazem",3*9);
            this.roidInventory.onClick((action)->{
                action.event.setCancelled(true);
            });
            this.roidInventory.onOpen((e)->{
                build();
            });
            reload();
        }

        public void reload(){
            reloading=true;
            build();
        }

        private void build(){
            if(!reloading||roidInventory.getInventory().getViewers().size()==0)return;
            this.roidInventory.clear();
            RoidSlot backSlot = this.roidInventory.getSlot(3,1);
            backSlot.setDisplay(new ItemStack(Material.ARROW));
            backSlot.onClick((action)->{
                action.player.openInventory(machinePlaced.getPanel().getRoidInventory().getInventory());
            });
            RoidSlot statusSlot = this.roidInventory.getSlot(3,5);
            statusSlot.setDisplay(new RoidItemStack(Material.INK_SACK,1,enable?10:8).displayName(enable?"§aAtivado":"§cDesativado").build());
            statusSlot.onClick((action)->{
                setEnable(!enable);
                MachineStorage.this.reload();
            });
            int x = 2;
            for(Map.Entry<MachineDrop,Long> drop : storage.entrySet()){
                ItemStack itemStack = new ItemStack(drop.getKey().getMaterial(),1,drop.getKey().getData());
                ItemMeta meta = itemStack.getItemMeta();
                meta.setLore(Arrays.asList(
                        "",
                        "§7Quantidade: §f"+drop.getValue(),
                        "§7Total: §f"+ Formatter.price(drop.getKey().getTotal(drop.getValue(),increment)),
                        ""
                ));
                itemStack.setItemMeta(meta);
                RoidSlot slot = this.roidInventory.getSlot(2,x++);
                slot.setDisplay(itemStack);
                slot.onClick((action)->{
                    action.event.setCancelled(true);
                    if(drop.getValue()==0||!drop.getKey().defineInSelling())return;
                    action.player.sendMessage("§aSelling");
                    remove(drop.getKey(),drop.getValue());
                    drop.getKey().setSelling(false);
                });
            }
            reloading=false;
        }
    }
}
