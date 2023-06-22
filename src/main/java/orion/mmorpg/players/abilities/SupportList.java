package orion.mmorpg.players.abilities;

import orion.mmorpg.players.abilities.supports.*;

//Change to have a list of Effects added to the player
public enum SupportList {
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
