package orion.mmorpg.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;

public class Knowledge {
    private int knowledge;

    public Knowledge(int knowledge) {
        this.knowledge = knowledge;
    }

    public KnowledgeState getKnowledgeState() {
        if (knowledge > 0 && knowledge < 100) {
            return KnowledgeState.LEARN;
        } else if (knowledge >= 100 && knowledge < 1000) {
            return KnowledgeState.EXPERIENCE;
        } else if (knowledge >= 1000) {
            return KnowledgeState.MASTERY;
        }
        return KnowledgeState.UNKNOWN;
    }

    public int getNextMilestone() {
        if (knowledge < 100) {
            return 100;
        } else if (knowledge < 1000) {
            return 1000;
        }
        return 0;
    }

    public int getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(int knowledge) {
        this.knowledge = knowledge;
    }

    public void addKnowledge(int knowledge) {
        this.knowledge += knowledge;
    }

    public ArrayList<String> getKnowledgeLore() {
        ArrayList<String> lore = new ArrayList<String>();
        if (knowledge > 0 && knowledge < 100) {
            lore.add(ChatColor.WHITE + "Vous ne savez rien du sort.");
        } else if (knowledge >= 100 && knowledge < 1000) {
            lore.add(ChatColor.WHITE + "Vous savez un peu utiliser ce sort, ");
            lore.add(ChatColor.WHITE + "utilisez le plus pour gagner de l'expérience.");
        } else if (knowledge >= 1000) {
            lore.add(ChatColor.WHITE + "Vous connaissez parfaitement ce sort.");
        }
        lore.add(ChatColor.WHITE + "Vous avez " + ChatColor.GOLD + knowledge + " points de connaissance.");
        return lore;
    }

    public enum KnowledgeState {
        UNKNOWN, LEARN, EXPERIENCE, MASTERY;
    }
}
