package bd.pablo.roidmc.machine.api;

import bd.pablo.roidmc.machine.RoidMachine;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public abstract class MachineTask {

    private int delay;

    public void runTask(){
        if(!getMachinePlaced().isEnable())return;
        if(this.getMachinePlaced().getFuel()<1){
            getMachinePlaced().setEnable(false);
            return;
        }
        this.delay--;
        this.getMachinePlaced().setFuel(this.getMachinePlaced().getFuel()-1);
        if(this.delay<=0){
            onDrop();
            this.delay=getMachinePlaced().getMachine().getDelay();
        }
    }

    public void drop(Collection<ItemStack> itemStacks){
        new BukkitRunnable(){
            @Override
            public void run() {
                itemStacks.forEach(item->{
                    getMachinePlaced().getLocation().getWorld().dropItemNaturally(getMachinePlaced().getLocation().clone().add(0.5,1,0.5),item);
                });
            }
        }.runTask(RoidMachine.getInstance());
    }

    public abstract void onDrop();

    public abstract MachinePlaced getMachinePlaced();


}
