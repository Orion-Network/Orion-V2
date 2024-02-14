package orion.mmorpg.items.sample;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ExcludeRegister;
import orion.mmorpg.items.ItemType;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.OEffect;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class to represent items as Rpg Item
 */
// TODO add to lore Rarity + ItemType
@ExcludeRegister
public abstract class BasicItem extends ItemStack {

    static HashMap<String, BasicItem> ressourceItems = new HashMap<>();

    protected final Material MATERIAL;
    protected final String NAME;
    protected final String INTERNAL_ID;
    protected final int MODEL_ID;
    protected final List<String> LORE;
    protected final Rarity RARITY;
    protected final String PERMISSION;
    protected final ItemType ITEM_TYPE;
    protected Element ELEMENT = Element.PHYSIC;
    protected final LinkedHashMap<String, Utils.Pair<PersistentDataType, Object>> DATA = new LinkedHashMap<>();
    protected Spell spell;

    protected BasicItem(ItemStack item) {
        this(item.getType(),
                item.getItemMeta().getPersistentDataContainer().get(Namespaces.name, PersistentDataType.STRING),
                item.getItemMeta().getPersistentDataContainer().get(Namespaces.internal_id, PersistentDataType.STRING),
                item.getItemMeta().getCustomModelData(),
                Mmorpg.getOrionGson().getGSON()
                        .fromJson(item.getItemMeta().getPersistentDataContainer().get(Namespaces.lore,
                                PersistentDataType.STRING), List.class),
                Rarity.valueOf(item.getItemMeta().getPersistentDataContainer().get(Namespaces.rarity,
                        PersistentDataType.STRING)),
                ItemType.valueOf(item.getItemMeta().getPersistentDataContainer().get(Namespaces.type,
                        PersistentDataType.STRING)));
        this.setItemMeta(item.getItemMeta());
    }

    protected BasicItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE,
            Rarity RARITY) {
        this(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, ItemType.ITEM);
    }

    protected BasicItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE,
            Rarity RARITY, ItemType ITEM_TYPE) {
        this(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, ITEM_TYPE, "basic.item");
    }

    protected BasicItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE,
            Rarity RARITY, ItemType ITEM_TYPE, String PERMISSION) {
        this.MATERIAL = MATERIAL;
        this.NAME = NAME;
        this.INTERNAL_ID = INTERNAL_ID;
        this.MODEL_ID = MODEL_ID;
        this.RARITY = RARITY;
        if (LORE != null)
            this.LORE = LORE.stream().map(a -> ChatColor.GRAY + a).collect(Collectors.toList());
        else
            this.LORE = null;
        this.ITEM_TYPE = ITEM_TYPE;
        this.PERMISSION = PERMISSION;
    }

    public static BasicItem generateRessourceItem(String identifier, Material MATERIAl, String NAME, int MODEL_ID,
            List<String> LORE, Rarity RARITY) {
        BasicItem item = new BasicItem(MATERIAl, NAME, "null", MODEL_ID, LORE, RARITY, ItemType.RESSOURCE) {
        };
        if (!ressourceItems.containsKey(identifier))
            ressourceItems.put(identifier, item);
        return item;
    }

    public static BasicItem generateIngredientItem(Material MATERIAl, String NAME, int MODEL_ID, List<String> LORE,
            Rarity RARITY, Class<? extends OEffect> effect) {
        BasicItem item = new BasicIngredient(MATERIAl, NAME, "null", MODEL_ID, LORE, RARITY, effect) {
        };
        if (!ressourceItems.containsKey(NAME))
            ressourceItems.put(NAME, item);
        return item;
    }

    public static BasicItem generateTemplateBasicItem(Material MATERIAL, String NAME, int MODEL_ID, List<String> LORE,
            Rarity RARITY) {
        return new BasicItem(MATERIAL, NAME, "null", MODEL_ID, LORE, RARITY) {
        };
    }

    public void onPickupItem(EntityPickupItemEvent e, ItemStack item) {
    }

    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR))
            if (spell != null) {
                spell.setCaster(e.getPlayer());
                spell.beforeLaunchFromItem(mmorpg, e.getPlayer(), new Knowledge(100));
            }
    }

    public void onHitWithItem(EntityDamageByEntityEvent e) {
    }

    public ItemStack generateItem(ItemStack item, int amount) {
        if (item == null)
            item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(generateDisplayName());
        meta.setLore(generateLore(null));

        meta.getPersistentDataContainer().set(Namespaces.name, PersistentDataType.STRING, NAME);
        meta.getPersistentDataContainer().set(Namespaces.lore, PersistentDataType.STRING,
                Mmorpg.getOrionGson().getGSON().toJson(getLORE()));
        meta.getPersistentDataContainer().set(Namespaces.rarity, PersistentDataType.STRING, RARITY.toString());
        meta.getPersistentDataContainer().set(Namespaces.internal_id, PersistentDataType.STRING, INTERNAL_ID);
        meta.getPersistentDataContainer().set(Namespaces.type, PersistentDataType.STRING, ITEM_TYPE.toString());
        for (String id : DATA.keySet()) {
            Utils.Pair<PersistentDataType, Object> data = DATA.get(id);
            meta.getPersistentDataContainer().set(NamespacedKey.minecraft(id), data.getKey(), data.getValue());
        }
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setCustomModelData(MODEL_ID);
        item.setItemMeta(meta);
        item.setAmount(amount);
        return item;
    }

    public String generateDisplayName() {
        return RARITY.getChatColor() + NAME;
    }

    public List<String> generateLore(List<String> lore) {
        if (lore == null)
            lore = new ArrayList<>();
        if (LORE != null)
            lore.addAll(LORE);

        if (this.spell != null) {
            if (lore.size() != 0)
                lore.add("\n");
            SpellList sp = SpellList.valueOf(spell.getInternal_id());
            lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Sort : " + sp.getName());
            List<String> finalLore = new ArrayList<>();
            sp.getLore().forEach(e -> {
                e = ChatColor.GRAY + e;
                finalLore.add(e);
            });

            lore.addAll(finalLore);
        }

        lore.add("\n");
        lore.add(RARITY.getChatColor() + "" + ChatColor.BOLD + RARITY + " " + ITEM_TYPE.name().replaceAll("_", " "));

        return lore;
    }

    public Material getMATERIAL() {
        return MATERIAL;
    }

    public String getNAME() {
        return NAME;
    }

    public String getINTERNAL_ID() {
        return INTERNAL_ID;
    }

    public int getMODEL_ID() {
        return MODEL_ID;
    }

    public List<String> getLORE() {
        return LORE;
    }

    public Rarity getRARITY() {
        return RARITY;
    }

    public Element getELEMENT() {
        return ELEMENT;
    }

    public String getPERMISSION() {
        return PERMISSION;
    }

    public ItemType getITEM_TYPE() {
        return ITEM_TYPE;
    }

    public LinkedHashMap<String, Utils.Pair<PersistentDataType, Object>> getDATA() {
        return DATA;
    }

    public Spell getSpell() {
        return spell;
    }
}
