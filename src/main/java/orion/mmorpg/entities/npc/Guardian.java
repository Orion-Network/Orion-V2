package orion.mmorpg.entities.npc;

import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.EnumHand;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalArrowAttack;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.monster.IRangedEntity;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.projectile.ProjectileHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.event.CraftEventFactory;
import org.bukkit.event.entity.EntityShootBowEvent;
import orion.mmorpg.entities.EntityType;
import orion.mmorpg.entities.pathfinders.MoveTowardsTargetIfRange;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.entities.samples.AbstractMob;
import orion.mmorpg.utils.Stats;

public class Guardian extends AbstractMob implements IRangedEntity {

    public Guardian(String uuid, Location spawnLocation) {
        super(EntityTypes.P, uuid, "Garde", spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 100000).setBase(Stats.StatsInfo.STRENGTH,1000).setBase(Stats.StatsInfo.SPEED, 4).build(), 99);
    }

    @Override
    protected void generatePathfinder() {
        this.bP.a(1, new PathfinderGoalArrowAttack(this, 1.25D, 20, 10.0F));
        this.bP.a(2, new MoveTowardsTargetIfRange(this, getStats().get(Stats.StatsInfo.SPEED)/10, 10, this.getSpawnLocation()));

        this.bQ.a(1, new PathfinderGoalNearestAttackableTarget(this, AbstractMob.class, 10, true, false, (entityliving) -> entityliving instanceof AbstractHostile));
        this.bQ.a(2, new PathfinderGoalHurtByTarget(this, new Class[0]));
    }

    @Override
    public void a(EntityLiving entityLiving, float v) {
        ItemStack itemstack = this.h(this.b(ProjectileHelper.a(this, Items.mg)));
        EntityArrow entityarrow = this.b(itemstack, f);
        entityarrow.setDamage(entityarrow.getDamage() + this.getStats().get(Stats.StatsInfo.STRENGTH));
        double d0 = entityLiving.locX() - this.locX();
        double d1 = entityLiving.e(0.3333333333333333D) - entityarrow.locY();
        double d2 = entityLiving.locZ() - this.locZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        entityarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.t.getDifficulty().a() * 4));
        EntityShootBowEvent event = CraftEventFactory.callEntityShootBowEvent(this, this.getItemInMainHand(), (ItemStack)null, entityarrow, EnumHand.a, 0.8F, true);
        if (event.isCancelled()) {
            event.getProjectile().remove();
        } else {
            if (event.getProjectile() == entityarrow.getBukkitEntity()) {
                this.t.addEntity(entityarrow);
            }

            this.playSound(SoundEffects.qW, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        }
    }

    protected EntityArrow b(ItemStack itemstack, float f) {
        return ProjectileHelper.a(this, itemstack, f);
    }
}
