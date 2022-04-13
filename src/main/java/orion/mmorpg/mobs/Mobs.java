package orion.mmorpg.mobs;

import orion.mmorpg.mobs.list.AngryChicken;
import orion.mmorpg.mobs.list.Vachette;
import orion.mmorpg.mobs.samples.SampleMob;

public enum Mobs {
    ANGRY_CHICKEN(AngryChicken.class),
    VACHETTE(Vachette.class),
    ;

    Class<? extends SampleMob> mobClass;

    Mobs(Class<? extends SampleMob> mobClass) {
        this.mobClass = mobClass;
    }

    public Class getMobClass() {
        return mobClass;
    }
}
