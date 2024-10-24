package mayonez.config

import mayonez.assets.text.*
import mayonez.util.*

/**
 * Stores user preferences and a set of default values as [Record] objects
 * for later use and performs input validation.
 *
 * @param filename the location of the preferences file
 * @param defaults the default preferences
 * @author SlavSquatSuperstar
 */
open class GameConfig(
    private val filename: String, private val defaults: Record
) : Record() {

    /**
     * Sets the preferences from a .json file. Override this to change the
     * read behavior.
     */
    protected open fun readFromFile() {
        setFrom(JSONFile(filename).readJSON())
    }

    /**
     * Checks user-inputted preferences and replaces and invalid values with defaults.
     *
     * @param rules any number of rules to apply
     */
    protected fun validateUserPreferences(vararg rules: PreferenceValidator<*>) {
        rules.forEach { rule -> rule.validate(this, defaults) }
    }

    override fun toString(): String = "GameConfig ($filename) ${super.toString()}"

}