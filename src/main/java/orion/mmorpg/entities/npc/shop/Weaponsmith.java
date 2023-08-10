package orion.mmorpg.entities.npc.shop;

import org.bukkit.Location;
import org.bukkit.entity.Villager;
import orion.mmorpg.entities.samples.AbstractShopkeepers;
import orion.mmorpg.gui.MerchantGui;
import orion.mmorpg.items.ItemList;

public class Weaponsmith extends AbstractShopkeepers {
    public Weaponsmith(String uuid, Location spawnLocation) {
        super(uuid, "Vendeur d'arme", spawnLocation);
        this.getBuyables().add(new MerchantGui.Buyable("gold", 100, ItemList.KNIGHT_SWORD.generateItemStack(1)));
    }


    @Override
    protected void generatePathfinder() {
    }


}
