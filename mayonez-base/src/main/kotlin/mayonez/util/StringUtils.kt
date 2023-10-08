package mayonez.util

import mayonez.util.StringUtils.toString

/**
 * A collection of utility functions for manipulating text data.
 *
 * @author SlavSquatSuperstar
 */
object StringUtils {

    // Case Conversion Methods

    /**
     * Converts a string to sentence case, capitalizing the first letter and
     * making all other letters lowercase.
     *
     * @param text a string
     * @return the string with the first word capitalized
     */
    @JvmStatic
    fun capitalizeFirstWord(text: String): String {
        val chars = text.lowercase().toCharArray()
        chars[0] = chars[0].uppercaseChar()
        return String(chars)
    }

    /**
     * Converts a string to title case, capitalizing the first letter of each
     * word and making all other letters lowercase.
     *
     * @param text a string
     * @return the string with each word capitalized
     */
    @JvmStatic
    fun capitalizeAllWords(text: String): String {
        return text.split(" ")
            .joinToString(" ") { capitalizeFirstWord(it) }
    }

    // To String Methods

    /**
     * Returns the simple name of an object's [Class], without the package
     * name. If the object is anonymous, its superclass name will be returned.
     *
     * @param obj an object
     * @return the object's class name, or "null" if the object is null
     */
    @JvmStatic
    fun getObjectClassName(obj: Any?): String {
        return when {
            obj == null -> "null"
            obj.javaClass.isAnonymousClass -> obj.javaClass.superclass.simpleName
            else -> obj.javaClass.simpleName
        }
    }

    /**
     * Finds the first object in the given collection with the given [toString]
     * representation, case-insensitive.
     *
     * @param objects the collection to search
     * @param name the object to find
     * @return the object with the name, or null if none is found
     */
    @JvmStatic
    fun <T> findWithName(objects: Collection<T>, name: String?): T? {
        return objects.find { it.toString().equals(name, ignoreCase = true) }
    }

}