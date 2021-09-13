package orion.mmorpg.players.abilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.list.TestSpell;
import orion.mmorpg.utils.Rarity;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public enum SpellList {
    TEST_SPELL(TestSpell.class, "Test spell", Rarity.UNOBTAINABLE, Material.PAPER, Arrays.asList("test"));

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

    public ItemStack getItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(rarity.getChatColor() + name);
        meta.setLore(getLore());
        item.setItemMeta(meta);
        return item;
    }

    public static void learnSpell(OrionPlayer player, String internal_id) {
        try {
            player.getSpells().put(internal_id, (Spell) SpellList.valueOf(internal_id).spellClass.getConstructors()[0].newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
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