package mayonez.input

import mayonez.util.*

/**
 * Default key axes available in the program.
 *
 * @author SlavSquatSuperstar
 */
enum class DefaultKeyAxis(negKey: Key, posKey: Key) : InputAxis {

    // Letter Keys
    VERTICAL(Key.S, Key.W),
    HORIZONTAL(Key.A, Key.D),
    HORIZONTAL2(Key.Q, Key.E),

    // Arrow Keys
    ARROWS_VERTICAL(Key.DOWN, Key.UP),
    ARROWS_HORIZONTAL(Key.LEFT, Key.RIGHT);

    companion object {
        @JvmStatic
        fun findWithName(axisName: String): DefaultKeyAxis? {
            return StringUtils.findWithName(entries, axisName)
        }
    }

    private val axis: KeyAxis = KeyAxis(negKey, posKey)

    override fun value(): Int = axis.value()

    override fun toString(): String {
        return StringUtils.capitalizeAllWords(name.replace('_', ' '))
    }

}