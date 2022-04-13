package orion.mmorpg.items.sample;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;

public abstract class BasicConsumableItem extends BasicItem{
    public BasicConsumableItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, String LORE, Rarity RARITY) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        super.onUseItem(mmorpg, data, e);
        e.getItem().setAmount(e.getItem().getAmount() - 1);
        e.getPlayer().updateInventory();
    }
}
