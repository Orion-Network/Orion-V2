package orion.mmorpg.mobs.bosses;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalAvoidTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.DragonFireball;
import org.bukkit.metadata.FixedMetadataValue;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.misc.MythicalScroll;
import orion.mmorpg.mobs.list.CryptSkeleton;
import orion.mmorpg.mobs.samples.SampleBossMob;
import orion.mmorpg.mobs.samples.SampleHostileMob;
import orion.mmorpg.mobs.samples.SampleMob;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.spells.RepulsionSpell;
import orion.mmorpg.utils.Knowledge;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.UUID;

public class Invocator extends SampleBossMob {
    public Invocator(String uuid, Location location) {
        super("L'Invocateur", 30, EntityTypes.O, uuid, location, new Stats.Builder().withIntel(100).withDefence(20).withVitality(1000).withSpeed(4).build());
        this.getRewards().add(new Rewards.Reward(((MythicalScroll) ItemList.MYTHICAL_SCROLL.getItem()).generateScroll("HAKI"), 25));
        this.getRewards().add(new Rewards.Reward(((MythicalScroll) ItemList.MYTHICAL_SCROLL.getItem()).generateScroll("HAKI"), 1));
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
         this.bP.a(0,new PathfinderGoalFireballAttack(this, this.getStats().getIntel()));
        this.bP.a(1, pathfinderGoalSummon);
        this.bP.a(2, new PathfinderGoalCastSpell(new RepulsionSpell(), this.getBukkitEntity().getLocation(), 120));
        this.bP.a(3, new PathfinderGoalAvoidTarget<>(this, EntityHuman.class, 3.0F, 0.8D, 0.33D));
        this.bQ.a(1, new PathfinderGoalHurtByTarget(this));
        this.bQ.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));

    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
        for(SampleMob mob : pathfinderGoalSummon.getSummons())
            mob.die();
    }

    private class PathfinderGoalFireballAttack extends PathfinderGoal {
        private final EntityInsentient invocator;
        private final float damage;
        private long lastFireball = System.currentTimeMillis();
        public PathfinderGoalFireballAttack(EntityInsentient invocator, float damage) {
            this.invocator = invocator;
            this.damage = damage;
        }

        @Override
        public boolean a() {
            return this.invocator.getGoalTarget() != null && this.invocator.getGoalTarget().isAlive() && System.currentTimeMillis() - lastFireball > 3000;
        }

        @Override
        public void e() {
            EntityLiving target = this.invocator.getGoalTarget();
            if( (this.invocator.e(target) < 32) && this.invocator.hasLineOfSight(target)) {
                DragonFireball fireball =  target.getBukkitEntity().getWorld().spawn(invocator.getBukkitEntity().getLocation().add(0, 2.5, 0), DragonFireball.class);
                fireball.setMetadata("damage", new FixedMetadataValue(Mmorpg.INSTANCE, damage));
                fireball.setDirection(target.getBukkitEntity().getLocation().subtract(fireball.getLocation()).toVector().normalize());

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
            return System.currentTimeMillis() - lastIteration > 300 * 1000 && !summonAlive;
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
            spell.launch(Mmorpg.INSTANCE, location, new Knowledge(200));
            lastIteration = System.currentTimeMillis();
        }
    }
}
