package slavsquatsuperstar.demos.spacegame.events;

/**
 * Indicates the player has toggled the spaceship auto-brake functionality.
 *
 * @author SlavSquatSuperstar
 */
public class AutoBrakeToggleEvent extends SpaceGameEvent {

    private final boolean enabled;

    public AutoBrakeToggleEvent(boolean enabled) {
        super("Auto-brake enabled: " + enabled);
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
