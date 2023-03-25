package bd.pablo.roidmc.machine.commands;

import bd.pablo.roidmc.machine.api.MachineServices;
import com.roidmc.core.api.command.RoidCommand;
import com.roidmc.core.api.command.RoidCommandExecutor;
import com.roidmc.core.api.command.RoidCommandGroup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RoidCommandGroup(name="maquinas")
public class MachineCommand implements RoidCommandExecutor {

    @RoidCommand(name="admin", acceptConsole = false)
    public void onAdmin(CommandSender sender, String[] args){
        Player player = (Player)sender;
        player.getInventory().addItem(MachineServices.inst.find("test").create(1));

    }

}
