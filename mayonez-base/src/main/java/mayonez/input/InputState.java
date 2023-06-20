package mayonez.input;

/**
 * Represents the state of a {@link mayonez.input.MappingStatus}.
 *
 * @author SlavSquatSuperstar
 */
public enum InputState {
    /**
     * The input mapping is not pressed or held.
     */
    RELEASED,
    /**
     * The input mapping is newly pressed this frame.
     */
    PRESSED,
    /**
     * The input mapping is continuously held this frame.
     */
    HELD
}
