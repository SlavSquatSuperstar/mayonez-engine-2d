package mayonez.util

/**
 * Stores information under key-value pairs, similar to how objects store
 * data in fields. This class is intended to store simple data types, including
 * numbers, texts, booleans, arrays, and other records, similar to SQL records
 * and JavaScript objects The [mayonez.assets.text.JSONFile] class can also
 * convert Records to and from JSONObjects.
 *
 * Storing other data types in a Record may  make (de-)serialization difficult,
 * in which case a dedicated Java class may be preferable.
 *
 * @author SlavSquatSuperstar
 */
open class Record(map: Map<String?, Any?>) {

    /** Creates an empty record with no data. */
    constructor() : this(HashMap())

    private val map: MutableMap<String?, Any?> = HashMap(map)

    // Get Property Methods

    /**
     * Retrieves the value stored under this key as a Java [Object], or null
     * if the key does not exist. The value itself may also be stored as null.
     *
     * @param key the key
     * @return the object
     */
    operator fun get(key: String?): Any? = map[key]

    /**
     * Retrieves the list or array stored under this key as a generic [List]
     * of [Object]s, or null otherwise.
     *
     * @param key the key
     * @return the list
     */
    fun getArray(key: String?): List<Any?>? {
        return when (val value = map[key]) {
            is List<*> -> value
            is Array<*> -> value.asList<Any?>()
            else -> null
        }
    }

    /**
     * Retrieves the record or map stored under this key as another [Record],
     * or null otherwise.
     *
     * @param key the key
     * @return the record
     */
    fun getObject(key: String?): Record? {
        return when (val value = map[key]) {
            is Record -> value
            is Map<*, *> -> value.toRecord()
            else -> null
        }
    }

    /**
     * Retrieves the value stored under this key as a string, or an empty
     * string otherwise.
     *
     * @param key the key
     * @return the string
     */
    fun getString(key: String?): String = map[key]?.toString() ?: ""

    /**
     * Retrieves the value stored under this key as a boolean. If the value is
     * stored as "true", "yes", or a non-zero number, then it will also be
     * considered true. Otherwise, the value will be null.
     *
     * @param key the key
     * @return the boolean
     */
    fun getBoolean(key: String?): Boolean {
        return when (val value = map[key]) {
            is Boolean -> value
            is Number -> value.toInt() != 0
            is String -> {
                value.equals("true", ignoreCase = true) ||
                        value.equals("yes", ignoreCase = true)
            }

            else -> false
        }
    }

    /**
     * Retrieves the value stored under this key as an integer. If the value
     * is a numerical string or other number, then it will be parsed and
     * truncated into an integer. If the value is a boolean, then it will be
     * considered 1 if true and 0 if false. Otherwise, the value will be 0.
     *
     * @param key the key
     * @return the integer
     */
    fun getInt(key: String?): Int {
        return when (val value = map[key]) {
            is Number -> value.toInt()
            is Boolean -> if (value) 1 else 0
            is String -> value.parseFloat()?.toInt() ?: 0
            else -> 0
        }
    }

    /**
     * Retrieves the value stored under this key as a float. If the value is
     * a numerical string or other number, then it will be parsed into a float.
     * If the value is a boolean, then it will be considered 1 if true and 0 if
     * false. Otherwise, the value will be 0.
     *
     * @param key the key
     * @return the float
     */
    fun getFloat(key: String?): Float {
        return when (val value = map[key]) {
            is Number -> value.toFloat()
            is Boolean -> if (value) 1f else 0f
            is String -> value.parseFloat() ?: 0f
            else -> 0f
        }
    }

    // Set Property Methods

    /**
     * Stores or updates an integer under this key.
     *
     * @param key the key
     * @param value the integer
     */
    operator fun set(key: String?, value: Int?) {
        map[key] = value
    }

    /**
     * Stores or updates a float under this key.
     *
     * @param key the key
     * @param value the float
     */
    operator fun set(key: String?, value: Float?) {
        map[key] = value
    }

    /**
     * Stores or updates a boolean under this key.
     *
     * @param key the key
     * @param value the boolean
     */
    operator fun set(key: String?, value: Boolean?) {
        map[key] = value
    }

    /**
     * Stores or updates a string under this key.
     *
     * @param key the key
     * @param value the string
     */
    operator fun set(key: String?, value: String?) {
        map[key] = value
    }

    /**
     * Stores or updates a list under this key. The list will be converted to
     * a generic [Object] list.
     *
     * @param key the key
     * @param value the list
     */
    operator fun set(key: String?, value: List<*>?) {
        map[key] = value as List<Any?>
    }

    /**
     * Stores or updates am array under this key. The array will be converted
     * to a generic [Object] list.
     *
     * @param key the key
     * @param value the array
     */
    operator fun set(key: String?, value: Array<*>?) {
        map[key] = value?.asList<Any?>()
    }

    /**
     * Stores or updates another record under this key.
     *
     * @param key the key
     * @param value the record
     */
    operator fun set(key: String?, value: Record?) {
        map[key] = value
    }

    /**
     * Stores or updates a map under this key. The map will be converted into
     * another record
     *
     * @param key the key
     * @param value the map
     */
    operator fun set(key: String?, value: Map<*, *>?) {
        map[key] = value?.toRecord()
    }

    // Copy Methods

    /**
     * Create a copy of this record with all the same stored keys-value pairs.
     *
     * @return the copy
     */
    fun copy(): Record = Record(HashMap(this.map))

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
     * Converts this record to a generic [Object]-to-[Object] map.
     *
     * @return a map
     */
    // TODO convert records to maps
    fun toMap(): Map<Any?, Any?> = map.toMap<Any?, Any?>()

    // Object Overrides

    /**
     * Checks whether any value is stored in the record under the given key.
     * At most one value may be stored under a null key.
     *
     * @param key the key
     * @return if the key exists
     */
    operator fun contains(key: String?): Boolean = map.containsKey(key)

    override fun equals(other: Any?): Boolean {
        return other is Record &&
                other.size() == this.size() &&
                other.map == this.map
    }

    override fun hashCode(): Int = (map as HashMap).hashCode()

    override fun toString(): String = map.toString()

}

// Helper Functions

private fun String.parseFloat(): Float? {
    return try {
        this.toFloat()
    } catch (_: NumberFormatException) {
        null
    }
}

private fun Map<*, *>.toRecord(): Record {
    val newMap = HashMap<String?, Any?>()
    this.forEach { newMap[it.key?.toString()] = it.value }
    // TODO recursive convert
    return Record(newMap)
}