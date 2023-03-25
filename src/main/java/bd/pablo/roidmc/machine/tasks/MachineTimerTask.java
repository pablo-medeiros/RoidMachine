package bd.pablo.roidmc.machine.tasks;

import bd.pablo.roidmc.machine.RoidMachine;
import bd.pablo.roidmc.machine.api.MachinePlaced;
import bd.pablo.roidmc.machine.api.MachinePlacedServices;
import org.bukkit.scheduler.BukkitRunnable;

public class MachineTimerTask extends BukkitRunnable {

    @Override
    public void run() {
        for(MachinePlaced placed : MachinePlacedServices.inst.findAll()){
            placed.runTask();
        }
    }

    public void start(){
        runTaskTimerAsynchronously(RoidMachine.getInstance(),20,20);
    }
}
