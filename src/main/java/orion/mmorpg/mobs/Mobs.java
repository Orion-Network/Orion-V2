package orion.mmorpg.mobs;

import orion.mmorpg.mobs.list.*;
import orion.mmorpg.mobs.samples.SampleMob;

public enum Mobs {
    ANGRY_CHICKEN(AngryChicken.class),
    CALM_SHEEP(CalmSheep.class),
    SHEEP(Sheep.class),
    MOUNTAIN_ZOMBIE(MountainZombie.class),
    CRYPT_SKELETON(CryptSkeleton.class),
    ;

    Class<? extends SampleMob> mobClass;

    Mobs(Class<? extends SampleMob> mobClass) {
        this.mobClass = mobClass;
    }

    public Class getMobClass() {
        return mobClass;
    }
}
