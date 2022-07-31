package slavsquatsuperstar.util

import org.json.JSONObject

/**
 * A basic data structure storing information under key-value pairs, similar to objects in many programming languages
 * storing data in fields. Also serves as a wrapper around JSONObject classes from external libraries.
 *
 * @author SlavSquatSuperstar
 */
// TODO safety measures so we don't set the use wrong data type for something
// TODO json record subclass because this may be slow?
open class Record(private val map: MutableMap<String?, Any?>) {

    /**
     * Creates an empty record with no data.
     */
    constructor() : this(HashMap())

    // Copy Methods

    /**
     * Erases all stored data and replaces it with data from another config object, including key-value pairs with
     * null elements.
     *
     * @param record another record
     */
    protected fun copyFrom(record: Record) {
        this.clear() // erase all data
        for (entry in record.entries()) this[entry.key] = entry.value // copy all data
    }

    /**
     * Overwrites or appends key-value pairs with non-null values from another object while preserving other existing
     * data in this record.
     *
     * @param record another record
     */
    protected fun loadFrom(record: Record) {
        for (entry in record.entries()) // overwrite or create values if not null
            if (entry.value != null) this[entry.key] = entry.value
    }

    // HashMap Methods

    /**
     * Retrieves the value stored under this key as a Java [Object], or null if it does not exist.
     */
    operator fun get(key: String?): Any? = map[key]

    /**
     * Retrieves the value stored under this key as a [List], or null if it does not exist.
     */
    fun getArray(key: String?): List<Any?>? {
        val value = map[key]
        return if (value is List<*>) value else null
    }

    /**
     * Retrieves the value stored under this key as a string, or an empty string if it does not exist.
     */
    fun getString(key: String?): String = map[key]?.toString() ?: ""

    /**
     * Retrieves the value stored under this key as a boolean, or false if it does not exist.
     */
    fun getBoolean(key: String?): Boolean {
        return getString(key).equals("true", ignoreCase = true) || getString(key).equals("yes", ignoreCase = true)
    }

    /**
     * Retrieves the value stored under this key as an integer, or 0 if it does not exist.
     */
    fun getInt(key: String?): Int {
        val value = map[key] ?: return 0
        return if (value is Number) value.toInt()
        else try {
            value.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }

    /**
     * Retrieves the value stored under this key as a float, or 0 if it does not exist.
     */
    fun getFloat(key: String?): Float {
        val value = map[key] ?: return 0f
        return if (value is Number) value.toFloat()
        else try {
            value.toString().toFloat()
        } catch (e: NumberFormatException) {
            0f
        }
    }

    /**
     * Stores a values under this key, or overwrites if it already exists.
     */
    operator fun set(key: String?, value: Any?) {
        map[key] = value
    }

    // Helper Methods

    /**
     * Deletes all JSON key-value pairs.
     */
    fun clear() = map.clear()

    /**
     * Returns an iterable set of keys.
     *
     * @return the key set
     */
    fun keys(): Set<String?> = map.keys

    /**
     * Returns an iterable set of keys.
     *
     * @return the key set
     */
    fun entries(): Set<Map.Entry<String?, Any?>> = map.entries

    /**
     * Returns this object as a formatted JSON string.
     *
     * @return the JSON string
     */
    fun toJSONString(): String = JSONObject(map).toString()

    override fun toString(): String = map.toString()
}