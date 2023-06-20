package mayonez.input

/**
 * Tracks the status for a key or button across multiple frames.
 *
 * @author SlavSquatSuperstar
 */
internal class MappingStatus {

    /** If the input listener detected this mapping to be down. */
    var down: Boolean = false
    private var state: InputState = InputState.RELEASED

    // Getters and Setters

    val pressed: Boolean
        get() = state == InputState.PRESSED

    fun setPressed() {
        state = InputState.PRESSED
    }

    val held: Boolean
        get() = state == InputState.HELD

    fun setHeld() {
        state = InputState.HELD
    }

    val released: Boolean
        get() = state == InputState.RELEASED

    fun setReleased() {
        state = InputState.RELEASED
    }

}
