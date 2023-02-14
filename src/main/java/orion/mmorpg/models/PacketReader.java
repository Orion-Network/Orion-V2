package orion.mmorpg.models;


import com.comphenix.protocol.injector.packet.PacketInjector;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.RightClickNpcEvent;

import java.lang.reflect.Field;
import java.util.List;

public class PacketReader {
    private final Player player;
    private int count = 0;

    public PacketReader(Player player) {
        this.player = player;
    }

    public boolean inject() {
        CraftPlayer nmsPlayer = (CraftPlayer) player;
        Channel channel = nmsPlayer.getHandle().b.a.k;

        if(channel.pipeline().get("PacketInjector") != null) {
            return false;
        }

        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<PacketPlayInUseEntity>() {

            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, PacketPlayInUseEntity packetPlayInUseEntity, List<Object> list) throws Exception {
                list.add(packetPlayInUseEntity);
                read(packetPlayInUseEntity);
            }
        });
        return true;
    }

    private void read(PacketPlayInUseEntity packet) {
        count++;
        System.out.println("Packet count: " + count);
        if(count == 4) {
            count = 0;
            int entityId = (int) getValue(packet, "a");
            if(!NPC.npcEntities.containsKey(entityId))
                return;
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getPluginManager().callEvent(new RightClickNpcEvent(player, NPC.npcEntities.get(entityId)));
                }
            }.runTaskLater(Mmorpg.INSTANCE, 20);
        }
    }

    private Object getValue(Object instance, String name) {
        Object result = null;
        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
