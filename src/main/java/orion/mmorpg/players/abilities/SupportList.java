package orion.mmorpg.players.abilities;

import orion.mmorpg.players.abilities.supports.*;

public enum SupportList {
    HAKI(Haki.class),
    PROJECT_SOUL(ProjectSoul.class),
    SOLEIL(Soleil.class),
    SUCCION_SANGUIN(SuccionSanguine.class),
    THE_FOOL(TheFool.class),
    ;

    private Class<? extends Support> support;

    SupportList(Class<? extends Support> support) {
        this.support = support;
    }

    public Class<? extends Support> getSupport() {
        return support;
    }
}
