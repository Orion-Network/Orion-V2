package orion.mmorpg.entities.samples;

import lombok.Getter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.gui.MerchantGui;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Stats;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractShopkeepers extends AbstractMob {

    @Getter
    private final ArrayList<MerchantGui.Buyable> buyables = new ArrayList<>();
    public AbstractShopkeepers(String uuid, String mobName, Location spawnLocation) {
        super(EntityType.VILLAGER, uuid, mobName, spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 100).build(), 0);
    }

    @Override
    protected InteractionResult mobInteract(Player entityhuman, InteractionHand enumhand) {
        OrionPlayer player = Mmorpg.INSTANCE.getPlayerManager().getPlayer(entityhuman.getUUID());
        MerchantGui gui = new MerchantGui("Shop", player, (org.bukkit.entity.Player) entityhuman.getBukkitEntity(),  buyables);
        gui.show(entityhuman.getBukkitEntity());
        return super.mobInteract(entityhuman, enumhand);
    }
}
