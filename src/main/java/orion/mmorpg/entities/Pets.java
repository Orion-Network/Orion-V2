package orion.mmorpg.entities;

import orion.mmorpg.entities.pets.FunFunGu;
import orion.mmorpg.entities.pets.FunnyWolf;
import orion.mmorpg.entities.samples.AbstractPet;

public enum Pets {
    FUNFUNGU(FunFunGu.class),
    FUNNYWOLF(FunnyWolf.class);

    Class<? extends AbstractPet> petClass;

    Pets(Class<? extends AbstractPet> petClass) {
        this.petClass = petClass;
    }

    public Class<? extends AbstractPet> getPetClass() {
        return petClass;
    }
}
