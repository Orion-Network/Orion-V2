package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import org.bukkit.entity.Player;
import orion.mmorpg.players.OrionPlayer;

public class AdminGui extends ChestGui {
    Player player;
    OrionPlayer orionPlayer;

    public AdminGui(Player player, OrionPlayer orionPlayer) {
        super(6, "Menu Admin");
        this.player = player;
        this.orionPlayer = orionPlayer;
    }
}
