package orion.mmorpg.items;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.EnumUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.items.armors.*;
import orion.mmorpg.items.gui.*;
import orion.mmorpg.items.minecraft.StoneSword;
import orion.mmorpg.items.minecraft.Wheat;
import orion.mmorpg.items.minecraft.WoodenSword;
import orion.mmorpg.items.misc.*;
import orion.mmorpg.items.misc.drops.NordicWolfBone;
import orion.mmorpg.items.misc.drops.NordicWolfFur;
import orion.mmorpg.items.misc.reforge.StrongStone;
import orion.mmorpg.items.money.BronzeCoin;
import orion.mmorpg.items.ressources.BronzeIngot;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.items.misc.NullItem;
import orion.mmorpg.items.tools.AdminRod;
import orion.mmorpg.items.tools.MinerPickaxe;
import orion.mmorpg.items.tools.Picky;
import orion.mmorpg.items.tools.WoodenRod;
import orion.mmorpg.items.weapons.*;
import orion.mmorpg.items.weapons.arrows.CommonArrow;
import orion.mmorpg.items.weapons.arrows.GravityArrow;
import orion.mmorpg.items.weapons.arrows.TransmissionArrow;
import orion.mmorpg.utils.Namespaces;

import java.lang.reflect.InvocationTargetException;

