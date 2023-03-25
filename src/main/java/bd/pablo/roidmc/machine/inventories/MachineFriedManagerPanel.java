package bd.pablo.roidmc.machine.inventories;

import bd.pablo.roidmc.machine.RoidMachine;
import bd.pablo.roidmc.machine.api.MachinePermission;
import bd.pablo.roidmc.machine.api.MachinePlaced;
import bd.pablo.roidmc.machine.api.MachinePlayer;
import com.roidmc.core.api.inventory.RoidInventory;
import com.roidmc.core.api.item.RoidItemStack;
import com.roidmc.core.api.message.RoidMessageService;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MachineFriedManagerPanel {

    protected MachinePlayer managingPlayer;
    private MachineFriedsPanel friedsPanel;
    private RoidInventory roidInventory;
    private boolean reloading;

    public MachineFriedManagerPanel(MachinePlayer managingPlayer, MachineFriedsPanel friedsPanel) {
        this.managingPlayer = managingPlayer;
        this.friedsPanel = friedsPanel;
    }

    private void create(){
        if(this.roidInventory!=null){
            reload();
            return;
        }
        this.roidInventory = RoidInventory.create(RoidMachine.getInstance(),"§8Gerenciando jogador: "+managingPlayer.getName(),6*9,false);
        roidInventory.noInteract();
        reload();
        this.roidInventory.onOpen((e)->{
            build();
        });
    }
    public void open(Player player){
        create();
        player.openInventory(roidInventory.getInventory());
    }
    public void reload(){
        reloading=true;
        build();
    }

    private void build(){
        if(!reloading||roidInventory.getInventory().getViewers().size()==0)return;
        getRoidInventory().clear();
        roidInventory.getSlot(6,1).setDisplay(new RoidItemStack(Material.ARROW).displayName("§cVoltar").build()).onClick((e)->{
            e.player.openInventory(friedsPanel.getRoidInventory().getInventory());
        });
        roidInventory.getSlot(6,5).setDisplay(new RoidItemStack(Material.EMERALD).displayName(managingPlayer.isTrusted()?"§cNão é confiavél":"§aConfiavél").build()).onClick((e)->{
            if(friedsPanel.machinePlaced.getPlayer(e.player).isTrusted()) {
                managingPlayer.setTrusted(!managingPlayer.isTrusted());
                reload();
            }
        });
        roidInventory.fillHalf((slot,index)->{
            MachinePermission permission = MachinePermission.values()[index];
            boolean has = managingPlayer.hasPermission(permission);
            slot.setDisplay(new RoidItemStack(has?Material.ENCHANTED_BOOK:Material.BOOK).displayName((has?"§a":"§c")+permission.getDisplayName()).loreAutoBreak("§7"+permission.getInfo(),32).build()).onClick(e->{
                if(has){
                    managingPlayer.removePermission(permission);
                }else {
                    managingPlayer.addPermission(permission);
                }
                reload();
            });
        }, MachinePermission.values().length);
        reloading=false;
    }


    public RoidInventory getRoidInventory() {
        if(roidInventory==null)create();
        return roidInventory;
    }
}
