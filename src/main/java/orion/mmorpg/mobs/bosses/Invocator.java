package orion.mmorpg.mobs.bosses;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.DragonFireball;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.mobs.list.CryptSkeleton;
import orion.mmorpg.mobs.samples.SampleBossMob;
import orion.mmorpg.mobs.samples.SampleHostileMob;
import orion.mmorpg.mobs.samples.SampleMob;
import orion.mmorpg.players.abilities.LocationSpell;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.spells.RepulsionSpell;
import orion.mmorpg.utils.Stats;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.UUID;

public class Invocator extends SampleBossMob {
    public Invocator(String uuid, Location location) {
        super("L'Invocateur", 30, EntityTypes.O, uuid, location, new Stats.Builder().withIntel(100).withDefence(20).withVitality(1000).withSpeed(4).build());
    }
    PathfinderGoalSummon pathfinderGoalSummon;
    @Override
    protected void generatePathfinder() {
         pathfinderGoalSummon = new PathfinderGoalSummon(this,
                new CryptSkeleton(UUID.randomUUID().toString(), this.getBukkitEntity().getLocation().add(5,0,0)),
                new CryptSkeleton(UUID.randomUUID().toString(), this.getBukkitEntity().getLocation().add(-5,0,0)),
                new CryptSkeleton(UUID.randomUUID().toString(), this.getBukkitEntity().getLocation().add(0,0,5)),
                new CryptSkeleton(UUID.randomUUID().toString(), this.getBukkitEntity().getLocation().add(0,0,-5)),
                new CryptSkeleton(UUID.randomUUID().toString(), this.getBukkitEntity().getLocation().add(4,0,4)),
                new CryptSkeleton(UUID.randomUUID().toString(), this.getBukkitEntity().getLocation().add(-4,0,4)),
                new CryptSkeleton(UUID.randomUUID().toString(), this.getBukkitEntity().getLocation().add(4,0,-4)),
                new CryptSkeleton(UUID.randomUUID().toString(), this.getBukkitEntity().getLocation().add(-4,0,-4))

        );
         this.bP.a(0,new PathfinderGoalFireballAttack(this));
        this.bP.a(1, pathfinderGoalSummon);
        this.bP.a(2, new PathfinderGoalCastSpell(new RepulsionSpell(), this.getBukkitEntity().getLocation(), 120));
        this.bQ.a(1, new PathfinderGoalHurtByTarget(this));
        this.bQ.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));

    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
        for(SampleMob mob : pathfinderGoalSummon.getSummons())
            mob.die();
    }

    private class PathfinderGoalFireballAttack extends PathfinderGoal { // TODO: Add delay between each fireball
        private final EntityInsentient invocator;
        private long lastFireball = System.currentTimeMillis();
        public PathfinderGoalFireballAttack(EntityInsentient invocator) {
            this.invocator = invocator;
        }

        @Override
        public boolean a() {
            return this.invocator.getGoalTarget() != null && this.invocator.getGoalTarget().isAlive() && System.currentTimeMillis() - lastFireball > 1000;
        }

        @Override
        public void e() {
            EntityLiving target = this.invocator.getGoalTarget();
            if( (this.invocator.e(target) < 32) && this.invocator.hasLineOfSight(target)) {
                DragonFireball fireball =  target.getBukkitEntity().getWorld().spawn(invocator.getBukkitEntity().getLocation(), DragonFireball.class);
                fireball.setDirection(target.getBukkitEntity().getLocation().subtract(invocator.getBukkitEntity().getLocation()).toVector().normalize());

            }
            lastFireball = System.currentTimeMillis();
        }
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

        public SampleHostileMob[] getSummons() {
            return summons;
        }
    }

    private class PathfinderGoalCastSpell extends PathfinderGoal {
        private Spell spell;
        private Location location;
        private long delay;
        private long lastIteration;
        public PathfinderGoalCastSpell(Spell spell, Location location, long delay) {
            this.spell = spell;
            this.location = location;
            this.delay = delay;
            this.lastIteration = System.currentTimeMillis() + delay * 1000;
        }

        @Override
        public boolean a() {
            return System.currentTimeMillis() - lastIteration > 60 * 1000;
        }

        @Override
        public void c() {
            ((LocationSpell)spell).launchFromLocation(Mmorpg.INSTANCE, null, null, location);
            lastIteration = System.currentTimeMillis();
        }
    }
}