public enum ItemList {
    MINECRAFT_WHEAT(Wheat.class),
    //Weapon Items
    MINECRAFT_WOODEN_SWORD(WoodenSword.class),
    MINECRAFT_STONE_SWORD(StoneSword.class),
    KNIGHT_SWORD(KnightSet.KnightSword.class),
    JOKER_CARD(JokerCard.class),
    ADMIN_SWORD(AdminSword.class),
    ASPECT_OF_JAIRI(AspectOfJairi.class),
    BAHTON(BahTon.class),
    FEUTON(FeuTon.class),
    ARCANE(ArcAne.class),
    REINFORCED_BOW(ReinforcedBow.class),
    HOUYI_BOW(HouYi.class),
    MURAMASA(Muramasa.class),
    THOUSANDS_WISDOM(ThousandWisdom.class),
    //Armor Items
    STARTING_HELMET(StartingSet.StartingBoots.class),
    STARTING_CHESTPLATE(StartingSet.StartingChestplate.class),
    STARTING_LEGGINGS(StartingSet.StartingLeggings.class),
    STARTING_BOOTS(StartingSet.StartingBoots.class),
    KNIGHT_HELMET(KnightSet.KnightHelmet.class),
    KNIGHT_CHESTPLATE(KnightSet.KnightChestplate.class),
    KNIGHT_LEGGINGS(KnightSet.KnightLeggings.class),
    KNIGHT_BOOTS(KnightSet.KnightBoots.class),
    FUR_HELMET(FurSet.FurHelmet.class),
    FUR_CHESTPLATE(FurSet.FurChestplate.class),
    FUR_LEGGINGS(FurSet.FurLeggings.class),
    FUR_BOOTS(FurSet.FurBoots.class),
    NECRO_RING(NecroSet.NecroRing.class),
    NECRO_BELT(NecroSet.NecroBelt.class),
    WHITE_WALKER_HELMET(WhiteWalkerSet.WhiteWalkerHelmet.class),
    WHITE_WALKER_CHESTPLATE(WhiteWalkerSet.WhiteWalkerChestplate.class),
    WHITE_WALKER_LEGGINGS(WhiteWalkerSet.WhiteWalkerLeggings.class),
    WHITE_WALKER_BOOTS(WhiteWalkerSet.WhiteWalkerBoots.class),
    WHITE_WALKER_RING(WhiteWalkerSet.WhiteWalkerRing.class),
    WHITE_WALKER_BELT(WhiteWalkerSet.WhiteWalkerBelt.class),
    CELESTIAN_CROWN(CelestianSet.CelestianCrown.class),
    CELESTIAN_WINGS(CelestianSet.CelestianWings.class),
    CELESTIAN_RING(CelestianSet.CelestianRing.class),
    CELESTIAN_BELT(CelestianSet.CelestianBelt.class),
    STARSET_HELMET(StarSet.StarsetHelmet.class),
    STARSET_CHESTPLATE(StarSet.StarsetChestplate.class),
    STARSET_LEGGINGS(StarSet.StarsetLeggings.class),
    STARSET_BOOTS(StarSet.StarsetBoots.class),
    //Arrows
    COMMON_ARROW(CommonArrow.class),
    GRAVITY_ARROW(GravityArrow.class),
    TRANSMISSION_ARROW(TransmissionArrow.class),
    //GUI Items
    HELMET_PLACEHOLDER(HelmetPlaceholderItem.class),
    CHESTPLATE_PLACEHOLDER(ChestplatePlaceholderItem.class),
    LEGGINGS_PLACEHOLDER(LeggingsPlaceholderItem.class),
    BOOTS_PLACEHOLDER(BootsPlaceholder.class),
    PET_PLACEHOLDER(PetPlaceholder.class),
    MOUNT_PLACEHOLDER(MountPlaceholder.class),
    ACCEPT_BUTTON_PLACEHOLDER(AcceptButtonPlaceholder.class),
    REFUSE_BUTTON_PLACEHOLDER(RefuseButtonPlaceholder.class),
    UP_BUTTON_PLACEHOLDER(UpButtonPlaceholder.class),
    RIGHT_BUTTON_PLACEHOLDER(RightButtonPlaceholder.class),
    DOWN_BUTTON_PLACEHOLDER(DownButtonPlaceholder.class),
    LEFT_BUTTON_PLACEHOLDER(LeftButtonPlaceholder.class),
    SPELL_PLACEHOLDER(SpellPlaceholderItem.class),
    PORTAL_STONE(PortalStoneItem.class),
    WIKI_BOOK_PLACEHOLDER(WikiBookPlaceholder.class),
    UNUSABLE_PLACEHOLDER(UnusablePlaceholder.class),
    USABLE_PLACEHOLDER(UsablePlaceholder.class),
    RESTRICTED_PLACEHOLDER(RestrictedPlaceholder.class),
    SETTINGS_PLACEHOLDER(SettingsPlaceholder.class),
    TBI_PLACEHOLDER(ToBeImplementedPlaceholder.class),
    FIRST_SPECIAL_PLACEHOLDER(SpecialPlaceholder.FirstSpecialPlaceholder.class),
    SECOND_SPECIAL_PLACEHOLDER(SpecialPlaceholder.SecondSpecialPlaceholder.class),
    THIRD_SPECIAL_PLACEHOLDER(SpecialPlaceholder.ThirdSpecialPlaceholder.class),
    RING_PLACEHOLDER(RingPlaceholder.class),
    BELT_PLACEHOLDER(BeltPlaceholder.class),
    JOB_PLACEHOLDER(JobPlaceholder.class),
    //MISC
    GIFT(Gift.class),
    NULL_ITEM(NullItem.class),
    GRIMORY(GrimoryItem.class),
    MASTER_SCROLL(MasterScrollItem.class),
    SPELL_SCROLL(SpellScroll.class),
    MYTHICAL_SCROLL(MythicalScroll.class),
    INFINITE_FIREWORK(InfiniteFirework.class),
    CELESTIAL_SERUM(CelestialSerum.class),
    SOUL_SWITCHER(SoulSwitcher.class),
    GUILD_BOOK(GuildBook.class),
    TEST_ITEM(TestItem.class),
    MENU_ITEM(MenuItem.class),
    ETERNAL_COMPASS(EternalCompass.class),
    //DROPS
    NORDIC_WOLF_FUR(NordicWolfFur.class),
    NORDIC_WOLF_BONE(NordicWolfBone.class),
    //REFORGES
    STRONG_STONE(StrongStone.class),
    //TOOLS
    MINER_PICKAXE(MinerPickaxe.class),
    PICKY(Picky.class),
    WOODEN_ROD(WoodenRod.class),
    ADMIN_ROD(AdminRod.class),

    //RESSOURCES
    BRONZE_INGOT(BronzeIngot.class),

    //CURRENCY
    BRONZE_COIN(BronzeCoin.class),
    ;

    Class<?> itemClass;

    ItemList(Class<? extends BasicItem> itemClass) {
        this.itemClass = itemClass;
    }

    public static BasicItem getItemFromItemstack(ItemStack itemStack) {
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if(container.has(Namespaces.internal_id, PersistentDataType.STRING) && EnumUtils.isValidEnum(ItemList.class,container.get(Namespaces.internal_id, PersistentDataType.STRING) )) {
            try {
                return (BasicItem) ItemList.valueOf(container.get(Namespaces.internal_id, PersistentDataType.STRING)).getItemClass().getSuperclass().getDeclaredConstructor(ItemStack.class).newInstance(itemStack);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Class<?> getItemClass() {
        return itemClass;
    }

    public BasicItem generateItem() {
        try {
            return (BasicItem) itemClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemStack generateItemStack(int i) {
        if(generateItem() == null)
            return null;
        return generateItem().generateItem(null, i);
    }
}
