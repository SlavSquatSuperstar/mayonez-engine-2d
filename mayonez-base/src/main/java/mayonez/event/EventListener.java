package mayonez.event;

import mayonez.annotations.ExperimentalFeature;

/**
 * An object that receives and reacts to events.
 *
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
@FunctionalInterface
// TODO parameterize listener and nodes
public interface EventListener {
    void onEvent(Event event);
}
