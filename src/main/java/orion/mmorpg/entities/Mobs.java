package orion.mmorpg.entities;

import org.bukkit.Location;
import orion.mmorpg.entities.bosses.Invocator;
import orion.mmorpg.entities.mobs.*;
import orion.mmorpg.entities.mobs.sea.SeaZombie;
import orion.mmorpg.entities.npc.Guardian;
import orion.mmorpg.entities.samples.AbstractMob;

import java.lang.reflect.InvocationTargetException;

//TODO Try to divide enums for mobs pets and mounts, then add speed to mount enum
public enum Mobs implements MobOperation {
    //Sample Mobs
    ANGRY_CHICKEN(AngryChicken.class),
    CALM_SHEEP(CalmSheep.class),
    SHEEP(Sheep.class),
    SCARECROW(Scarecrow.class),
    KINDLETRONSR(Kindletronsr.class),
    MOUNTAIN_ZOMBIE(MountainZombie.class),
    CRYPT_SKELETON(CryptSkeleton.class),
    GUARDIAN(Guardian.class),
    NORDIC_WOLF(NordicWolf.class),
    WHITE_WALKER(WhiteWalker.class),
    //Bosses
    INVOCATOR(Invocator.class),
    //Sea Crature
    SEA_ZOMBIE(SeaZombie.class),
    ;

    Class<? extends AbstractMob> mobClass;

    Mobs(Class<? extends AbstractMob> mobClass) {
        this.mobClass = mobClass;
    }
    @Override
    public Class<? extends AbstractMob> getMobClass() {
        return mobClass;
    }

    public AbstractMob getMob(String UUID, Location location) {
        try {
            return this.getMobClass().getDeclaredConstructor(String.class, Location.class).newInstance(UUID, location);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
