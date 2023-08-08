package mayonez.input

import mayonez.util.*

/**
 * Stores two keys intended to perform opposite actions and produces a value based on which are pressed.
 */
// TODO allow storing input mappings
// TODO allow adding custom axes
enum class KeyAxis(
    /**
     * The negative key of this axis (down, left, counterclockwise, etc.).
     */
    private val negKey: Key,
    /**
     * The positive key of this axis (up, right, clockwise, etc.).
     */
    private val posKey: Key
) {
    // Letter Keys
    VERTICAL(Key.S, Key.W),
    HORIZONTAL(Key.A, Key.D),
    HORIZONTAL2(Key.Q, Key.E),

    // Arrow Keys
    ARROWS_VERTICAL(Key.DOWN, Key.UP),
    ARROWS_HORIZONTAL(Key.LEFT, Key.RIGHT),

    // Symbols
    BRACKETS(Key.LEFT_BRACKET, Key.RIGHT_BRACKET),
    PLUS_MINUS(Key.MINUS, Key.EQUALS);

    /**
     * Get the value of this axis, 1 of the positive key is pressed, -1 if the negative key is pressed,
     * and 0 if both or neither key is pressed.
     *
     * @return the axis value, -1, 0, or 1
     */
    internal fun value(): Int {
        val negVal = if (KeyInput.keyDown(negKey)) -1 else 0
        val posVal = if (KeyInput.keyDown(posKey)) 1 else 0
        return negVal + posVal
    }

    override fun toString(): String = StringUtils.capitalizeAllWords(name.replace('_', ' '))

}