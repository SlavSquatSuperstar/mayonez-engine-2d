package mayonez.util

import org.json.JSONObject

/**
 * A basic data structure storing information under key-value pairs,
 * similar to objects in many programming languages storing data in fields.
 * Also serves as a wrapper around JSONObject classes from external
 * libraries.
 *
 * @author SlavSquatSuperstar
 */
// TODO safety measures so we don't set the use wrong data type for something
open class Record(private val map: MutableMap<String?, Any?>) {

    /** Creates an empty record with no data. */
    constructor() : this(HashMap())

    // Copy Methods

    /**
     * Sets this record as a copy of another record, clearing all stored data
     * and replacing it with data from the other object.
     *
     * @param record another record
     */
    protected fun copyFrom(record: Record) {
        this.clear() // erase all data
        for (entry in record.entries()) this[entry.key] = entry.value // copy all data
    }

    /**
     * Adds all key-value pairs from another record but does not erase any
     * pre-existing data. If any new entries contain a key already in this
     * record, their new values are used, as long as they are not null.
     *
     * @param record another record
     */
    protected fun addAll(record: Record) {
        record.entries().forEach {
            if (it.value != null) this[it.key] = it.value
        }
    }

    // Hash Map Methods

    /**
     * Retrieves the value stored under this key as a Java [Object], or null if
     * it does not exist.
     */
    operator fun get(key: String?): Any? = map[key]

    /**
     * Retrieves the value stored under this key as a [List], or null if it
     * does not exist.
     */
    fun getArray(key: String?): List<Any?>? {
        val value = map[key]
        return if (value is List<*>) value else null
    }

    /**
     * Retrieves the value stored under this key as a string, or an empty
     * string if it does not exist.
     */
    fun getString(key: String?): String = map[key]?.toString() ?: ""

    /**
     * Retrieves the value stored under this key as a boolean, or false if it
     * does not exist.
     */
    fun getBoolean(key: String?): Boolean {
        val lower = getString(key).lowercase()
        if (lower == "true" || lower == "yes") return true
        val num = getInt(key)
        return num != 0
    }

    /**
     * Retrieves the value stored under this key as an integer, or 0 if it does
     * not exist.
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
     * Retrieves the value stored under this key as a float, or 0 if it does
     * not exist.
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

    /** Stores a values under this key, or overwrites if it already exists. */
    operator fun set(key: String?, value: Any?) {
        map[key] = value
    }

    // Helper Methods

    /** Deletes all JSON key-value pairs. */
    fun clear() = map.clear()

    /** Whether any value is stored in the record under the given key. */
    operator fun contains(key: String?): Boolean = map.containsKey(key)

    /** The number of key-value pairs stored in this record. */
    fun size(): Int = map.size

    /**
     * Returns an iterable set of key-value pairs.
     *
     * @return the entry set
     */
    private fun entries(): Set<Map.Entry<String?, Any?>> = map.entries

    /**
     * Returns this record as a formatted JSON string.
     *
     * @return the JSON string
     */
    fun toJSONString(): String = JSONObject(map).toString(4)

    override fun toString(): String = map.toString()
}