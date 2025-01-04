package mayonez.input;

/**
 * Represents the state of an input mapping (key or button).
 *
 * @author SlavSquatSuperstar
 */
public enum InputState {

    /**
     * The input mapping is not held this frame, the default state.
     */
    NONE {
        @Override
        public InputState getNextState(boolean down) {
            return down ? PRESSED : NONE;
        }
    },
    /**
     * The input mapping was newly pressed this frame.
     */
    PRESSED {
        @Override
        public InputState getNextState(boolean down) {
            return down ? HELD : RELEASED;
        }
    },
    /**
     * The input mapping is continuously held this frame.
     */
    HELD {
        @Override
        public InputState getNextState(boolean down) {
            return down ? HELD : RELEASED;
        }
    },
    /**
     * The input mapping was newly released this frame.
     */
    RELEASED {
        @Override
        public InputState getNextState(boolean down) {
            return down ? PRESSED : NONE;
        }
    };

    /**
     * Transition to the next input state based on this frame's actions.
     *
     * @param down if the input mapping is down
     * @return the next state
     */
    public abstract InputState getNextState(boolean down);

}
