package bd.pablo.roidmc.machine.api;

import bd.pablo.roidmc.machine.RoidMachine;
import bd.pablo.roidmc.machine.inventories.MachineFriedsPanel;
import bd.pablo.roidmc.machine.inventories.MachinePanel;
import bd.pablo.roidmc.machine.inventories.MachineStorage;
import com.roidmc.core.RoidPlugin;
import com.roidmc.core.api.hologram.RoidHolograms;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MachinePlaced extends MachineTask implements MetadataValue {

    private final Machine machine;
    private final Location location;
    private final RoidHolograms holograms;
    private boolean enable;
    private final List<MachinePlayer> machinePlayers;
    private MachinePlayer trusted;
    private MachinePanel panel;
    private final MachineStorage storage;
    private final MachineFriedsPanel friedsPanel;
    private long stacked = 1;
    private long fuel;

    public MachinePlaced(Machine machine, Location location, boolean enable) {
        this.machine = machine;
        this.location = location;
        this.enable = enable;
        this.holograms = new RoidHolograms(location.clone().add(0.5,0.3,0.5),String.format("machine_%s",location.toString())) {

            @Override
            public RoidPlugin getOwner() {
                return RoidMachine.getInstance();
            }

            @Override
            public String[] getLines() {
                return machine.getHolograms().toArray(new String[0]);
            }
        };
        this.machinePlayers = new ArrayList<>();
        this.storage = new MachineStorage(this);
        this.friedsPanel = new MachineFriedsPanel(this);
        this.location.getBlock().setMetadata("machine",this);
    }

    public void destroy(){
        this.location.getBlock().removeMetadata("machine",getOwningPlugin());
        this.holograms.remove();
    }

    @Override
    public void onDrop() {
        if(storage.isEnable()){
            for(MachineDrop drop : getMachine().getDrops()) {
                storage.store(drop,stacked);
            }
            if(this.panel!=null)this.panel.reload();
            storage.reload();
        }else {
            for(MachineDrop drop : getMachine().getDrops()) {
                drop(createItems(drop,stacked,null));
            }
        }
    }

    private List<ItemStack> createItems(MachineDrop drop, long amount, List<ItemStack> items){
        if(items==null)items=new ArrayList<>();
        ItemStack newItem = new ItemStack(drop.getMaterial(), 1, drop.getData());
        if(amount>Integer.MAX_VALUE){
            newItem.setAmount(Integer.MAX_VALUE);
            amount-=Integer.MAX_VALUE;
            createItems(drop,amount,items);
        }else {
            newItem.setAmount((int) amount);
        }
        items.add(newItem);
        return items;
    }

    public MachinePlaced setEnable(boolean enable) {
        this.enable = enable;
        if(this.panel!=null)this.panel.reload();
        return this;
    }

    public MachinePlaced setFuel(long fuel) {
        this.fuel = fuel;
        if(this.panel!=null)this.panel.reload();
        return this;
    }

    public void addPlayer(MachinePlayer player){
        machinePlayers.removeIf(p->p.getUniqueIdString().equals(player.getUniqueId().toString()));
        machinePlayers.add(player);
        if(player.isTrusted()&&trusted==null)this.trusted=player;
        if(this.panel!=null)this.panel.reload();
    }

    public boolean hasPlayer(OfflinePlayer player){
        return machinePlayers.stream().anyMatch(p->p.getUniqueIdString().equals(player.getUniqueId().toString()));
    }

    public boolean hasPlayer(UUID UUID){
        return machinePlayers.stream().anyMatch(p->p.getUniqueIdString().equals(UUID.toString()));
    }

    public MachinePlayer getPlayer(OfflinePlayer player){
        return machinePlayers.stream().filter(p->p.getUniqueIdString().equals(player.getUniqueId().toString())).findFirst().orElse(null);
    }

    public MachinePlayer getPlayer(UUID UUID){
        return machinePlayers.stream().filter(p->p.getUniqueIdString().equals(UUID.toString())).findFirst().orElse(null);
    }

    public boolean hasPermission(OfflinePlayer player,MachinePermission permission){
        return machinePlayers.stream().anyMatch(p->p.getUniqueIdString().equals(player.getUniqueId().toString())&&p.hasPermission(permission));
    }

    public boolean hasPermission(UUID UUID,MachinePermission permission){
        return machinePlayers.stream().anyMatch(p->p.getUniqueIdString().equals(UUID.toString())&&p.hasPermission(permission));
    }

    public MachinePlayer getTrusted() {
        return trusted;
    }

    public boolean isTrusted(OfflinePlayer player){
        return machinePlayers.stream().anyMatch(p->p.getUniqueIdString().equals(player.getUniqueId().toString())&&p.isTrusted());
    }

    public boolean isTrusted(UUID UUID){
        return machinePlayers.stream().anyMatch(p->p.getUniqueIdString().equals(UUID.toString())&&p.isTrusted());
    }

    public Machine getMachine() {
        return machine;
    }

    public Location getLocation() {
        return location;
    }

    public RoidHolograms getHolograms() {
        return holograms;
    }

    public boolean isEnable() {
        return enable;
    }

    public long getFuel() {
        return fuel;
    }

    public List<MachinePlayer> getMachinePlayers() {
        return new ArrayList<>(machinePlayers);
    }

    public MachinePanel getPanel() {
        if(panel==null)panel=new MachinePanel(this);
        return panel;
    }

    public MachineStorage getStorage() {
        return storage;
    }

    public MachineFriedsPanel getFriedsPanel() {
        return friedsPanel;
    }

    public long getStacked() {
        return stacked;
    }

    public MachinePlaced setStacked(long stacked) {
        this.stacked = stacked;
        return this;
    }

    @Override
    public Object value() {
        return this;
    }

    @Override
    public int asInt() {
        return 0;
    }

    @Override
    public float asFloat() {
        return 0;
    }

    @Override
    public double asDouble() {
        return 0;
    }

    @Override
    public long asLong() {
        return 0;
    }

    @Override
    public short asShort() {
        return 0;
    }

    @Override
    public byte asByte() {
        return 0;
    }

    @Override
    public boolean asBoolean() {
        return false;
    }

    @Override
    public String asString() {
        return null;
    }

    @Override
    public Plugin getOwningPlugin() {
        return RoidMachine.getInstance();
    }

    @Override
    public void invalidate() {

    }

    @Override
    public MachinePlaced getMachinePlaced() {
        return this;
    }
}
