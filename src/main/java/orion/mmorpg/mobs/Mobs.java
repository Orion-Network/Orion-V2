package orion.mmorpg.mobs;

import orion.mmorpg.mobs.bosses.Destroyer;
import orion.mmorpg.mobs.list.*;
import orion.mmorpg.mobs.pets.FunnyWolf;
import orion.mmorpg.mobs.samples.SampleMob;

public enum Mobs {
    //Sample Mobs
    ANGRY_CHICKEN(AngryChicken.class),
    CALM_SHEEP(CalmSheep.class),
    SHEEP(Sheep.class),
    MOUNTAIN_ZOMBIE(MountainZombie.class),
    CRYPT_SKELETON(CryptSkeleton.class),
    GUARDIAN(Guardian.class),
    NORDIC_WOLF(NordicWolf.class),

    //Pet Mobs
    FUNNY_WOLF(FunnyWolf.class),

    //Bosses
    DESTROYER(Destroyer.class),
    ;

    Class<? extends SampleMob> mobClass;

    Mobs(Class<? extends SampleMob> mobClass) {
        this.mobClass = mobClass;
    }

    public Class getMobClass() {
        return mobClass;
    }
}
