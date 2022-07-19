package orion.mmorpg.items.sample;

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
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemType;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Rarity;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public abstract class BasicItem extends ItemStack {

    protected final Material MATERIAL;
    protected final String NAME;
    protected final String INTERNAL_ID;
    protected final int MODEL_ID;
    protected final String LORE;
    protected final Rarity RARITY;
    protected final String PERMISSION;
    protected final ItemType TYPE;
    protected Element ELEMENT = Element.PHYSIC;
    protected final LinkedHashMap<String, Pair<PersistentDataType, Object>> DATA = new LinkedHashMap<>();
    protected Spell spell;

    protected BasicItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, String LORE, Rarity RARITY) {
        this.MATERIAL = MATERIAL;
        this.NAME = NAME;
        this.INTERNAL_ID = INTERNAL_ID;
        this.MODEL_ID = MODEL_ID;
        this.RARITY = RARITY;
        this.LORE = LORE;
        PERMISSION = "basic.item";
        this.TYPE = ItemType.ITEM;
    }

    protected BasicItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, String LORE, Rarity RARITY, ItemType TYPE) {
        this.MATERIAL = MATERIAL;
        this.NAME = NAME;
        this.INTERNAL_ID = INTERNAL_ID;
        this.MODEL_ID = MODEL_ID;
        this.RARITY = RARITY;
        this.LORE = LORE;
        PERMISSION = "basic.item";
        this.TYPE = TYPE;
    }

    protected BasicItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, String LORE, Rarity RARITY, ItemType TYPE, String PERMISSION) {
        this.MATERIAL = MATERIAL;
        this.NAME = NAME;
        this.INTERNAL_ID = INTERNAL_ID;
        this.MODEL_ID = MODEL_ID;
        this.RARITY = RARITY;
        this.LORE = LORE;
        this.TYPE = TYPE;
        this.PERMISSION = PERMISSION;
    }

    public static BasicItem generateTemplateBasicItem(Material MATERIAL, String NAME, int MODEL_ID, String LORE, Rarity RARITY) {
        return new BasicItem(MATERIAL, NAME, "null", MODEL_ID, LORE, RARITY) {
        };
    }

    public static BasicItem generateBasicItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, String LORE, Rarity RARITY) {
        return new BasicItem(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY) {
        };
    }

    public void onPickupItem(EntityPickupItemEvent e, ItemStack item) {
    }

    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR))
            if (spell != null) {
                spell.setCaster(e.getPlayer());
                spell.beforeLaunch(mmorpg, e.getPlayer(), data, false);
            }
    }

    public void onHitWithItem(EntityDamageByEntityEvent e) {
    }

    public ItemStack generateItem(ItemStack item, int amount) {
        if (item == null)
            item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(RARITY.getChatColor() + NAME);
        meta.setLore(generateLore(meta.getLore()));
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING, INTERNAL_ID);
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("type"), PersistentDataType.STRING, TYPE.toString());
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("uuid"), PersistentDataType.STRING, UUID.randomUUID().toString());
        for(String id : DATA.keySet()) {
            Pair<PersistentDataType, Object> data = DATA.get(id);
            meta.getPersistentDataContainer().set(NamespacedKey.fromString(id, Mmorpg.INSTANCE), data.getA(), data.getB());
        }
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setCustomModelData(MODEL_ID);
        item.setItemMeta(meta);
        item.setAmount(amount);
        return item;
    }

    public List<String> generateLore(List<String> lore) {
        if (lore == null)
            lore = new ArrayList<>();

        if (LORE != null) {
            if (lore.size() != 0)
                lore.add("\n");
            lore.add(ChatColor.GRAY + LORE);
        }
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
        lore.add(RARITY.getChatColor() + "" + ChatColor.BOLD + RARITY);

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

    public String getLORE() {
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

    public ItemType getTYPE() {
        return TYPE;
    }

    public LinkedHashMap<String, Pair<PersistentDataType, Object>> getDATA() {
        return DATA;
    }

    public Spell getSpell() {
        return spell;
    }
}
