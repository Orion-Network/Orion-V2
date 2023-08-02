package orion.mmorpg.entities.bosses;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.DragonFireball;
import org.bukkit.metadata.FixedMetadataValue;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.mobs.CryptSkeleton;
import orion.mmorpg.entities.samples.AbstractBoss;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.entities.samples.AbstractMob;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.spells.Repulsion;
import orion.mmorpg.utils.Knowledge;
import orion.mmorpg.utils.Stats;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.UUID;

public class Invocator extends AbstractBoss {
    public Invocator(String uuid,Location spawnLocation) {
        super(EntityType.EVOKER, uuid, "L'Invocateur", spawnLocation,  new Stats.Builder().setBase(Stats.StatsInfo.INTELLIGENCE, 100)
                .setBase(Stats.StatsInfo.DEFENCE, 20)
                .setBase(Stats.StatsInfo.VITALITY, 1000)
                .setBase(Stats.StatsInfo.SPEED, 4).build(), 30);
    }

    PathfinderGoalSummon pathfinderGoalSummon;
    @Override
    protected void generatePathfinder() {
         pathfinderGoalSummon = new PathfinderGoalSummon(this,
              generateSummon(5,0),
              generateSummon(-5,0),
              generateSummon(0,5),
              generateSummon(0,-5), generateSummon(4,4),
              generateSummon(-4,4),
              generateSummon(4,-4),
              generateSummon(-4,-4)
        );
         this.goalSelector.addGoal(0,new PathfinderGoalFireballAttack(this, this.getStats().get(Stats.StatsInfo.INTELLIGENCE)));
        this.goalSelector.addGoal(1, pathfinderGoalSummon);
        this.goalSelector.addGoal(2, new PathfinderGoalCastSpell(new Repulsion(), this.getBukkitEntity().getLocation(), 120));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 3.0F, 0.8D, 0.60D));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));

    }

    private CryptSkeleton generateSummon(int x, int y) {
        CryptSkeleton skeleton = new CryptSkeleton(UUID.randomUUID().toString(), this.getBukkitEntity().getLocation().add(x,0,y));
        skeleton.setSummon(true);
        return skeleton;
    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
        for(AbstractMob mob : pathfinderGoalSummon.getSummons())
            mob.remove(RemovalReason.DISCARDED);
    }

    private class PathfinderGoalFireballAttack extends Goal {
        private final PathfinderMob invocator;
        private final float damage;
        private long lastFireball = System.currentTimeMillis();
        public PathfinderGoalFireballAttack(PathfinderMob invocator, float damage) {
            this.invocator = invocator;
            this.damage = damage;
        }

        @Override
        public boolean canUse() {
            return this.invocator.getTarget() != null && this.invocator.getTarget().isAlive() && System.currentTimeMillis() - lastFireball > 3000;
        }

        @Override
        public void start() {
            LivingEntity target = this.invocator.getTarget();
            if( (this.invocator.distanceToSqr(target) < 32) && this.invocator.hasLineOfSight(target)) {
                DragonFireball fireball =  target.getBukkitEntity().getWorld().spawn(invocator.getBukkitEntity().getLocation().add(0, 2.5, 0), DragonFireball.class);
                fireball.setMetadata("damage", new FixedMetadataValue(Mmorpg.INSTANCE, damage));
                fireball.setDirection(target.getBukkitEntity().getLocation().subtract(fireball.getLocation()).toVector().normalize());

            }
            lastFireball = System.currentTimeMillis();
        }
    }

    private class PathfinderGoalSummon extends Goal {
        private final AbstractMob mob;
        private long lastIteration = System.currentTimeMillis();
        private AbstractHostile[] summons;
        public PathfinderGoalSummon(AbstractMob mob, AbstractHostile... summons) {
            this.mob = mob;
            this.summons = summons;

            for(AbstractHostile summon : summons) {
                summon.spawn();
                summon.setRespawn(false);
            }
        }

        @Override
        public boolean canUse() {
            boolean summonAlive = Arrays.stream(summons).anyMatch(summon -> summon.isAlive());
            return System.currentTimeMillis() - lastIteration > 300 * 1000 && !summonAlive;
        }

        @Override
        public void start() {

            Bukkit.broadcastMessage("Summoning");
            for (int i = 0; i < summons.length; i++) {
                try {
                    AbstractMob copy = summons[i].getMobData().generateMob();
                    summons[i] = (AbstractHostile) copy;
                    copy.setRespawn(false);
                    copy.spawn();
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            lastIteration = System.currentTimeMillis();
        }

        public AbstractHostile[] getSummons() {
            return summons;
        }
    }

    private class PathfinderGoalCastSpell extends Goal {
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
        public boolean canUse() {
            return System.currentTimeMillis() - lastIteration > 60 * 1000;
        }

        @Override
        public void start() {
            spell.launch(Mmorpg.INSTANCE, location, new Knowledge(200));
            lastIteration = System.currentTimeMillis();
        }
    }
}
