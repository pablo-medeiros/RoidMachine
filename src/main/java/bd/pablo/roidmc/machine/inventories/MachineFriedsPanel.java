package bd.pablo.roidmc.machine.inventories;

import bd.pablo.roidmc.machine.RoidMachine;
import bd.pablo.roidmc.machine.api.MachinePlaced;
import bd.pablo.roidmc.machine.api.MachinePlayer;
import bd.pablo.roidmc.machine.util.Formatter;
import com.roidmc.core.api.inventory.RoidInventory;
import com.roidmc.core.api.item.RoidItemStack;
import com.roidmc.core.api.message.RoidMessageService;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MachineFriedsPanel {

    protected MachinePlaced machinePlaced;
    private RoidInventory roidInventory;
    private List<MachineFriedManagerPanel> friedManagerPanels = new ArrayList<>();
    private boolean reloading;

    public MachineFriedsPanel(MachinePlaced machinePlaced) {
        this.machinePlaced = machinePlaced;
    }

    private void create(){
        if(this.roidInventory!=null){
            reload();
            return;
        }
        this.roidInventory = RoidInventory.create(RoidMachine.getInstance(),"§8Gerenciador",6*9,false);
        roidInventory.noInteract();
        reload();
        this.roidInventory.onOpen((e)->{
            build();
        });
    }
    public void open(MachinePlayer player){
        if(player.getOfflinePlayer().getPlayer()==null)return;
        create();
        player.getOfflinePlayer().getPlayer().openInventory(roidInventory.getInventory());
    }
    public void reload(){
        reloading=true;
        build();
    }

    private void build(){
        if(!reloading||roidInventory.getInventory().getViewers().size()==0)return;
        getRoidInventory().clear();
        roidInventory.getSlot(6,1).setDisplay(new RoidItemStack(Material.ARROW).displayName("§cVoltar").build()).onClick((e)->{
            e.player.openInventory(machinePlaced.getPanel().getRoidInventory().getInventory());
        });
        roidInventory.getSlot(6,5).setDisplay(new RoidItemStack(Material.NETHER_STAR).displayName("§aAdicionar jogador").build()).onClick((e)->{
            e.player.closeInventory();
            RoidMessageService.inst.find("machine.add_friend",true).send(e.player);
        });
        roidInventory.fillHalf((slot,index)->{
            MachinePlayer player = machinePlaced.getMachinePlayers().get(index);
            slot.setDisplay(new RoidItemStack(Material.SKULL_ITEM,1,3)
                    .displayName(String.format("§e%s",player.getName()))
                    .lore(
                            "§7",
                            "§7Remover usuario: §fBotão direito",
                            "§7Acessos do usuario: §fBotão esquerdo",
                            "§7"
                    )
                    .owner(player.getName()).build());
            slot.onClick(action->{
//                if(!action.player.getUniqueId().toString().equals(player.getUniqueIdString())){
                    if(action.key.isRightClick()){
                        if(player.isTrusted()&&!machinePlaced.getTrusted().getUniqueIdString().equals(action.player.getUniqueId().toString()))return;
                        // Abrir menu de confirmação
                    }else if(action.key.isLeftClick()){
                        MachineFriedManagerPanel managerPanel = null;
                        for(MachineFriedManagerPanel panel : friedManagerPanels){
                            if(panel.managingPlayer.getUniqueIdString().equals(player.getUniqueIdString())){
                                managerPanel = panel;
                                break;
                            }
                        }
                        if(managerPanel==null){
                            managerPanel = new MachineFriedManagerPanel(player,this);
                            friedManagerPanels.add(managerPanel);
                        }
                        managerPanel.open(action.player);
                    }
//                }
            });
        },machinePlaced.getMachinePlayers().size());
        reloading=false;
    }


    public RoidInventory getRoidInventory() {
        if(roidInventory==null)create();
        return roidInventory;
    }
}
