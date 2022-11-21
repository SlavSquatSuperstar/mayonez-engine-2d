package slavsquatsuperstar.mayonez.util

import slavsquatsuperstar.mayonez.math.MathUtils.random

/**
 * A collection of utility functions for manipulating text data.
 *
 * @author SlavSquatSuperstar
 */
object StringUtils {

    // Array Methods

    /**
     * Joins an array of strings into a single string using the given separator
     *
     * @param strings   the string array
     * @param separator the string connecting each member
     * @return a single text string
     */
    @JvmStatic
    fun join(strings: Array<String>, separator: String): String {
        return strings.joinToString(separator = separator)
    }

    /**
     * Splits a single string into an array along the given separator
     *
     * @param string    the text string
     * @param delimiter the string separating each member
     * @return an array of strings
     */
    @JvmStatic
    fun split(string: String, delimiter: String): Array<String> {
        return string.split(delimiter).toTypedArray()
    }

    // todo parse other types

    // Case Conversion Methods

    /**
     * Converts a string to sentence case, capitalizing the first letter and making all other letters lowercase.
     *
     * @param text a string
     * @return the string with the first word capitalized
     */
    @JvmStatic
    fun capitalize(text: String): String {
        val chars = text.lowercase().toCharArray()
        chars[0] = chars[0].uppercaseChar()
        return String(chars)
    }

    /**
     * Converts a string to title case, capitalizing the first letter of each word and making all other letters lowercase.
     *
     * @param text a string
     * @return the string with each word capitalized
     */
    @JvmStatic
    fun capitalizeWords(text: String): String {
        val words = split(text, " ")
        for (i in words.indices) words[i] = capitalize(words[i])
        return words.joinToString(" ")
    }

    // Ordering Methods

    /**
     * Returns the string that comes first in alphabetical order, with uppercase letters coming before lowercase letters.
     *
     * @param s1 the first string to compare
     * @param s2 the second string to compare
     * @return the lesser string
     */
    @JvmStatic
    fun sortStrings(s1: String, s2: String): String = if (s1 < s2) s1 else s2

    /**
     * Returns the string that comes first in alphabetical order.
     *
     * @param s1         the first string to compare
     * @param s2         the second string to compare
     * @param ignoreCase whether to treat uppercase and lowercase letters as equal
     * @return the lesser string
     */
    @JvmStatic
    fun sortStrings(s1: String, s2: String, ignoreCase: Boolean): String {
        return if (ignoreCase) if (s1 < s2) s1 else s2
        else if (s1.compareTo(s2, true) < 0) s1 else s2
    }

    // Random Methods

    /**
     * Generates a random uppercase letter (A-Z).
     */
    @JvmStatic
    fun randomUppercase(): Char = random(65, 90).toChar()

    /**
     * Generates a random lowercase letter (a-z).
     */
    @JvmStatic
    fun randomLowercase(): Char = random(97, 122).toChar()

    /**
     * Generates a random number character (0-9).
     */
    @JvmStatic
    fun randomNumber(): Char = random(0, 9).toChar()

    /**
     * Returns a random alphanumeric character (A-Z, a-z, 0-9) .
     */
    @JvmStatic
    fun randomAlphanumeric(): Char {
        val rand = random(0, 30)
        return if (rand < 13) randomUppercase() // 26/62 = 13/31 Uppercase
        else if (rand < 26) randomLowercase() // 26/62 = 13/31 Lowercase
        else randomNumber() // 10/62 = 5/31 Number
    }

}