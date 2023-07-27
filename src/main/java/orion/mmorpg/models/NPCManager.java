package orion.mmorpg.models;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import orion.mmorpg.Mmorpg;

import java.util.HashMap;
import java.util.UUID;

public class NPCManager {

    @Getter
    public HashMap<Integer, ServerPlayer> npcEntities = new HashMap<>();

    Mmorpg plugin;
    public NPCManager(Mmorpg plugin) {
        this.plugin = plugin;

        /*
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
        });*/
    }

    public void setup(Player player) {
        ServerPlayer craftPlayer = ((CraftPlayer) player).getHandle();

        Property textures = (Property) craftPlayer.getGameProfile().getProperties().get("textures").toArray()[0];
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());
        gameProfile.getProperties().put("textures", new Property("textures", textures.getValue(), textures.getSignature()));
        ServerLevel world = ((CraftWorld) player.getWorld()).getHandle();

        NPC corpse = new NPC(
                ((CraftServer) Bukkit.getServer()).getServer(),
                world,
                gameProfile
        );

        corpse.setPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

        for(Player on : Bukkit.getOnlinePlayers()) {
            ServerPlayerConnection connection = ((CraftPlayer) on).getHandle().connection;

            //connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, corpse));
            connection.send(new ClientboundAddEntityPacket(corpse));
        }

        npcEntities.put(corpse.getId(), corpse);

        corpse.getLevel().addFreshEntity(corpse, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    public void removeNPC(Integer id) {
        ServerPlayer corpse = npcEntities.get(id);
        for(Player on : Bukkit.getOnlinePlayers()) {
            ServerPlayerConnection connection = ((CraftPlayer) on).getHandle().connection;

            connection.send(new ClientboundRemoveEntitiesPacket(corpse.getId()));
        }
        npcEntities.remove(id);
    }
}
