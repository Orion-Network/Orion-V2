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
        @Getter@Setter
        private boolean isPermanent = false;

        public OEffect(String name, String description, Class<? extends Event> event, Long duration) {
            this.name = name;
            this.description = description;
            this.event = event;
            this.duration = duration + System.currentTimeMillis();
        }

        private boolean isEventValid(Event event) {
            return this.event.isInstance(event);
        }

        public boolean execute(Event event) {
            if(!isEventValid(event)) return false;
            if(!isPermanent && System.currentTimeMillis() > duration) return false;
            onEvent(event);
            return true;
        }

        public abstract void onEvent(Event event);
}
