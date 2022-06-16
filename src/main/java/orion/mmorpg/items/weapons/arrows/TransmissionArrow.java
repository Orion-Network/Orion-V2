package orion.mmorpg.items.weapons.arrows;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArrow;
import orion.mmorpg.players.abilities.spells.InstantTransmission;
import orion.mmorpg.utils.Rarity;

public class TransmissionArrow extends BasicArrow {
    public TransmissionArrow() {
        super(Material.TIPPED_ARROW, "Flèche de Transmission", "TRANSMISSION_ARROW", 1, null, Rarity.LEGENDARY, 7);
        this.spell = new InstantTransmission();
    }


}
