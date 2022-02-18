package orion.mmorpg.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.items.armors.CelestianSet;
import orion.mmorpg.items.armors.StarSet;
import orion.mmorpg.items.armors.StartingSet;
import orion.mmorpg.items.gui.*;
import orion.mmorpg.items.minecraft.StoneSword;
import orion.mmorpg.items.minecraft.Wheat;
import orion.mmorpg.items.minecraft.WoodenSword;
import orion.mmorpg.items.misc.*;
import orion.mmorpg.items.sample.*;
import orion.mmorpg.items.weapons.*;

public enum ItemList {
    MINECRAFT_WHEAT(new Wheat(), BasicItem.class),
    //Weapon Items
    MINECRAFT_WOODEN_SWORD(new WoodenSword(), BasicMeleeWeapon.class),
    MINECRAFT_STONE_SWORD(new StoneSword(), BasicMeleeWeapon.class),
    JOKER_CARD(new JokerCard(), BasicMeleeWeapon.class),
    ADMIN_SWORD(new AdminSword(), BasicMeleeWeapon.class),
    ASPECT_OF_JAIRI(new AspectOfJairi(), BasicMeleeWeapon.class),
    BAHTON(new BahTon(), BasicMagicWeapon.class),
    ARCANE(new ArcAne(), BasicRangedWeapon.class),
    //Armor Items
    STARTING_HELMET(new StartingSet.StartingHelmet(), BasicArmorItem.class),
    STARTING_CHESTPLATE(new StartingSet.StartingChestplate(), BasicArmorItem.class),
    STARTING_LEGGINGS(new StartingSet.StartingLeggings(), BasicArmorItem.class),
    STARTING_BOOTS(new StartingSet.StartingBoots(), BasicArmorItem.class),
    CELESTIAN_CROWN(new CelestianSet.CelestianCrown(), BasicArmorItem.class),
    CELESTIAN_WINGS(new CelestianSet.CelestianWings(), BasicArmorItem.class),
    STARSET_HELMET(new StarSet.StarsetHelmet(), BasicArmorItem.class),
    STARSET_CHESTPLATE(new StarSet.StarsetChestplate(), BasicArmorItem.class),
    STARSET_LEGGINGS(new StarSet.StarsetLeggings(), BasicArmorItem.class),
    STARSET_BOOTS(new StarSet.StarsetBoots(), BasicArmorItem.class),
    //GUI Items
    HELMET_PLACEHOLDER(new HelmetPlaceholderItem(), BasicGuiItem.class),
    CHESTPLATE_PLACEHOLDER(new ChestplatePlaceholderItem(), BasicGuiItem.class),
    LEGGINGS_PLACEHOLDER(new LeggingsPlaceholderItem(), BasicGuiItem.class),
    BOOTS_PLACEHOLDER(new BootsPlaceholder(), BasicGuiItem.class),
    ACCEPT_BUTTON_PLACEHOLDER(new AcceptButtonPlaceholder(), BasicGuiItem.class),
    REFUSE_BUTTON_PLACEHOLDER(new RefuseButtonPlaceholder(), BasicGuiItem.class),
    UP_BUTTON_PLACEHOLDER(new UpButtonPlaceholder(), BasicGuiItem.class),
    RIGHT_BUTTON_PLACEHOLDER(new RightButtonPlaceholder(), BasicGuiItem.class),
    DOWN_BUTTON_PLACEHOLDER(new DownButtonPlaceholder(), BasicGuiItem.class),
    LEFT_BUTTON_PLACEHOLDER(new LeftButtonPlaceholder(), BasicGuiItem.class),
    SPELL_PLACEHOLDER(new SpellPlaceholderItem(), BasicGuiItem.class),
    PORTAL_STONE(new PortalStoneItem(), BasicGuiItem.class),
    WIKI_BOOK_PLACEHOLDER(new WikiBookPlaceholder(), BasicGuiItem.class),
    //MISC
    GIFT(new Gift(), BasicItem.class),
    GRIMORY(new GrimoryItem(), BasicItem.class),
    MASTER_SCROLL(new MasterScrollItem(), BasicItem.class),
    INFINITE_FIrlREWORK(new InfiniteFirework(), BasicItem.class),
    CELESTIAL_SERUM(new CelestialSerum(), BasicItem.class),
    SOUL_SWITCHER(new SoulSwitcher(), BasicItem.class),
    TEST_ITEM(new TestItem(), BasicItem.class),
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

    public static BasicItem getItemFromItemstack(ItemStack itemStack) {
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        return valueOf(container.get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)).getItem();
    }
}
