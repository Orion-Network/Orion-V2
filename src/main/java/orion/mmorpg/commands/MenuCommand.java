package orion.mmorpg.commands;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.gui.MenuGui;

import java.util.List;

public class MenuCommand implements TabExecutor {
    Mmorpg mmorpg;

    public MenuCommand(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            new MenuGui((Player) sender, this.mmorpg.getPlayerManager().getPlayer(((Player) sender).getUniqueId()));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
