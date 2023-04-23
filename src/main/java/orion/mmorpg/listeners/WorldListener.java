package orion.mmorpg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.BreakingRessourceEvent;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicTool;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;

public class WorldListener implements Listener {
    Mmorpg plugin;

    public WorldListener(Mmorpg plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        if(plugin.getRessourceManager().isRessource(event.getBlock().getLocation())) {
            event.setCancelled(true);

            OrionPlayer orionPlayer = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
            if(orionPlayer != null) {
                event.getPlayer().getInventory().getItemInMainHand();
                if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                    Utils.sendErrorMessage(event.getPlayer(), "Vous devez avoir un outil en main pour casser cette ressource");
                    return;
                }
                BasicTool tool = (BasicTool) ItemList.getItemFromItemstack(event.getPlayer().getInventory().getItemInMainHand());
                if(tool == null) {
                    Utils.sendErrorMessage(event.getPlayer(), "Vous devez avoir un outil en main pour casser cette ressource");
                    return;
                }
                Bukkit.getPluginManager().callEvent(new BreakingRessourceEvent(tool, event.getPlayer(), orionPlayer, plugin.getRessourceManager().getRessource(event.getBlock().getLocation())));
            }
        }
    }

    @EventHandler
    public void onPlayerBreakRessource(BreakingRessourceEvent event) {
        if(!event.getRessource().getToolType().equals(event.getTool().getToolType())) {
            Utils.sendErrorMessage(event.getPlayer(), "Vous ne pouvez pas casser cette ressource avec cet outil");
        } else if(event.getTool().getToolPower() < event.getRessource().getToolPower()) {
            Utils.sendErrorMessage(event.getPlayer(), "Vous n'avez pas assez de puissance pour casser cette ressource");
        } else if(event.getOrionPlayer().getJobs().getJob(event.getRessource().getJobType()).getLevel() < event.getRessource().getJobLevel()) {
          Utils.sendInfoMessage(event.getPlayer(), "Vous n'avez pas le niveau requis pour casser cette ressource");
        } else if(plugin.getRessourceManager().breakRessource(event.getOrionPlayer(), event.getRessource().getLocation())) {
            event.getPlayer().getInventory().addItem(event.getRessource().getDrop());
            event.getRessource().getLocation().getBlock().setType(Material.AIR);
            Utils.sendInfoMessage(event.getPlayer(), "Vous avez cassé la ressource");
        }
    }
}
