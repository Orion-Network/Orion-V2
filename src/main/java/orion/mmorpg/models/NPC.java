package orion.mmorpg.models;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class NPC extends EntityPlayer {

    public static HashMap<Integer, EntityPlayer> npcEntities = new HashMap<>();

    public NPC(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile) {
        super(minecraftserver, worldserver, gameprofile);

    }

    public static void setup(Player player) {
        EntityPlayer craftPlayer = ((CraftPlayer) player).getHandle();

        Property tectures = (Property) craftPlayer.getProfile().getProperties().get("textures").toArray()[0];
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());
        gameProfile.getProperties().put("textures", new Property("textures", tectures.getValue(), tectures.getSignature()));

        EntityPlayer corpse = new EntityPlayer(
                ((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) player.getWorld()).getHandle(),
                gameProfile
        );

        corpse.setPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

        for(Player on : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) on).getHandle().b;

            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, corpse));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(corpse));
        }

        npcEntities.put(corpse.getId(), corpse);
    }
}
