package bd.pablo.roidmc.machine;

import com.roidmc.core.RoidCore;
import com.roidmc.core.RoidPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class RoidMachine extends JavaPlugin implements RoidPlugin {

    {
        RoidCore.registerPlugin(this);
    }

    @Override
    public void onInit() {
        System.out.println("Init Machine");
    }

    @Override
    public void onStart() {
        System.out.println("Start Machine");
    }

    @Override
    public void onShutdown() {
        System.out.println("Stop Machine");
    }

    @Override
    public JavaPlugin getPlugin() {
        return this;
    }

    @Override
    public File getFile() {
        return super.getFile();
    }
}
