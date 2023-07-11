package orion.mmorpg.models;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class NPCManager {

    @Getter
    public HashMap<Integer, EntityPlayer> npcEntities = new HashMap<>();

    Mmorpg plugin;
    public NPCManager(Mmorpg plugin) {
        this.plugin = plugin;

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                int entityId = packet.getIntegers().read(0);
                if(!npcEntities.containsKey(entityId)) {
                    event.setCancelled(true);
                } else {

                    EnumWrappers.EntityUseAction action = packet.getEnumEntityUseActions().read(0).getAction();
                    if(action.equals(EnumWrappers.EntityUseAction.ATTACK)) return;
                    EnumWrappers.Hand hand = packet.getEnumEntityUseActions().read(0).getHand();

                    if(hand.equals(EnumWrappers.Hand.MAIN_HAND) && action.equals(EnumWrappers.EntityUseAction.INTERACT)) {
                        Bukkit.broadcastMessage("Interact");
                    }

                }
            }
        });
    }

    public void setup(Player player) {
        EntityPlayer craftPlayer = ((CraftPlayer) player).getHandle();

        Property textures = (Property) craftPlayer.getProfile().getProperties().get("textures").toArray()[0];
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());
        gameProfile.getProperties().put("textures", new Property("textures", textures.getValue(), textures.getSignature()));

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

    public void removeNPC(Integer id) {
        EntityPlayer corpse = npcEntities.get(id);
        for(Player on : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) on).getHandle().b;

            connection.sendPacket(new PacketPlayOutEntityDestroy(corpse.getId()));
        }
        npcEntities.remove(id);
    }
}
