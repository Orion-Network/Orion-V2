package orion.mmorpg.items.misc;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicConsumableItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

public class MasterScrollItem extends BasicConsumableItem {

    public MasterScrollItem() {
        super(Material.PAPER, "Parchemin", "MASTER_SCROLL", 1, "Un parchemin pouvant vous apprendre des sorts", Rarity.SUPERRARE);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        String spell = e.getItem().getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("spell_id"), PersistentDataType.STRING);
        if (spell == null) {
            e.getItem().setAmount(0);
            e.getPlayer().updateInventory();
            Utils.sendErrorMessage(e.getPlayer(), "Parchemin invalide, suppression de l'item.");
            return;
        }
        Utils.sendInfoMessage(e.getPlayer(), "Vous avez appris le sort : " + SpellList.valueOf(spell).getName());
        SpellList.learnSpell(data, spell);
        super.onUseItem(mmorpg, data, e);
    }

    public ItemStack generateScroll(String spell_id) {
        spell_id = spell_id.toUpperCase();
        ItemStack i = generateItem(null, 1);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(this.RARITY.getChatColor() + this.NAME + " " + SpellList.valueOf(spell_id).getRarity().getChatColor() + SpellList.valueOf(spell_id).getName());
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("spell_id"), PersistentDataType.STRING, spell_id);
        i.setItemMeta(meta);
        return i;
    }
}
