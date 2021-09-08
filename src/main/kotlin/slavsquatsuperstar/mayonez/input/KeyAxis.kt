package slavsquatsuperstar.mayonez.input

import org.apache.commons.lang3.StringUtils

/**
 * Stores two keys intended to perform opposite actions.
 */
// TODO store mappings?
internal enum class KeyAxis(posKey: KeyMapping, negKey: KeyMapping) {
    VERTICAL(KeyMapping.DOWN, KeyMapping.UP), HORIZONTAL(KeyMapping.RIGHT, KeyMapping.LEFT);

    private val posKey: String = posKey.name
    private val negKey: String = negKey.name

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

    override fun toString(): String = StringUtils.capitalize(name.lowercase())

}