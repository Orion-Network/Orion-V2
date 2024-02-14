package orion.mmorpg.items.sample;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ExcludeRegister;
import orion.mmorpg.items.ItemType;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

import java.util.List;

@ExcludeRegister
public class BasicConsumableItem extends BasicItem{
    public BasicConsumableItem(ItemStack item) {
        super(item);
    }

    public BasicConsumableItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE, Rarity RARITY) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, ItemType.CONSUMABLE);
    }

    public BasicConsumableItem(Material material, String name, String internalId, int modelId, List<String> lore, Rarity rarity, ItemType itemType) {
        super(material, name, internalId, modelId, lore, rarity, itemType);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        super.onUseItem(mmorpg, data, e);
        e.getItem().setAmount(e.getItem().getAmount() - 1);
        e.getPlayer().updateInventory();
    }

    protected boolean checkScrollValidity(PlayerInteractEvent event, String spell) {
        if (spell == null) {
            event.getItem().setAmount(0);
            event.getPlayer().updateInventory();
            Utils.sendErrorMessage(event.getPlayer(), "Parchemin invalide, suppression de l'item.");
            return false;
        }
        return true;
    }
}
