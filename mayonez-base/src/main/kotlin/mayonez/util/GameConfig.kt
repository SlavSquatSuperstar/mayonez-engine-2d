package mayonez.util

import mayonez.io.text.*

/**
 * Reads user preferences from a file and stores them in a [Record].
 *
 * @param filename the location of the preferences file
 * @param defaults the default preferences
 * @author SlavSquatSuperstar
 */
open class GameConfig(
    private val filename: String, protected val defaults: Record
) {

    /** The configuration, stored as a record */
    protected val config: Record = defaults.copy()

    /**
     * Reads user preferences from a file.
     *
     * @return the record
     */
    protected open fun readUserPreferences(): Record {
        return JSONFile(filename).readJSON()
    }

    /**
     * Checks user preferences and replaces and invalid values with the default
     * value.
     *
     * @param userPrefs the user preferences
     * @param defaults the default preferences
     */
    protected open fun validateUserPreferences(userPrefs: Record, defaults: Record) {
    }

    override fun toString(): String {
        return "GameConfig ($filename)"
    }

}