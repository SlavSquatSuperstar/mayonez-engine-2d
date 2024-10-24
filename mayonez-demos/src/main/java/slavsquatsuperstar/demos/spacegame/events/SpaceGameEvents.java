package slavsquatsuperstar.demos.spacegame.events;

import mayonez.event.*;

/**
 * Informs the game of different events that occur.
 *
 * @author SlavSquatSuperstar
 */
public final class SpaceGameEvents {

    private static final EventSystem<Event> PLAYER_EVENTS = new EventSystem<>();

    private SpaceGameEvents() {
    }

    public static EventSystem<Event> getPlayerEventSystem() {
        return PLAYER_EVENTS;
    }
}
