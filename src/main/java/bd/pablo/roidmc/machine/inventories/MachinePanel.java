package bd.pablo.roidmc.machine.inventories;

import bd.pablo.roidmc.machine.RoidMachine;
import bd.pablo.roidmc.machine.api.MachineDrop;
import bd.pablo.roidmc.machine.api.MachinePermission;
import bd.pablo.roidmc.machine.api.MachinePlaced;
import bd.pablo.roidmc.machine.api.MachinePlayer;
import bd.pablo.roidmc.machine.util.Formatter;
import com.roidmc.core.api.inventory.RoidInventory;
import com.roidmc.core.api.inventory.RoidSlot;
import com.roidmc.core.api.item.RoidItemStack;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MachinePanel {

    private MachinePlaced machinePlaced;
    private RoidInventory roidInventory;
    private boolean reloading;
    private int lastEnable = -1;
    private int lastFriendSize = -1;
    private long lastFuel = -1;
    private double lastStorageMinimumSale = -1;

    public MachinePanel(MachinePlaced machinePlaced) {
        this.machinePlaced = machinePlaced;
    }

    private void create(){
        this.roidInventory = RoidInventory.create(RoidMachine.getInstance(),"§8Maquina",3*9,false);
        reload();
        roidInventory.getSlot(2,2).onClick((e)->{
            if(machinePlaced.isEnable()){
               machinePlaced.setEnable(false);
            }else if(machinePlaced.getFuel()>0){
                machinePlaced.setEnable(true);
            }
        });
        roidInventory.getSlot(2,4).onClick((e)->{
            // Abrir gerenciador de amigos
            MachinePlayer machinePlayer = machinePlaced.getPlayer(e.player);
            if(machinePlayer==null||!machinePlayer.hasPermission(MachinePermission.MANAGER_USERS))return;
            machinePlaced.getFriedsPanel().open(machinePlaced.getPlayer(e.player));
        });
        roidInventory.getSlot(2,6).onClick((e)->{
            if(e.key== ClickType.LEFT){
                // Pega o combustivel na mão, se tiver shit, todos na mão
            }else if(e.key==ClickType.RIGHT){
                // Pega o combustivel do inventario
            }
        });
        roidInventory.getSlot(2,8).onClick((e)->{
            machinePlaced.getStorage().open(machinePlaced.getPlayer(e.player));
        });
        this.roidInventory.onOpen((e)->{
            build();
        });
    }

    public void reload(){
        reloading=true;
        build();
    }

    private void build(){
        if(!reloading||roidInventory==null||roidInventory.getInventory().getViewers().size()==0)return;
        if(lastEnable==-1||(lastEnable==1) != machinePlaced.isEnable()){
            this.roidInventory.getSlot(2,2).setDisplay(new RoidItemStack(Material.INK_SACK,1,machinePlaced.isEnable()?10:8).displayName("§eStatus").lore(
                    "",
                    (machinePlaced.isEnable()?"§aLigada":"§cDesligada"),
                    "",
                    "§7Status se a maquina esta ligada ou",
                    "§7não."
            ).build());
            lastEnable = machinePlaced.isEnable()?1:0;
        }
        if(lastFriendSize==-1||lastFriendSize!=machinePlaced.getMachinePlayers().size()) {
            this.roidInventory.getSlot(2, 4).setDisplay(new RoidItemStack(Material.SKULL_ITEM, 1, 3).owner(machinePlaced.getTrusted().getName()).displayName("§eAmigos").lore(
                    "",
                    "§aAmigos: §f" + (machinePlaced.getMachinePlayers().size() - 1),
                    "",
                    "§7Gerenciar acesso de outros",
                    "§7jogadores em sua maquina."
            ).build());
            lastFriendSize = machinePlaced.getMachinePlayers().size();
        }
        if(lastFuel==-1||lastFuel!=machinePlaced.getFuel()) {
            this.roidInventory.getSlot(2, 6).setDisplay(new RoidItemStack(Material.COAL).displayName("§eCombustiveis").lore(
                    "",
                    "§aLitros: §f" + Formatter.fuel(machinePlaced.getFuel()),
                    "",
                    "§7Para abastecer a maquina clique",
                    "§7com §fEsquerdo §7para usar o",
                    "§7combustivel da sua mão, ou ",
                    "§fDireito §7para abastecer com",
                    "§7todos os combustiveis do seu",
                    "§7inventario"
            ).build());
            lastFuel=machinePlaced.getFuel();
        }
        if(lastStorageMinimumSale==-1||lastStorageMinimumSale!=machinePlaced.getStorage().getMinimumSale()) {
            this.roidInventory.getSlot(2, 8).setDisplay(new RoidItemStack(Material.CHEST).displayName("§eArmazem").lore(
                    "",
                    "§aPeso: §f" + Formatter.weight(machinePlaced.getStorage().getStorage().entrySet().stream().map(entry -> new Map.Entry<Material, Long>() {
                        @Override
                        public Material getKey() {
                            return entry.getKey().getMaterial();
                        }

                        @Override
                        public Long getValue() {
                            return entry.getValue();
                        }

                        @Override
                        public Long setValue(Long value) {
                            return entry.getValue();
                        }
                    }).collect(Collectors.toList())),
                    "§aPreço minimo: §f" + Formatter.price(machinePlaced.getStorage().getMinimumSale()),
                    "",
                    "§7Drops da sua maquina, o preço",
                    "§7minimo é o valor minimo vendido,",
                    "§7caso tenha um booster ou vip,",
                    "§7o valor ficará mais alto."

            ).build());
            this.lastStorageMinimumSale = machinePlaced.getStorage().getMinimumSale();
        }
        reloading=false;
    }


    public RoidInventory getRoidInventory() {
        if(roidInventory==null)create();
        return roidInventory;
    }
}
