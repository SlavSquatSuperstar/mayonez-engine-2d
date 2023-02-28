package mayonez.util

/**
 * A collection of utility functions for manipulating text data.
 *
 * @author SlavSquatSuperstar
 */
object StringUtils {

    // Array Methods

    /**
     * Joins an array of strings into a single string using the given
     * separator.
     *
     * @param strings the string array
     * @param separator the string connecting each member
     * @return a single text string
     */
    @JvmStatic
    fun join(strings: Array<Any?>, separator: String): String {
        return strings.joinToString(separator = separator)
    }

    /**
     * Splits a single string into an array along the given separator.
     *
     * @param string the text string
     * @param delimiter the string separating each member
     * @return an array of strings
     */
    @JvmStatic
    fun split(string: String, delimiter: String): Array<String> {
        return string.split(delimiter).toTypedArray()
    }

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
        val words = split(text, " ")
        for (i in words.indices) words[i] = capitalizeFirstWord(words[i])
        return words.joinToString(" ")
    }

    // Ordering Methods

    // To String Methods

    /**
     * Returns the simple name of an object's class, without the package name.
     * If the object is anonymous, its superclass name will be returned.
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

}