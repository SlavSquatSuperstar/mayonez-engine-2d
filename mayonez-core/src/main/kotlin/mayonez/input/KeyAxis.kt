package mayonez.input

/**
 * Stores two keys that perform opposite actions and produces a value based on which are pressed.
 *
 * @author SlavSquatSuperstar
 */
// TODO allow storing input mappings
// TODO allow adding custom axes
data class KeyAxis(
    /**
     * The negative key of this axis (down, left, counterclockwise, etc.).
     */
    private val negKey: Key?,
    /**
     * The positive key of this axis (up, right, clockwise, etc.).
     */
    private val posKey: Key?
): InputAxis {

    /**
     * Get the value of this axis, 1 if the positive key is held, -1 if the
     * negative key is held, and 0 if neither key is held.
     *
     * @return the axis value, 1, -1, or 0
     */
    override fun value(): Int {
        val negVal = if (KeyInput.keyDown(negKey)) -1 else 0
        val posVal = if (KeyInput.keyDown(posKey)) 1 else 0
        return negVal + posVal
    }

    override fun toString(): String =  "KeyAxis ($negKey, $posKey)"

}