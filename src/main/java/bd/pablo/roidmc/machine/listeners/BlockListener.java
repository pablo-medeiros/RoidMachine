package bd.pablo.roidmc.machine.listeners;

import bd.pablo.roidmc.machine.RoidMachine;
import bd.pablo.roidmc.machine.api.*;
import com.roidmc.core.api.listeners.RoidListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BlockListener extends RoidListener {

    @EventHandler
    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        List<MetadataValue> metadata = e.getBlock().getMetadata("machine");
        if(metadata!=null&&!metadata.isEmpty()){
            MachinePlaced machinePlaced = (MachinePlaced) metadata.get(0);
            if(e.getPlayer().hasMetadata("roid.admin")||machinePlaced.isTrusted(e.getPlayer())){
                e.setCancelled(true);
                e.getBlock().setType(Material.AIR);
                machinePlaced.destroy();
                MachinePlacedServices.inst.unRegister(e.getBlock().getLocation().toString());
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Machine machine = MachineServices.inst.find(e.getItemInHand());
        if(machine!=null){
            MachinePlaced machinePlaced = new MachinePlaced(machine,e.getBlock().getLocation(), false);
            machinePlaced.addPlayer(new MachinePlayer(e.getPlayer(),true));
            machinePlaced.getHolograms().spawn();
            machinePlaced.getStorage().store(new MachineDrop(
                    Material.STONE,50
            ),100);
            machinePlaced.setFuel(1000);
            MachinePlacedServices.inst.register(e.getBlock().getLocation().toString(),machinePlaced);
            for(Player player : Bukkit.getOnlinePlayers()){
                if(player.getName().equals(e.getPlayer().getName()))continue;
                machinePlaced.addPlayer(new MachinePlayer(player,false));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK)return;
        if(e.getItem()!=null&&(e.getItem().getType().isBlock()||e.getItem().getType().isEdible())&&e.getPlayer().isSneaking()){
            return;
        }
        List<MetadataValue> metadata = e.getClickedBlock().getMetadata("machine");
        if(metadata!=null&&!metadata.isEmpty()) {
            e.setCancelled(true);
            MachinePlaced machinePlaced = (MachinePlaced) metadata.get(0);
            e.getPlayer().openInventory(machinePlaced.getPanel().getRoidInventory().getInventory());
        }
    }
}
