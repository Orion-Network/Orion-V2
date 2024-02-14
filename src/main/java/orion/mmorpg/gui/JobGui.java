package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.items.sample.BasicGuiItem;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.jobs.AbstractJob;
import orion.mmorpg.players.jobs.JobType;
import orion.mmorpg.utils.Rarity;

import java.util.ArrayList;
import java.util.List;

public class JobGui extends ChestGui {
    Player player;
    OrionPlayer orionPlayer;

    public JobGui(Player player, OrionPlayer orionPlayer) {
        super(3, "Menu des métiers");
        this.player = player;
        this.orionPlayer = orionPlayer;
        this.setOnGlobalClick(event -> event.setCancelled(true));
        this.addPane(getJobPane());
    }

    protected OutlinePane getJobPane() {
        OutlinePane pane = new OutlinePane(1, 1, 7, 1);
        orionPlayer.getJobs().getJobs().forEach((jobType, job) -> {
            pane.addItem(new GuiItem(new JobItem(jobType, job).generateItem(null, 1)));
        });
        return pane;
    }

    private static class JobItem extends BasicGuiItem {
        JobType jobType;
        AbstractJob job;

        public JobItem(JobType jobType, AbstractJob job) {
            super(jobType.getMaterial(), jobType.name(), "JOB_ITEM", 0);
            this.jobType = jobType;
            this.job = job;
        }

        @Override
        public String generateDisplayName() {
            return ChatColor.YELLOW + jobType.getIcon() + " " + jobType.name();
        }

        @Override
        public List<String> generateLore(List<String> l) {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Niveau : " + job.getLevel());
            lore.add(ChatColor.GRAY + "Expérience : " + job.getExperience());
            lore.add(ChatColor.GRAY + "Expérience nécessaire : " + job.neededExperienceToLevelup());

            return lore;
        }
    }
}
