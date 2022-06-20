package orion.mmorpg.players;

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
            stats.setStrength(stats.getStrength() + item.getSTATS().getStrength());
            stats.setCritical_chance(stats.getCritical_chance() + item.getSTATS().getCritical_chance());
            stats.setCritical_damage(stats.getCritical_damage() + item.getSTATS().getCritical_damage());
            stats.setAttack_speed(stats.getAttack_speed() + item.getSTATS().getAttack_speed());
            stats.setVitality(stats.getVitality() + item.getSTATS().getVitality());
            stats.setIntel(stats.getIntel() + item.getSTATS().getIntel());
            stats.setDefence(stats.getDefence() + item.getSTATS().getDefence());
            stats.setSpeed(stats.getSpeed() + item.getSTATS().getSpeed());
            stats.setLuck(stats.getLuck() + item.getSTATS().getLuck());
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
