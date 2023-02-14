package orion.mmorpg.players.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.spells.*;
import orion.mmorpg.utils.Knowledge;
import orion.mmorpg.utils.Rarity;

import java.util.Arrays;
import java.util.List;

public enum SpellList {
    TEST_SPELL(TestSpell.class, "Test spell", Rarity.UNOBTAINABLE, Material.PAPER, Arrays.asList(ChatColor.GRAY + "test")),
    BOOSTED_MODE(Surpassement.class, "Boosted Mode", Rarity.UNCOMMON, Material.IRON_AXE, Arrays.asList(ChatColor.GRAY + "Boost vos stats")),
    HEAL(Heal.class, "Heal", Rarity.COMMON, Material.POTION, Arrays.asList(ChatColor.GRAY + "Heal 1/10 de votre vitalité.")),
    SKY_FLUX(SkyFlux.class, "Flux Ciel", Rarity.RARE, Material.PHANTOM_MEMBRANE, Arrays.asList(ChatColor.GRAY + "Envoie un punch dans la cible")),
    GOAT_HUNGER(GoatHunger.class, "Goat Hunger", Rarity.HERO, Material.SOUL_TORCH, Arrays.asList(ChatColor.GRAY + "Leurs âmes seront volée")),
    INSTANT_TRANSMISSION(InstantTransmission.class, "Instant Transmission", Rarity.RARE, Material.ENDER_EYE, Arrays.asList(ChatColor.GRAY + "Vous téléporte de 8 blocks")),
    SELF_SUCCION(SelfSuccion.class, "Succion Personelle", Rarity.RARE, Material.IRON_SWORD, Arrays.asList(ChatColor.GRAY + "Divise votre vie par 2 et", ChatColor.GRAY + "Soigne votre partie de 10% des dégats subits", ChatColor.GRAY +  "Ou vous applique l'effet de Succion Sanguine si vous n'êtes dans aucune partie")),
    FULL_GRAVITY(FullGravity.class, "Full Gravity", Rarity.SUPERRARE, Material.ENDER_EYE, Arrays.asList(ChatColor.GRAY + "Augmente la gravité de façon démentielle empêchant tout saut")),
    REPULSION(Repulsion.class, "Repulsion", Rarity.MYTHIC, Material.TNT, Arrays.asList(ChatColor.GRAY + "Repousse tout les joueurs alentours")),
    ATTRACTION(Attraction.class, "Attraction", Rarity.MYTHIC, Material.TNT, Arrays.asList(ChatColor.GRAY + "Attire tout les joueurs alentours")),
    DARK_ROOM(DarkRoom.class, "Dark Room", Rarity.RARE, Material.BLACK_CANDLE, Arrays.asList("Plonge une zone dans l'obscurité")),
    BACK_TIME(BackTime.class, "Back Time", Rarity.HERO, Material.CLOCK, Arrays.asList(ChatColor.GRAY + "Vous téléporte à l'endroit où vous étiez il y a 15 secondes")),
    SWITCH(Switch.class, "Switch", Rarity.RARE, Material.ENDER_PEARL, Arrays.asList(ChatColor.GRAY + "Echangez de place avec une entitée en face de vous")),
    CHANNEL_STRIKE(ChannelStrike.class, "Channel Strike", Rarity.RARE, Material.DIAMOND_SWORD, Arrays.asList(ChatColor.GRAY + "Inflict des dégats à une entitée en face de vous et a celle la plus proche jusqu'a 5 fois")),
    ;

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

    public static void learnSpell(OrionPlayer player, Class<? extends Spell> spellClass, int knowledge) {
        if(!player.getKnownSpells().containsKey(spellClass))
            player.getKnownSpells().put(spellClass, new Knowledge(knowledge));
        else
            player.getKnownSpells().get(spellClass).addKnowledge(knowledge);
    }

    public ItemStack getItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(rarity.getChatColor() + name);
        meta.setLore(getLore());
        item.setItemMeta(meta);
        return item;
    }

    public static SpellList getByClass(Class<? extends Spell> spellClass) {
        for (SpellList spell : values()) {
            if (spell.getSpellClass().equals(spellClass)) {
                return spell;
            }
        }
        return null;
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