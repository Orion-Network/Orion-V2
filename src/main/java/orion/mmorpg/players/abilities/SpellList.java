package orion.mmorpg.players.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.spells.*;
import orion.mmorpg.utils.Rarity;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public enum SpellList {
    TEST_SPELL(TestSpell.class, "Test spell", Rarity.UNOBTAINABLE, Material.PAPER, Arrays.asList(ChatColor.GRAY + "test")),
    BOOSTED_MODE(Surpassement.class, "Boosted Mode", Rarity.UNCOMMON, Material.IRON_AXE, Arrays.asList(ChatColor.GRAY + "Boost vos stats")),
    HEAL(Heal.class, "Heal", Rarity.COMMON, Material.POTION, Arrays.asList(ChatColor.GRAY + "Heal 1/10 de votre vitalité.")),
    SKY_FLUX(SkyFlux.class, "Flux Ciel", Rarity.RARE, Material.PHANTOM_MEMBRANE, Arrays.asList(ChatColor.GRAY + "Envoie un punch dans la cible")),
    GOAT_HUNGER(GoatHunger.class, "Goat Hunger", Rarity.HERO, Material.SOUL_TORCH, Arrays.asList(ChatColor.GRAY + "Leurs âmes seront volée")),
    INSTANT_TRANSMISSION(InstantTransmission.class, "Instant Transmission", Rarity.RARE, Material.ENDER_EYE, Arrays.asList(ChatColor.GRAY + "Vous téléporte de 8 blocks")),
    SELF_SUCCION(SelfSuccion.class, "Succion Personelle", Rarity.RARE, Material.IRON_SWORD, Arrays.asList(ChatColor.GRAY + "Divise votre vie par 2 et", "Soigne votre party de 10% des dégats subits", "Ou vous applique l'éffet de Succion Sanguine")),
    FULL_GRAVITY(FullGravity.class, "Full Gravity", Rarity.SUPERRARE, Material.ENDER_EYE, Arrays.asList(ChatColor.GRAY + "Augmente la gravité de façon démentielle empêchant tout saut"));

    Class<? extends Spell> spellClass;
    String name;
    Rarity rarity;
    Material material;
    List<String> lore;

    SpellList(Class<? extends Spell> spellClass, String name, Rarity rarity, Material material, List<String> lore) {
        this.spellClass = spellClass;
        this.name = name;
        this.rarity = rarity;
        this.material = material;
        this.lore = lore;
    }

    public static void learnSpell(OrionPlayer player, String internal_id) {
        try {
            player.getSpells().put(internal_id, (Spell) SpellList.valueOf(internal_id).spellClass.getConstructors()[0].newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public ItemStack getItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(rarity.getChatColor() + name);
        meta.setLore(getLore());
        item.setItemMeta(meta);
        return item;
    }

    public Class<? extends Spell> getSpellClass() {
        return spellClass;
    }

    public String getName() {
        return name;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Material getMaterial() {
        return material;
    }

    public List<String> getLore() {
        return lore;
    }
}