package mayonez.input

import mayonez.util.StringUtils

/**
 * Stores two keys intended to perform opposite actions and produces a value based on which are pressed.
 */
// TODO store mappings?
internal enum class KeyAxis(
    /**
     * The negative key of this axis (down, left, counterclockwise, etc.).
     */
    private val negKey: Key,
    /**
     * The positive key of this axis (up, right, clockwise, etc.).
     */
    private val posKey: Key
) {
    VERTICAL(Key.S, Key.W), HORIZONTAL(Key.A, Key.D), HORIZONTAL2(Key.Q, Key.E),
    ARROWS_VERTICAL(Key.DOWN, Key.UP), ARROWS_HORIZONTAL(Key.LEFT, Key.RIGHT),
    BRACKETS(Key.LEFT_BRACKET, Key.RIGHT_BRACKET), PLUS_MINUS(Key.MINUS, Key.EQUALS);

    /**
     * @return The value of this axis. 1 if the positive key is pressed. -1 if the negative key is pressed.
     * 0 if the both or neither key is pressed.
     */
    internal fun value(): Int {
        // add neg and pos keys to make sure pressing a new key doesn't override the other
        val negVal = if (KeyInput.keyDown(negKey)) -1 else 0
        val posVal = if (KeyInput.keyDown(posKey)) 1 else 0
        return negVal + posVal
    }

    override fun toString(): String = StringUtils.capitalizeWords(name.replace('_', ' '))

}