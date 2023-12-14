package orion.mmorpg.players.abilities;

import lombok.Getter;
import orion.mmorpg.players.abilities.supports.*;

//Change to have a list of Effects added to the player
@Getter
public enum SupportList {
    THE_FOOL(TheFool.class),
    ;

    private Class<? extends Support> support;

    SupportList(Class<? extends Support> support) {
        this.support = support;
    }

}
