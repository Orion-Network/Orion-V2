package orion.mmorpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.jobs.JobType;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataCommand implements TabExecutor {
    Mmorpg mmorpg;

    public DataCommand(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
        } else {
            if (args[0].equalsIgnoreCase("player")) {
                if (sender instanceof Player) {
                    Player p = Bukkit.getPlayer(args[1]);
                    OrionPlayer data = mmorpg.getPlayerManager().getPlayer(p.getUniqueId());
                    StringBuilder builder = new StringBuilder();
                    builder.append(ChatColor.GRAY + "Name : " + ChatColor.YELLOW + p.getName() + "\n");
                    builder.append(ChatColor.GRAY + "UUID : " + ChatColor.YELLOW + p.getUniqueId() + "\n");
                    builder.append(ChatColor.GRAY + "Mode : " + ChatColor.YELLOW + data.getPlayerMode().name() + "\n");
                    builder.append(ChatColor.GRAY + "Health : " + ChatColor.YELLOW + data.getHealth() + "\n");
                    builder.append(ChatColor.GRAY + "Mana : " + ChatColor.YELLOW + data.getMana() + "\n");
                    builder.append(ChatColor.GRAY + "Support : " + ChatColor.YELLOW + (data.getSupport() == null ? "Aucun" : data.getSupport().getName()) + "\n");
                    builder.append(ChatColor.GRAY + "Race : " + ChatColor.YELLOW + data.getRace().getName() + "\n");
                    builder.append(data.getStats().modifiersAsLore()).append("\n");
                    for (String stat : data.getStats().statsAsLore())
                        builder.append(stat + "\n");
                    builder.append(data.getPortalStones() + "\n");
                    for (BasicArmorItem item : data.getEquipements().getEquipments().values())
                        if (item != null)
                            builder.append(item.getNAME());
                        else
                            builder.append(ChatColor.GRAY + "Vide, ");
                    builder.append("\n" + data.getKnownSpells().keySet() + "\n");
                    builder.append("\n" + "Smithing : " + data.getJobs().getJob(JobType.SMITHING).getLevel() + " " + data.getJobs().getJob(JobType.SMITHING).getExperience());
                    builder.append("\n" + "Mining : " + data.getJobs().getJob(JobType.MINING).getLevel() + " " + data.getJobs().getJob(JobType.MINING).getExperience());
                    for (Support s : data.getSupportMap().keySet())
                        builder.append(s.getName());
                    sender.sendMessage(builder.toString());
                }
            } else if (args[0].equalsIgnoreCase("item")) {
                if (sender instanceof Player) {
                    ItemMeta meta = ((Player) sender).getInventory().getItemInMainHand().getItemMeta();
                    String internal_id = meta.getPersistentDataContainer().get(Namespaces.internal_id, PersistentDataType.STRING);
                    String type = meta.getPersistentDataContainer().get(Namespaces.type, PersistentDataType.STRING);
                    Stats stats = Mmorpg.getOrionGson().getGSON().fromJson(meta.getPersistentDataContainer().get(Namespaces.stats, PersistentDataType.STRING), Stats.class);
                    float damage = 0;
                    if (meta.getPersistentDataContainer().has(Namespaces.damage, PersistentDataType.FLOAT))
                        damage = meta.getPersistentDataContainer().get(Namespaces.damage, PersistentDataType.FLOAT);

                    StringBuilder builder = new StringBuilder();
                    builder.append(ChatColor.GRAY + "INTERNAL_ID: " + ChatColor.YELLOW + internal_id + "\n");
                    builder.append(ChatColor.GRAY + "TYPE: " + ChatColor.YELLOW + type + "\n");
                    if (damage != 0)
                        builder.append(ChatColor.GRAY + "DAMAGE: " + ChatColor.RED + damage + "\n");
                    if (stats != null) {
                        builder.append(String.join("\n", stats.statsAsLore()));
                        builder.append("\n");
                    }
                    builder.append(meta.getPersistentDataContainer().get(Namespaces.type, PersistentDataType.STRING));
                    builder.append(ChatColor.GRAY + "Data: " + ChatColor.LIGHT_PURPLE + meta.getPersistentDataContainer().getKeys());

                    sender.sendMessage(builder.toString());
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<String>();
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("player")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    list.add(p.getName());
                }
                return list;
            } else if (args[0].equalsIgnoreCase("item"))
                return Arrays.asList("hand", "0");
        }
        return Arrays.asList("item", "player");
    }
}
