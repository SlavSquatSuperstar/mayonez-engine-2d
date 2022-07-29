package slavsquatsuperstar.util

import slavsquatsuperstar.math.MathUtils.random

/**
 * A collection of utility functions for manipulating text data.
 *
 * @author SlavSquatSuperstar
 */
object StringUtils {

    /**
     * Capitalizes the first letter of a string, ignoring all other letters.
     *
     * @param text a string
     * @return the capitalized string
     */
    @JvmStatic
    fun capitalizeFirst(text: String): String {
        val chars = text.toCharArray()
        chars[0] = chars[0].uppercaseChar()
        return String(chars)
    }

    /**
     * Capitalizes the first letter of each word in a string, ignoring all other letters.
     *
     * @param text a string
     * @return the capitalized string
     */
    @JvmStatic
    fun capitalizeWords(text: String): String {
        val words = text.split(" ").toTypedArray()
        for (i in words.indices) words[i] = capitalizeFirst(words[i])
        return words.joinToString(" ")
    }

    /**
     * Returns the string that comes first in alphabetical order, with uppercase letters coming before lowercase letters.
     *
     * @param s1 the first string to compare
     * @param s2 the second string to compare
     * @return the lesser string
     */
    @JvmStatic
    fun getFirst(s1: String, s2: String): String = if (s1 < s2) s1 else s2

    /**
     * Returns the string that comes first in alphabetical order.
     *
     * @param s1 the first string to compare
     * @param s2 the second string to compare
     * @param ignoreCase whether to treat uppercase and lowercase letters as equal
     * @return the lesser string
     */
    @JvmStatic
    fun getFirst(s1: String, s2: String, ignoreCase: Boolean): String {
        return if (ignoreCase)
            if (s1 < s2) s1 else s2
        else
            if (s1.compareTo(s2, true) < 0) s1 else s2
    }

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