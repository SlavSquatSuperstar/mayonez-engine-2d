package mayonez.util

/**
 * Stores information under key-value pairs, similar to how objects store
 * data in fields. Records store simple data types, including numbers,
 * texts, booleans, arrays, and other records, and may be converted to and
 * from a JSONObject.
 *
 * @author SlavSquatSuperstar
 */
open class Record(map: Map<String?, Any?>) {

    /** Creates an empty record with no data. */
    constructor() : this(HashMap())

    private val map: MutableMap<String?, Any?> = HashMap(map)

    // Get Property Methods

    /**
     * Retrieves the value stored under this key as a Java [Object], or null if
     * it does not exist.
     */
    operator fun get(key: String?): Any? = map[key]

    /**
     * Retrieves the array stored under this key as a [List], or null if it
     * does not exist.
     */
    fun getArray(key: String?): List<Any?>? {
        val value = map[key]
        return if (value is List<*>) value else null
    }

    /**
     * Retrieves the object stored under this key as a [Record], or null if it
     * does not exist.
     */
    fun getObject(key: String?): Record? {
        val value = map[key]
        return if (value is Map<*, *>) {
            val newMap = HashMap<String?, Any?>()
            value.forEach { newMap[it.key?.toString()] = it.value }
            Record(newMap)
        } else null
    }

    /**
     * Retrieves the value stored under this key as a string, or an empty
     * string if it does not exist.
     */
    fun getString(key: String?): String = map[key]?.toString() ?: ""

    /**
     * Retrieves the value stored under this key as a boolean, or false if it
     * does not exist. If the value is "true", "yes", or not 0, then it will be
     * returned as true.
     */
    fun getBoolean(key: String?): Boolean {
        return when {
            getString(key).equals("true", ignoreCase = true) -> true
            getString(key).equals("yes", ignoreCase = true) -> true
            getInt(key) != 0 -> true
            else -> false
        }
    }

    /**
     * Retrieves the value stored under this key as an integer, or 0 if it does
     * not exist.
     */
    fun getInt(key: String?): Int {
        return when (val value = map[key]) {
            is Number -> value.toInt()
            is String -> try {
                value.toInt()
            } catch (e: NumberFormatException) {
                0
            }

            else -> 0
        }
    }

    /**
     * Retrieves the value stored under this key as a float, or 0 if it does
     * not exist.
     */
    fun getFloat(key: String?): Float {
        return when (val value = map[key]) {
            is Number -> value.toFloat()
            is String -> try {
                value.toFloat()
            } catch (e: NumberFormatException) {
                0f
            }

            else -> 0f
        }
    }

    // Set Property Methods

    /** Stores or updates an integer under this key. */
    operator fun set(key: String?, value: Int?) {
        map[key] = value
    }

    /** Stores or updates a float under this key. */
    operator fun set(key: String?, value: Float?) {
        map[key] = value
    }

    /** Stores or updates a boolean under this key. */
    operator fun set(key: String?, value: Boolean?) {
        map[key] = value
    }

    /** Stores or updates a string under this key. */
    operator fun set(key: String?, value: String?) {
        map[key] = value
    }

    /** Stores or updates a list under this key. */
    operator fun set(key: String?, value: List<*>?) {
        map[key] = value as List<Any?>
    }

    /** Stores or updates an array under this key. */
    operator fun set(key: String?, value: Array<*>?) {
        map[key] = value?.asList()
    }

    /** Stores or updates a record under this key. */
    operator fun set(key: String?, value: Record?) {
        map[key] = value?.map?.toMap()
    }

    /** Stores or updates a map under this key. */
    operator fun set(key: String?, value: Map<*, *>?) {
        map[key] = value?.toMap()
    }

    // Copy Methods

    /**
     * Create a copy of this record with all the same stored values.
     *
     * @return the copy
     */
    fun copy(): Record = Record(this.map)

    /**
     * Adds all key-value pairs from another record. Any keys that exist in
     * both records are overwritten with the value from the other record, as
     * long as that value is not null.
     *
     * @param record another record
     */
    fun setFrom(record: Record) {
        record.map.entries
            .filter { it.value != null }
            .forEach { map[it.key] = it.value }
    }

    /**
     * Overwrites the entry stored in this record under the given key with the
     * key-value pair from another record, as long as the value is not null.
     *
     * @param record another record
     * @param key the key for the entry
     */
    fun setFrom(record: Record, key: String?) {
        if (record[key] != null) this.map[key] = record[key]
    }

    // Map Methods

    /** Deletes all key-value pairs from this record. */
    fun clear() = map.clear()

    /** The number of key-value pairs stored in this record. */
    fun size(): Int = map.size

    /**
     * Converts this record to an object-to-object map.
     *
     * @return a map
     */
    fun toMap(): Map<Any?, Any?> = map.toMap()

    // Object Overrides

    /** Whether any value is stored in the record under the given key. */
    operator fun contains(key: String?): Boolean = map.containsKey(key)

    override fun toString(): String = map.toString()

}