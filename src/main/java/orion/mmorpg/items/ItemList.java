package orion.mmorpg.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.items.armors.CelestianSet;
import orion.mmorpg.items.armors.KnightSet;
import orion.mmorpg.items.armors.StarSet;
import orion.mmorpg.items.armors.StartingSet;
import orion.mmorpg.items.gui.*;
import orion.mmorpg.items.minecraft.StoneSword;
import orion.mmorpg.items.minecraft.Wheat;
import orion.mmorpg.items.minecraft.WoodenSword;
import orion.mmorpg.items.misc.*;
import orion.mmorpg.items.sample.*;
import orion.mmorpg.items.weapons.*;
import orion.mmorpg.items.weapons.arrows.GravityArrow;
import orion.mmorpg.items.weapons.arrows.TransmissionArrow;

public enum ItemList {
    MINECRAFT_WHEAT(new Wheat(), BasicItem.class),
    //Weapon Items
    MINECRAFT_WOODEN_SWORD(new WoodenSword(), BasicMeleeWeapon.class),
    MINECRAFT_STONE_SWORD(new StoneSword(), BasicMeleeWeapon.class),
    KNIGHT_SWORD(new KnightSet.KnightSword(), BasicMeleeWeapon.class),
    JOKER_CARD(new JokerCard(), BasicMeleeWeapon.class),
    ADMIN_SWORD(new AdminSword(), BasicMeleeWeapon.class),
    ASPECT_OF_JAIRI(new AspectOfJairi(), BasicMeleeWeapon.class),
    BAHTON(new BahTon(), BasicMagicWeapon.class),
    ARCANE(new ArcAne(), BasicRangedWeapon.class),
    HOUYI_BOW(new HouYi(), BasicRangedWeapon.class),
    AMATERASU(new Amaterasu(), BasicMeleeWeapon.class),
    //Armor Items
    STARTING_HELMET(new StartingSet.StartingHelmet(), BasicArmorItem.class),
    STARTING_CHESTPLATE(new StartingSet.StartingChestplate(), BasicArmorItem.class),
    STARTING_LEGGINGS(new StartingSet.StartingLeggings(), BasicArmorItem.class),
    STARTING_BOOTS(new StartingSet.StartingBoots(), BasicArmorItem.class),
    KNIGHT_HELMET(new KnightSet.KnightHelmet(), BasicArmorItem.class),
    KNIGHT_CHESTPLATE(new KnightSet.KnightChestplate(), BasicArmorItem.class),
    KNIGHT_LEGGINGS(new KnightSet.KnightLeggings(), BasicArmorItem.class),
    KNIGHT_BOOTS(new KnightSet.KnightBoots(), BasicArmorItem.class),
    CELESTIAN_CROWN(new CelestianSet.CelestianCrown(), BasicArmorItem.class),
    CELESTIAN_WINGS(new CelestianSet.CelestianWings(), BasicArmorItem.class),
    STARSET_HELMET(new StarSet.StarsetHelmet(), BasicArmorItem.class),
    STARSET_CHESTPLATE(new StarSet.StarsetChestplate(), BasicArmorItem.class),
    STARSET_LEGGINGS(new StarSet.StarsetLeggings(), BasicArmorItem.class),
    STARSET_BOOTS(new StarSet.StarsetBoots(), BasicArmorItem.class),
    //Arrows
    GRAVITY_ARROW(new GravityArrow(), BasicArrow.class),
    TRANSMISSION_ARROW(new TransmissionArrow(), BasicArrow.class),
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
    UNUSABLE_PLACEHOLDER(new UnusablePlaceholder(), BasicGuiItem.class),
    USABLE_PLACEHOLDER(new UsablePlaceholder(), BasicGuiItem.class),
    SETTINGS_PLACEHOLDER(new SettingsPlaceholder(), BasicGuiItem.class),
    //MISC
    GIFT(new Gift(), BasicItem.class),
    GRIMORY(new GrimoryItem(), BasicItem.class),
    MASTER_SCROLL(new MasterScrollItem(), BasicItem.class),
    SPELL_SCROLL(new SpellScroll(), BasicItem.class),
    INFINITE_FIREWORK(new InfiniteFirework(), BasicItem.class),
    CELESTIAL_SERUM(new CelestialSerum(), BasicItem.class),
    SOUL_SWITCHER(new SoulSwitcher(), BasicItem.class),
    TEST_ITEM(new TestItem(), BasicItem.class),
    MENU_ITEM(new MenuItem(), BasicItem.class),
    ;

    BasicItem item;
    Class<?> itemClass;

    ItemList(BasicItem item, Class<? extends BasicItem> itemClass) {
        this.item = item;
        this.itemClass = itemClass;
    }

    public static BasicItem getItemFromItemstack(ItemStack itemStack) {
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        return valueOf(container.get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)).getItem();
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
