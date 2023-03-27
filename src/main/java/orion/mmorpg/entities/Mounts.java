package orion.mmorpg.entities;

import orion.mmorpg.entities.mounts.FlyingFish;
import orion.mmorpg.entities.mounts.TestWolf;
import orion.mmorpg.entities.samples.AbstractMount;

public enum Mounts {

    TEST_WOLF(TestWolf.class),
    FLYING_FISH(FlyingFish.class)
    ;

    Class<? extends AbstractMount> mountClass;

    Mounts(Class<? extends AbstractMount> mobClass) {
        this.mountClass = mobClass;
    }

    public Class<? extends AbstractMount> getMountClass() {
        return mountClass;
    }
}
