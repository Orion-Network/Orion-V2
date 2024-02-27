package orion.mmorpg.players.abilities;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerFishEvent;

public abstract class OEffect {

    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private Class<? extends Event> event;
    @Getter
    private Long duration;
    @Getter
    private final boolean isPermanent;

    /**
     * Constructor for effects
     *
     * @param name
     *            The name of the effect
     * @param description
     *            The description of the effect
     * @param event
     *            The event that triggers the effect
     * @param duration
     *            The duration of the effect in seconds, -1 for permanent
     */
    public OEffect(String name, String description, Class<? extends Event> event, Long duration) {
        this.name = name;
        this.description = description;
        this.event = event;
        this.duration = (duration * 1000) + System.currentTimeMillis();
        this.isPermanent = (duration == -1);
    }

    private boolean isEventValid(Event event) {
        return this.event.isInstance(event);
    }

    /**
     * Returns true if the effect has been executed else false
     */
    public boolean execute(Event event) {
        System.out.println(duration + " " + (System.currentTimeMillis() > duration));
        if (!isEventValid(event))
            return false;
        if (!isPermanent && System.currentTimeMillis() > duration)
            return false;
        onEvent(event);
        return true;
    }

    public abstract void onEvent(Event event);
}
