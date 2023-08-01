package mayonez.init

import mayonez.*
import mayonez.math.*
import mayonez.util.*
import java.util.function.Predicate

/**
 * Defines allowed values for a game preference and corrects the preference if set to
 * something invalid.
 *
 * @author SlavSquatSuperstar
 */
internal abstract class PreferenceValidator<T> protected constructor(
    private vararg val keys: String?,
    private val isValid: Predicate<T>
) {
    protected abstract fun getValue(key: String?, preferences: Record): T

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

/**
 * Forces a string preference to not be empty.
 *
 * @author SlavSquatSuperstar
 */
internal class StringValidator(vararg keys: String?) :
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
internal class IntValidator(min: Int, max: Int, vararg keys: String) :
    PreferenceValidator<Int>(*keys, isValid = Predicate<Int> { n -> n in Interval(min, max) }) {

    override fun getValue(key: String?, preferences: Record): Int {
        return preferences.getInt(key)
    }
}