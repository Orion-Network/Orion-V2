package orion.mmorpg.players;

import orion.mmorpg.items.sample.BasicArmorItem;

import java.util.HashMap;

public class Equipments<T extends BasicArmorItem> {
    private HashMap<EquipmentSlot, T> equipments = new HashMap<>();


    public boolean addEquipment(T item) {
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

    private EquipmentSlot isSlotFree(BasicArmorItem.ArmorType armorType) {
        switch (armorType) {
            case HELMET: {
                if(!equipments.containsKey(EquipmentSlot.HELMET))
                    return EquipmentSlot.HELMET;
                break;
            }
            case CHESTPLATE: {
                if(equipments.containsKey(EquipmentSlot.CHESTPLATE))
                    return EquipmentSlot.CHESTPLATE;
                break;
            }
            case LEGGINGS: {
                if(equipments.containsKey(EquipmentSlot.LEGGINGS))
                    return EquipmentSlot.LEGGINGS;
                break;
            }
            case BOOTS: {
                if(equipments.containsKey(EquipmentSlot.BOOTS))
                    return EquipmentSlot.BOOTS;
                break;
            }
            case BELT: {
                if(equipments.containsKey(EquipmentSlot.BELT))
                    return EquipmentSlot.BELT;
                break;
            }
            case RING: {
                if(!equipments.containsKey(EquipmentSlot.LEFT_RING))
                    return EquipmentSlot.LEFT_RING;
                else if(equipments.containsKey(EquipmentSlot.RIGHT_RING))
                    return EquipmentSlot.RIGHT_RING;
                break;
            }
        }
        return null;
    }

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
