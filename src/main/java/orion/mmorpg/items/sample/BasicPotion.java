package orion.mmorpg.items.sample;

import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.OnGetEffect;
import orion.mmorpg.items.ExcludeRegister;
import orion.mmorpg.items.ItemType;
import orion.mmorpg.items.sample.BasicUsableItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.OEffect;
import orion.mmorpg.players.abilities.effects.Propagation;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter@ExcludeRegister
public class BasicPotion extends BasicConsumableItem {
    @Setter
    int duration;
    final ArrayList<String> effectList;

    public BasicPotion(ItemStack item) {
        super(item);
        this.duration = item.getItemMeta().getPersistentDataContainer().get(Namespaces.duration, PersistentDataType.INTEGER);
        String[] list = item.getItemMeta().getPersistentDataContainer().get(Namespaces.effectList, DataType.STRING_ARRAY);

        this.effectList = new ArrayList<>();
        Collections.addAll(effectList, list);
    }


    public BasicPotion(String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE, Rarity RARITY, int duration) {
        super(Material.POTION, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, ItemType.POTION);
        this.duration = duration;
        this.effectList = new ArrayList<>();
    }

    public void addEffect(Class<? extends OEffect> oEffect) {
        this.effectList.add(oEffect.getName());
    }

    public Class<? extends OEffect>[] getEffectsClass() {
        String[] list = getItemMeta().getPersistentDataContainer().get(Namespaces.effectList, DataType.STRING_ARRAY);
        Class[] classes = new Class[list.length];
        for (int i = 0; i < list.length; i++) {
            try {
                classes[i] = Class.forName(list[i]);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        String[] effectList = e.getItem().getItemMeta().getPersistentDataContainer().get(Namespaces.effectList, DataType.STRING_ARRAY);
        for (String effect : effectList) {
            try {
                OEffect oEffect = (OEffect) Class.forName(effect).getConstructor(long.class).newInstance(duration);
                Bukkit.getServer().getPluginManager().callEvent(new OnGetEffect(data, oEffect, oEffect.getDuration(), this.getNAME()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        super.onUseItem(mmorpg, data, e);
    }

    @Override
    public ItemStack generateItem(ItemStack item, int amount) {
        if (item == null)
            item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(Namespaces.duration, PersistentDataType.INTEGER, duration);
        meta.getPersistentDataContainer().set(Namespaces.effectList, DataType.STRING_ARRAY, effectList.toArray(new String[0]));
        item.setItemMeta(meta);
        return super.generateItem(item, amount);
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        if (lore == null)
            lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "Durée : " + this.RARITY.getChatColor() + duration + ChatColor.YELLOW + "s");
        if(effectList.isEmpty())
            lore.add(ChatColor.YELLOW + "Aucun effet");
        else {
            lore.add(ChatColor.YELLOW + "Effets : ");
            for (String effect : effectList) {
                lore.add(ChatColor.YELLOW + " - " + effect.split("\\.")[effect.split("\\.").length - 1]);
            }
        }
        return super.generateLore(lore);
    }
}
