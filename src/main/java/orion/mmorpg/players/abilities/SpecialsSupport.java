package orion.mmorpg.players.abilities;

import org.bukkit.entity.Player;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.players.OrionPlayer;

public interface SpecialsSupport {
    void getFirstSpecial(Player player, OrionPlayer orionPlayer);
    void getSecondSpecial(Player player, OrionPlayer orionPlayer);
   void getThirdSpecial(Player player, OrionPlayer orionPlayer);

    default void getItems(Player player, OrionPlayer orionPlayer) {
        if(player.getInventory().getItem(8) != null) {
            player.getEnderChest().addItem(player.getInventory().getItem(8));
            player.getInventory().setItem(8, null);
        }
        if(player.getInventory().getItem(7) != null) {
            player.getEnderChest().addItem(player.getInventory().getItem(7));
            player.getInventory().setItem(7, null);
        }
        if(player.getInventory().getItem(6) != null) {
            player.getEnderChest().addItem(player.getInventory().getItem(6));
            player.getInventory().setItem(6, null);
        }
        else {
            orionPlayer.getSlotTypes()[8] = OrionPlayer.SlotType.RESTRICTED_SLOT;
            orionPlayer.getSlotTypes()[7] = OrionPlayer.SlotType.RESTRICTED_SLOT;
            orionPlayer.getSlotTypes()[6] = OrionPlayer.SlotType.RESTRICTED_SLOT;

            player.getInventory().setItem(6, ItemList.FIRST_SPECIAL_PLACEHOLDER.getProcessedItem(1));
            player.getInventory().setItem(7, ItemList.SECOND_SPECIAL_PLACEHOLDER.getProcessedItem(1));
            player.getInventory().setItem(8, ItemList.THIRD_SPECIAL_PLACEHOLDER.getProcessedItem(1));
        }
    }

    default void removeItems(Player player, OrionPlayer orionPlayer) {
        orionPlayer.getSlotTypes()[8] = OrionPlayer.SlotType.FREE_SLOT;
        orionPlayer.getSlotTypes()[7] = OrionPlayer.SlotType.FREE_SLOT;
        orionPlayer.getSlotTypes()[6] = OrionPlayer.SlotType.FREE_SLOT;

        player.getInventory().setItem(6, null);
        player.getInventory().setItem(7, null);
        player.getInventory().setItem(8, null);
    };
}
