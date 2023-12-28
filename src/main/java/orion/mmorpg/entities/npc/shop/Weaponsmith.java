package orion.mmorpg.entities.npc.shop;

import org.bukkit.Location;
import orion.mmorpg.entities.samples.AbstractShopkeepers;
import orion.mmorpg.gui.MerchantGui;
import orion.mmorpg.utils.ItemUtils;

public class Weaponsmith extends AbstractShopkeepers {
    public Weaponsmith(String uuid, Location spawnLocation) {
        super(uuid, "Vendeur divers", spawnLocation);
        this.getBuyables().add(new MerchantGui.Buyable("gold", 100, ItemUtils.generateItemStack("KNIGHT_SWORD", 1)));
        this.getBuyables().add(new MerchantGui.Buyable("gold", 25, ItemUtils.generateItemStack("WOODEN_ROD", 1)));

        this.getBuyables().add(new MerchantGui.Buyable("gold", 250, ItemUtils.generateItemStack("GRIMORY", 1)));

        //this.getBuyables().add(new MerchantGui.Buyable("gold", 50, ((SpellScroll) ItemList.SPELL_SCROLL.generateItem()).generateScroll("HEAL")));
    }


    @Override
    protected void generatePathfinder() {
    }


}
