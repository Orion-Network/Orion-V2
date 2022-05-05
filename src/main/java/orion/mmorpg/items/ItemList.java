package orion.mmorpg.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.items.armors.*;
import orion.mmorpg.items.gui.*;
import orion.mmorpg.items.minecraft.StoneSword;
import orion.mmorpg.items.minecraft.Wheat;
import orion.mmorpg.items.minecraft.WoodenSword;
import orion.mmorpg.items.misc.*;
import orion.mmorpg.items.sample.*;
import orion.mmorpg.items.weapons.*;
import orion.mmorpg.items.weapons.arrows.CommonArrow;
import orion.mmorpg.items.weapons.arrows.GravityArrow;
import orion.mmorpg.items.weapons.arrows.TransmissionArrow;
import orion.mmorpg.utils.Rarity;

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
    FEUTON(new FeuTon(), BasicMagicWeapon.class),
    ARCANE(new ArcAne(), BasicRangedWeapon.class),
    REINFORCED_BOW(new ReinforcedBow(), BasicRangedWeapon.class),
    HOUYI_BOW(new HouYi(), BasicRangedWeapon.class),
    MURAMASA(new Muramasa(), BasicMeleeWeapon.class),
    //Armor Items
    STARTING_HELMET(new StartingSet.StartingHelmet(), BasicArmorItem.class),
    STARTING_CHESTPLATE(new StartingSet.StartingChestplate(), BasicArmorItem.class),
    STARTING_LEGGINGS(new StartingSet.StartingLeggings(), BasicArmorItem.class),
    STARTING_BOOTS(new StartingSet.StartingBoots(), BasicArmorItem.class),
    KNIGHT_HELMET(new KnightSet.KnightHelmet(), BasicArmorItem.class),
    KNIGHT_CHESTPLATE(new KnightSet.KnightChestplate(), BasicArmorItem.class),
    KNIGHT_LEGGINGS(new KnightSet.KnightLeggings(), BasicArmorItem.class),
    KNIGHT_BOOTS(new KnightSet.KnightBoots(), BasicArmorItem.class),
    FUR_HELMET(new FurSet.FurHelmet(), BasicArmorItem.class),
    FUR_CHESTPLATE(new FurSet.FurChestplate(), BasicArmorItem.class),
    FUR_LEGGINGS(new FurSet.FurLeggings(), BasicArmorItem.class),
    FUR_BOOTS(new FurSet.FurBoots(), BasicArmorItem.class),
    NECRO_RING(new NecroSet.NecroRing(), BasicArmorItem.class),
    NECRO_BELT(new NecroSet.NecroBelt(), BasicArmorItem.class),
    WHITE_WALKER_HELMET(new WhiteWalkerSet.WhiteWalkerHelmet(), BasicArmorItem.class),
    WHITE_WALKER_CHESTPLATE(new WhiteWalkerSet.WhiteWalkerChestplate(), BasicArmorItem.class),
    WHITE_WALKER_LEGGINGS(new WhiteWalkerSet.WhiteWalkerLeggings(), BasicArmorItem.class),
    WHITE_WALKER_BOOTS(new WhiteWalkerSet.WhiteWalkerBoots(), BasicArmorItem.class),
    WHITE_WALKER_RING(new WhiteWalkerSet.WhiteWalkerRing(), BasicArmorItem.class),
    WHITE_WALKER_BELT(new WhiteWalkerSet.WhiteWalkerBelt(), BasicArmorItem.class),
    CELESTIAN_CROWN(new CelestianSet.CelestianCrown(), BasicArmorItem.class),
    CELESTIAN_WINGS(new CelestianSet.CelestianWings(), BasicArmorItem.class),
    CELESTIAN_RING(new CelestianSet.CelestianRing(), BasicArmorItem.class),
    CELESTIAN_BELT(new CelestianSet.CelestianBelt(), BasicArmorItem.class),
    STARSET_HELMET(new StarSet.StarsetHelmet(), BasicArmorItem.class),
    STARSET_CHESTPLATE(new StarSet.StarsetChestplate(), BasicArmorItem.class),
    STARSET_LEGGINGS(new StarSet.StarsetLeggings(), BasicArmorItem.class),
    STARSET_BOOTS(new StarSet.StarsetBoots(), BasicArmorItem.class),
    //Arrows
    COMMON_ARROW(new CommonArrow(), BasicArrow.class),
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
    RESTRICTED_PLACEHOLDER(new RestrictedPlaceholder(), BasicGuiItem.class),
    SETTINGS_PLACEHOLDER(new SettingsPlaceholder(), BasicGuiItem.class),
    FIRST_SPECIAL_PLACEHOLDER(new SpecialPlaceholder.FirstSpecialPlaceholder(), BasicGuiItem.class),
    SECOND_SPECIAL_PLACEHOLDER(new SpecialPlaceholder.SecondSpecialPlaceholder(), BasicGuiItem.class),
    THIRD_SPECIAL_PLACEHOLDER(new SpecialPlaceholder.ThirdSpecialPlaceholder(), BasicGuiItem.class),
    RING_PLACEHOLDER(new RingPlaceholder(), BasicGuiItem.class),
    BELT_PLACEHOLDER(new BeltPlaceholder(), BasicGuiItem.class),
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
    ETERNAL_COMPASS(new EternalCompass(), BasicItem.class),
    //Currency
    BRONZE_COIN(BasicItem.generateBasicItem(Material.COPPER_INGOT, "Pièce de Bronze", "BRONZE_COIN", 1, ChatColor.GOLD + "Valeur : 1 $", Rarity.UNCOMMON), BasicItem.class),
    IRON_COIN(BasicItem.generateBasicItem(Material.IRON_INGOT, "Pièce d'Argent", "IRON_COIN", 1, ChatColor.GOLD + "Valeur : 64 $", Rarity.UNCOMMON), BasicItem.class),
    GOLD_COIN(BasicItem.generateBasicItem(Material.GOLD_INGOT, "Pièce d'Or", "GOLD_COIN", 1, ChatColor.GOLD + "Valeur : 4096 $", Rarity.UNCOMMON), BasicItem.class),
    EMERALD_COIN(BasicItem.generateBasicItem(Material.EMERALD, "Pièce d'Emeraude", "EMERALD_COIN", 1, ChatColor.GOLD + "Valeur : 262144 $", Rarity.RARE), BasicItem.class),
    //Beta Floor Drops
    DRY_WHEAT(BasicItem.generateBasicItem(Material.WHEAT, "Blé Sec", "DRY_WHEAT", 1, null, Rarity.COMMON), BasicItem.class),
    WHEAT_SEEDS(BasicItem.generateBasicItem(Material.WHEAT_SEEDS, "Graines", "WHEAT_SEEDS", 1, null, Rarity.COMMON), BasicItem.class),
    SHEEP_LAMB(BasicItem.generateBasicItem(Material.MUTTON, "Lambeau de Mouton", "SHEEP_LAMB", 1, null, Rarity.COMMON), BasicItem.class),
    ROTTEN_FLESH(BasicItem.generateBasicItem(Material.ROTTEN_FLESH, "Chair Pourrie", "ROTTEN_FLESH", 1, null, Rarity.COMMON), BasicItem.class),
    ZOMBIE_HEAD(BasicItem.generateBasicItem(Material.ZOMBIE_HEAD, "Tête de Zombie", "ZOMBIE_HEAD", 1, null, Rarity.COMMON), BasicItem.class),
    DAMAGED_BONE(BasicItem.generateBasicItem(Material.BONE, "Os Endommagé", "DAMAGED_BONE", 1, null, Rarity.COMMON), BasicItem.class),
    SKELETON_SKULL(BasicItem.generateBasicItem(Material.SKELETON_SKULL, "Crâne de Skeleton", "SKELETON_SKULL", 1, null, Rarity.COMMON), BasicItem.class),
    NORDIC_WOLF_FUR(BasicItem.generateBasicItem(Material.LEATHER, "Fourrure de Loup Nordique", "NORDIC_WOLF_FUR", 1, null, Rarity.COMMON), BasicItem.class),
    NORDIC_WOLF_BONE(BasicItem.generateBasicItem(Material.BONE, "Os de Loup Nordique", "NORDIC_WOLF_BONE", 1, null, Rarity.COMMON), BasicItem.class),
    WHITE_WALKER_SOUL(BasicItem.generateBasicItem(Material.SOUL_SAND, "Âme de Marcheur Blanc", "WHITE_WALKER_SOUL", 1, null, Rarity.RARE), BasicItem.class),
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
