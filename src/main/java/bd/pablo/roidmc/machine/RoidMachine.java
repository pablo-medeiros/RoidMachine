package bd.pablo.roidmc.machine;

import bd.pablo.roidmc.machine.api.Machine;
import bd.pablo.roidmc.machine.api.MachineDrop;
import bd.pablo.roidmc.machine.api.MachinePlacedServices;
import bd.pablo.roidmc.machine.api.MachineServices;
import bd.pablo.roidmc.machine.tasks.MachineTimerTask;
import com.roidmc.core.DevMode;
import com.roidmc.core.RoidCore;
import com.roidmc.core.RoidPlugin;
import com.roidmc.core.api.RoidService;
import com.roidmc.core.api.command.events.RoidCommandPreprocessEvent;
import com.roidmc.core.api.command.events.RoidCommandReceivedEvent;
import com.roidmc.core.api.hologram.RoidHologramsService;
import com.roidmc.core.api.inventory.nbt.NBTItem;
import com.roidmc.core.api.inventory.nbt.NBTTagInt;
import com.roidmc.core.api.inventory.nbt.NBTTagList;
import com.roidmc.core.api.item.RoidItem;
import com.roidmc.core.api.item.RoidItemsService;
import com.roidmc.core.api.message.RoidMessageService;
import com.roidmc.core.api.reset.RoidReset;
import com.roidmc.core.util.Debug;
import com.roidmc.core.util.Progress;
import com.roidmc.core.util.Translate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;

public final class RoidMachine extends JavaPlugin implements RoidPlugin {

    private static final Machine machine = new Machine("test");

    {
        RoidCore.registerPlugin(this);
        machine.setHolograms(Arrays.asList("§eMaquina Test","","§eTestando"));
        machine.setItemStack(new ItemStack(Material.GOLD_BLOCK));
        machine.setDrops(Collections.singletonList(new MachineDrop(Material.GOLD_INGOT, 20)));
        machine.setDelay(5);
    }

    @Override
    public void onInit() {
        RoidCore.getInstance().registerService(MachineServices.inst);
        RoidCore.getInstance().registerService(MachinePlacedServices.inst);
        subscribe();
        MachineServices.inst.register(machine);
        System.out.println("Init Machine");
        Translate.addMessages(this,"messages.lang");
    }

    @Override
    public void onStart() {
        System.out.println("Start Machine");
        Debug.enable=true;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this,this);
        new MachineTimerTask().start();
    }
    @Override
    public void onShutdown() {
        System.out.println("Stop Machine");
        unsubscribe();
        MachinePlacedServices.inst.findAll().forEach(machinePlaced -> {
            machinePlaced.destroy();
            MachinePlacedServices.inst.unRegister(machinePlaced.getLocation().toString());
        });
    }

    @Override
    public JavaPlugin getPlugin() {
        return this;
    }

    @Override
    public File getFile() {
        return super.getFile();
    }

    @Override
    public RoidReset reset() {
        return new RoidReset() {
            @Override
            public int maxInteraction(boolean b) {
                return 500;
            }

            @Override
            public void make(File file, Progress progress) {
                for(int i = 0; i < 500; i++){
                    try {
                        File file1 = new File(file,String.format("teste-%d.txt",i));
                        try(PrintWriter printWriter = new PrintWriter(new FileWriter(file1))){
                            printWriter.println("Taciti quaerendum pharetra urna ultrices. Suavitate rutrum porro definiebas nullam nisl. Sollicitudin prodesset offendit et hendrerit quaerendum. Fabellas mutat sociis placerat cetero fames veri erat auctor epicuri. Magnis verear a aenean discere tota deserunt duis melius menandri. Utroque interesset consequat habitasse noluisse. Tantas usu scelerisque tantas purus nullam vim mel delicata.");
                        }catch (Exception e){}
                        progress.next();
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void rollback(File file, Progress progress) {
                for(int i = 0; i < 500; i++){
                    try {
                        progress.next();
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private static RoidMachine instance;

    public static RoidMachine getInstance() {
        return instance;
    }

    {
        instance = this;
        subscribe();
    }
}
