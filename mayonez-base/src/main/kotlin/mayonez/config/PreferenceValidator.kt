package mayonez.config

import mayonez.*
import mayonez.math.*
import mayonez.util.*
import java.util.function.Predicate

/**
 * Defines allowed values for a game preference and corrects the preference
 * if set to something invalid.
 *
 * @param keys which preferences to check
 * @param isValid the rules that define a valid preference
 * @author SlavSquatSuperstar
 */
abstract class PreferenceValidator<T> protected constructor(
    private vararg val keys: String?,
    private val isValid: Predicate<T>
) {
    protected abstract fun getValue(key: String?, preferences: Record): T

    /**
     * Validates all specified preferences and replaces invalid preferences
     * with their default values.
     *
     * @param preferences the user preferences
     * @param defaults the default preferences
     */
    fun validate(preferences: Record, defaults: Record) {
        for (prefKey in keys) {
            val prefValue = getValue(prefKey, preferences)
            if (!isValid.test(prefValue)) {
                Logger.debug("Invalid value \"$prefValue\" for preference $prefKey, resetting to default")
                preferences.setFrom(defaults, prefKey)
            }
        }
    }
}

// Subclass Definitions

/**
 * Forces a string preference to not be empty.
 *
 * @author SlavSquatSuperstar
 */
class StringValidator(vararg keys: String?) :
    PreferenceValidator<String>(*keys, isValid = Predicate<String> { str -> str.isNotEmpty() }) {

    override fun getValue(key: String?, preferences: Record): String {
        return preferences.getString(key)
    }
}

/**
 * Forces an integer preference to between two values.
 *
 * @author SlavSquatSuperstar
 */
class IntValidator(min: Int, max: Int, vararg keys: String) :
    PreferenceValidator<Int>(*keys, isValid = Predicate<Int> { n -> n in Interval(min, max) }) {

    override fun getValue(key: String?, preferences: Record): Int {
        return preferences.getInt(key)
    }
}

/**
 * Forces a boolean value to be represented as a true/false string, yes/no string,
 * or 1/0 integer.
 *
 * @author SlavSquatSuperstar
 */
class BooleanValidator(vararg keys: String) :
    PreferenceValidator<String>(*keys, isValid = Predicate<String> {
        str -> str.isBooleanString().or(str.isBitNumber())
    }) {

    override fun getValue(key: String?, preferences: Record): String {
        return preferences.getString(key)
    }
}

// Boolean Helpers

private fun String.isBooleanString(): Boolean {
    return when (this.lowercase()) {
        "true", "false" -> true
        "yes", "no" -> true
        else -> false
    }
}

private fun String.isBitNumber(): Boolean {
    return when (this) {
        "0", "1" -> true
        else -> false
    }
}
