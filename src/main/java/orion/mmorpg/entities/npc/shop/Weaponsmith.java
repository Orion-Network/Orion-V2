package orion.mmorpg.entities.npc.shop;

import org.bukkit.Location;
import org.bukkit.entity.Villager;
import orion.mmorpg.entities.samples.AbstractShopkeepers;
import orion.mmorpg.gui.MerchantGui;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.misc.SpellScroll;

public class Weaponsmith extends AbstractShopkeepers {
    public Weaponsmith(String uuid, Location spawnLocation) {
        super(uuid, "Vendeur divers", spawnLocation);
        this.getBuyables().add(new MerchantGui.Buyable("gold", 100, ItemList.KNIGHT_SWORD.generateItemStack(1)));
        this.getBuyables().add(new MerchantGui.Buyable("gold", 25, ItemList.WOODEN_ROD.generateItemStack(1)));

        this.getBuyables().add(new MerchantGui.Buyable("gold", 25, ItemList.KNIGHT_HELMET.generateItemStack(1)));
        this.getBuyables().add(new MerchantGui.Buyable("gold", 25, ItemList.KNIGHT_CHESTPLATE.generateItemStack(1)));
        this.getBuyables().add(new MerchantGui.Buyable("gold", 25, ItemList.KNIGHT_LEGGINGS.generateItemStack(1)));
        this.getBuyables().add(new MerchantGui.Buyable("gold", 25, ItemList.KNIGHT_BOOTS.generateItemStack(1)));


        this.getBuyables().add(new MerchantGui.Buyable("gold", 250, ItemList.GRIMORY.generateItemStack(1)));

        this.getBuyables().add(new MerchantGui.Buyable("gold", 50, ((SpellScroll) ItemList.SPELL_SCROLL.generateItem()).generateScroll("HEAL")));
    }


    @Override
    protected void generatePathfinder() {
    }


}
