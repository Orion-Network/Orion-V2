package orion.mmorpg.mobs.bosses;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import orion.mmorpg.mobs.list.CryptSkeleton;
import orion.mmorpg.mobs.samples.SampleBossMob;
import orion.mmorpg.mobs.samples.SampleHostileMob;
import orion.mmorpg.mobs.samples.SampleMob;
import orion.mmorpg.utils.Stats;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.UUID;

public class Destroyer extends SampleBossMob {
    public Destroyer(String uuid, Location location) {
        super("L'Invocateur", 30, EntityTypes.O, uuid, location, new Stats.Builder().withIntel(100).withDefence(20).withVitality(1000).withSpeed(4).build());
    }

    @Override
    protected void generatePathfinder() {
        this.bP.a(0, new PathfinderGoalSummon(this, new CryptSkeleton(UUID.randomUUID().toString(), this.getBukkitEntity().getLocation())));
        this.bQ.a(1, new PathfinderGoalHurtByTarget(this, new Class[0]));
        this.bQ.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityInsentient.class, true));

    }

    private class PathfinderGoalSummon extends PathfinderGoal {
        private final SampleMob mob;
        private long lastIteration = System.currentTimeMillis();
        private SampleHostileMob[] summons;
        public PathfinderGoalSummon(SampleMob mob, SampleHostileMob... summons) {
            this.mob = mob;
            this.summons = summons;

            for(SampleHostileMob summon : summons) {
                summon.spawn();
                summon.setRespawn(false);
            }
        }

        @Override
        public boolean a() {
            boolean summonAlive = Arrays.stream(summons).anyMatch(summon -> summon.isAlive());
            return System.currentTimeMillis() - lastIteration > 60 * 1000 && !summonAlive;
        }

        @Override
        public void c() {
            Bukkit.broadcastMessage("Summoning");
            for (int i = 0; i < summons.length; i++) {
                try {
                    SampleMob copy = summons[i].getMobData().generateMob();
                    summons[i] = (SampleHostileMob) copy;
                    copy.setRespawn(false);
                    copy.spawn();
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            lastIteration = System.currentTimeMillis();
        }
    }
}
