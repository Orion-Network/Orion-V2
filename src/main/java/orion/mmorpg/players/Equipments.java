package orion.mmorpg.players;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Stats;

import java.util.HashMap;

public class Equipments {
    private HashMap<EquipmentSlot, BasicArmorItem> equipments = new HashMap<>();


    public boolean addEquipment(BasicArmorItem item) {
        EquipmentSlot slot = isSlotFree(item.getARMORTYPE());
        if(slot == null)
            return false;
        equipments.put(slot, item);
        return true;
    }

    public boolean removeEquipment(EquipmentSlot slot) {
        if(!equipments.containsKey(slot))
            return false;
        equipments.remove(slot);
        return true;
    }

    public BasicArmorItem getEquipmentFromSlot(EquipmentSlot slot) {
        if(equipments.containsKey(slot))
            return equipments.get(slot);
        return null;
    }

    public Stats getEquipmentStats() {
        Stats stats = new Stats.Builder().build();
        equipments.values().forEach(item -> {
            stats.setStrength(stats.getStrength() + item.getBASE_STATS().getStrength());
            stats.setCritical_chance(stats.getCritical_chance() + item.getBASE_STATS().getCritical_chance());
            stats.setCritical_damage(stats.getCritical_damage() + item.getBASE_STATS().getCritical_damage());
            stats.setAttack_speed(stats.getAttack_speed() + item.getBASE_STATS().getAttack_speed());
            stats.setVitality(stats.getVitality() + item.getBASE_STATS().getVitality());
            stats.setIntel(stats.getIntel() + item.getBASE_STATS().getIntel());
            stats.setDefence(stats.getDefence() + item.getBASE_STATS().getDefence());
            stats.setSpeed(stats.getSpeed() + item.getBASE_STATS().getSpeed());
            stats.setLuck(stats.getLuck() + item.getBASE_STATS().getLuck());
        });
        return stats;
    }

    private EquipmentSlot isSlotFree(BasicArmorItem.ArmorType armorType) {
        switch (armorType) {
            case HELMET: {
                if(!equipments.containsKey(EquipmentSlot.HELMET))
                    return EquipmentSlot.HELMET;
                break;
            }
            case CHESTPLATE: {
                if(!equipments.containsKey(EquipmentSlot.CHESTPLATE))
                    return EquipmentSlot.CHESTPLATE;
                break;
            }
            case LEGGINGS: {
                if(!equipments.containsKey(EquipmentSlot.LEGGINGS))
                    return EquipmentSlot.LEGGINGS;
                break;
            }
            case BOOTS: {
                if(!equipments.containsKey(EquipmentSlot.BOOTS))
                    return EquipmentSlot.BOOTS;
                break;
            }
            case BELT: {
                if(!equipments.containsKey(EquipmentSlot.BELT))
                    return EquipmentSlot.BELT;
                break;
            }
            case RING: {
                if(!equipments.containsKey(EquipmentSlot.LEFT_RING))
                    return EquipmentSlot.LEFT_RING;
                else if(!equipments.containsKey(EquipmentSlot.RIGHT_RING))
                    return EquipmentSlot.RIGHT_RING;
                break;
            }
        }
        return null;
    }

    public StaticPane getEquipmentPane(Gui gui) {
        StaticPane pane = new StaticPane(0,0,3,4);

        if(equipments.containsKey(EquipmentSlot.HELMET))
            pane.addItem(new GuiItem(equipments.get(EquipmentSlot.HELMET).generateItem(null, 1)), 0, 0);
        else
            pane.addItem(new GuiItem(ItemList.HELMET_PLACEHOLDER.getProcessedItem(0)), 0, 0);

        if(equipments.containsKey(EquipmentSlot.CHESTPLATE))
            pane.addItem(new GuiItem(equipments.get(EquipmentSlot.CHESTPLATE).generateItem(null, 1)), 1, 0);
        else
            pane.addItem(new GuiItem(ItemList.CHESTPLATE_PLACEHOLDER.getProcessedItem(0)), 1, 0);

        if(equipments.containsKey(EquipmentSlot.LEGGINGS))
            pane.addItem(new GuiItem(equipments.get(EquipmentSlot.LEGGINGS).generateItem(null, 1)), 2, 0);
        else
            pane.addItem(new GuiItem(ItemList.LEGGINGS_PLACEHOLDER.getProcessedItem(0)), 2, 0);

        if(equipments.containsKey(EquipmentSlot.BOOTS))
            pane.addItem(new GuiItem(equipments.get(EquipmentSlot.BOOTS).generateItem(null, 1)), 3, 0);
        else
            pane.addItem(new GuiItem(ItemList.BOOTS_PLACEHOLDER.getProcessedItem(0)), 3, 0);

        if(equipments.containsKey(EquipmentSlot.BELT))
            pane.addItem(new GuiItem(equipments.get(EquipmentSlot.BELT).generateItem(null, 1)), 2, 2);
        else
            pane.addItem(new GuiItem(ItemList.BELT_PLACEHOLDER.getProcessedItem(0)), 2, 2);

        if(equipments.containsKey(EquipmentSlot.LEFT_RING))
            pane.addItem(new GuiItem(equipments.get(EquipmentSlot.LEFT_RING).generateItem(null, 1)), 0, 1);
        else
            pane.addItem(new GuiItem(ItemList.LEFT_BUTTON_PLACEHOLDER.getProcessedItem(0)), 0, 1);

        if(equipments.containsKey(EquipmentSlot.RIGHT_RING))
            pane.addItem(new GuiItem(equipments.get(EquipmentSlot.RIGHT_RING).generateItem(null, 1)), 0, 2);
        else
            pane.addItem(new GuiItem(ItemList.RIGHT_BUTTON_PLACEHOLDER.getProcessedItem(0)), 0, 2);

        return pane;
    }

    public HashMap<EquipmentSlot, BasicArmorItem> getEquipments() {
        return equipments;
    }

    //TODO Implement gui in class

    public enum EquipmentSlot {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS,
        LEFT_RING,
        RIGHT_RING,
        BELT
    }
}
