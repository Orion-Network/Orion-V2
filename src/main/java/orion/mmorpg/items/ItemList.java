package orion.mmorpg.items;

import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.armors.CelestianSet;
import orion.mmorpg.items.armors.StartingSet;
import orion.mmorpg.items.gui.*;
import orion.mmorpg.items.minecraft.StoneSword;
import orion.mmorpg.items.minecraft.Wheat;
import orion.mmorpg.items.minecraft.WoodenSword;
import orion.mmorpg.items.misc.Gift;
import orion.mmorpg.items.misc.GrimoryItem;
import orion.mmorpg.items.misc.InfiniFirework;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.items.sample.BasicGuiItem;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.items.weapons.AdminSword;

public enum ItemList {
    MINECRAFT_WHEAT(new Wheat(), BasicItem.class),
    //Weapon Items
    MINECRAFT_WOODEN_SWORD(new WoodenSword(), BasicMeleeWeapon.class),
    MINECRAFT_STONE_SWORD(new StoneSword(), BasicMeleeWeapon.class),
    ADMIN_SWORD(new AdminSword(), BasicMeleeWeapon.class),
    //Armor Items
    STARTING_HELMET(new StartingSet.StartingHelmet(), BasicArmorItem.class),
    STARTING_CHESTPLATE(new StartingSet.StartingChestplate(), BasicArmorItem.class),
    STARTING_LEGGINGS(new StartingSet.StartingLeggings(), BasicArmorItem.class),
    STARTING_BOOTS(new StartingSet.StartingBoots(), BasicArmorItem.class),
    CELESTIAN_WINGS(new CelestianSet.CelestianWings(), BasicArmorItem.class),
    //GUI Items
    HELMET_PLACEHOLDER(new HelmetPlaceholderItem(), BasicGuiItem.class),
    CHESTPLATE_PLACEHOLDER(new ChestplatePlaceholderItem(), BasicGuiItem.class),
    LEGGINGS_PLACEHOLDER(new LeggingsPlaceholderItem(), BasicGuiItem.class),
    BOOTS_PLACEHOLDER(new BootsPlaceholder(), BasicGuiItem.class),
    ACCEPT_BUTTON_PLACEHOLDER(new AcceptButtonPlaceholder(), BasicGuiItem.class),
    REFUSE_BUTTON_PLACEHOLDER(new RefuseButtonPlaceholder(), BasicGuiItem.class),
    SPELL_PLACEHOLDER(new SpellPlaceholderItem(), BasicGuiItem.class),
    PORTAL_STONE(new PortalStoneItem(), BasicGuiItem.class),
    //MISC
    GIFT(new Gift(), BasicItem.class),
    GRIMORY(new GrimoryItem(), BasicItem.class),
    INFINITI_FIREWORK(new InfiniFirework(), BasicItem.class),
    ;

    BasicItem item;
    Class<?> itemClass;

    ItemList(BasicItem item, Class<? extends BasicItem> itemClass) {
        this.item = item;
        this.itemClass = itemClass;
    }

    public BasicItem getItem() {
        return item;
    }

    public Class<?> getItemClass() {
        return itemClass;
    }

    public ItemStack getProcessedItem(int amount) {
        return item.generateItem(null, amount);
    }
}
