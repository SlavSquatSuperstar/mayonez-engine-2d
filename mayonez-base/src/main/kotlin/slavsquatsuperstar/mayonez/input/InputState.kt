package slavsquatsuperstar.mayonez.input

/**
 * What state a key or mouse button is in.
 */
internal enum class InputState {

    /**
     * If this input is inactive in the current frame.
     */
    RELEASED,

    /**
     * If this input was just active in the current frame.
     */
    PRESSED,

    /**
     * If this input has been continuously active over many frames.
     */
    HELD

}