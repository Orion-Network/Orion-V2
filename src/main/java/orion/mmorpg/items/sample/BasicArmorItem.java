package orion.mmorpg.items.sample;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemType;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasicArmorItem extends BasicEquipmentItem {
    public final ArmorType ARMORTYPE;

    public BasicArmorItem(ItemStack item) {
        super(item);
        this.ARMORTYPE = ArmorType.valueOf(item.getItemMeta().getPersistentDataContainer().get(Namespaces.armor_type, PersistentDataType.STRING));
    }

    public BasicArmorItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE, Rarity RARITY, ArmorType ARMORTYPE, Stats.Modifiers modifiers) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, ItemType.ARMOR, modifiers);
        this.ARMORTYPE = ARMORTYPE;
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        Utils.sendInfoMessage(e.getPlayer(), "Vous devez vous équiper via le /menu");
        e.setCancelled(true);
    }

    @Override
    public ItemStack generateItem(ItemStack item, int amount) {
        if (item == null)
            item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(Namespaces.armor_type, PersistentDataType.STRING, ARMORTYPE.name());
        meta.getPersistentDataContainer().set(Namespaces.uuid, PersistentDataType.STRING, UUID.randomUUID().toString());
        item.setItemMeta(meta);
        return super.generateItem(item, amount);
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        if (lore == null)
            lore = new ArrayList<>();
        lore.addAll(getModifiers().getAsLore());

        return super.generateLore(lore);
    }

    public boolean onEquip(Player p, OrionPlayer data, ItemStack item) {
        data.getStats().addModifiers(getINTERNAL_ID(), getModifiers());
        onCosmeticEquip(p, data, item);
        return true;
    }

    public void onCosmeticEquip(Player p, OrionPlayer data, ItemStack item) {

    }

    public boolean onUnequip(Player p, OrionPlayer data, ItemStack item) {
        data.getStats().removeModifiers(getINTERNAL_ID());
        onCosmeticUnequip(p, data, item);
        return true;
    }

    public void onCosmeticUnequip(Player p, OrionPlayer data, ItemStack item) {

    }

    public ArmorType getARMORTYPE() {
        return ARMORTYPE;
    }

    public enum ArmorType {
        HELMET, CHESTPLATE, LEGGINGS, BOOTS, WINGS, BELT, RING
    }
}
