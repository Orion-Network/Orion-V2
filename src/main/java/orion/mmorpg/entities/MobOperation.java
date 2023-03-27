package orion.mmorpg.entities;

import orion.mmorpg.entities.pets.FunFunGu;
import orion.mmorpg.entities.pets.FunnyWolf;
import orion.mmorpg.entities.samples.AbstractMob;
import orion.mmorpg.entities.samples.AbstractPet;

public interface MobOperation {
    Class<? extends AbstractMob> getMobClass();

}
