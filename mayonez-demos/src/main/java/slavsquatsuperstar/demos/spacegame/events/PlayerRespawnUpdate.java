package slavsquatsuperstar.demos.spacegame.events;

import mayonez.event.*;

/**
 * Indicates how long the player has until respawning.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerRespawnUpdate extends Event {

    private final float respawnPercent;

    public PlayerRespawnUpdate(float respawnPercent) {
        super("Respawn progress: %.2f%%".formatted(respawnPercent * 100f));
        this.respawnPercent = respawnPercent;
    }

    public float getRespawnPercent() {
        return respawnPercent;
    }

}
