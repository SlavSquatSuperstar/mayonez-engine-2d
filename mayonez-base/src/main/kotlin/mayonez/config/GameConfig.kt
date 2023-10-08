package mayonez.config

import mayonez.io.text.*
import mayonez.util.*

/**
 * Reads user preferences from a file into a [Record].
 *
 * @param filename the location of the preferences file
 * @param defaults the default preferences
 * @author SlavSquatSuperstar
 */
open class GameConfig(
    private val filename: String, private val defaults: Record
): Record() {

    /**
     * Sets the configuration from a .json file. Override this to change the
     * read behavior.
     */
    protected open fun readFromFile() {
        setFrom(JSONFile(filename).readJSON())
    }

    /**
     * Checks user preferences and replaces and invalid values with the default
     * value.
     *
     * @param rules any number of rules to apply
     */
    protected fun validateUserPreferences(vararg rules: PreferenceValidator<*>) {
        rules.forEach { rule -> rule.validate(this, defaults) }
    }

    override fun toString(): String = "GameConfig ($filename)"

}