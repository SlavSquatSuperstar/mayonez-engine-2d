package mayonez.math

/**
 * A class with methods dedicated to generating random numbers and primitive types.
 *
 * @author SlavSquatSuperstar
 */
object Random {

    // Random Numbers

    /**
     * Generates a random integer between the two provided bounds.
     *
     * @param min the lower bound (inclusive)
     * @param max the maximum bound (inclusive)
     * @return the random float
     */
    @JvmStatic
    fun randomFloat(min: Float, max: Float): Float {
        val range = Range(min, max)
        return (Math.random() * (range.max - range.min + Float.MIN_VALUE)).toFloat() + range.min
    }

    /**
     * Generates a random integer between the two provided bounds.
     *
     * @param min the lower bound (inclusive)
     * @param max the maximum bound (inclusive)
     * @return The random integer.
     */
    @JvmStatic
    fun randomInt(min: Int, max: Int): Int {
        val range = Range(min.toFloat(), max.toFloat())
        return (Math.random() * (range.max - range.min + 1) + range.min).toInt()
    }

    /**
     * Generates a random event with a certain percent chance of succeeding.
     *
     * @param percent the change of succeeding, from 0-1
     * @return true the given percentage of the time, otherwise false
     */
    @JvmStatic
    fun randomPercent(percent: Float): Boolean = Math.random() < percent

    /**
     * Generates a random event with a 50% chance of succeeding, equal to a fair coin flip.
     *
     * @return true half the time, otherwise false
     */
    @JvmStatic
    fun randomBoolean(): Boolean = Math.random() < 0.5f

    // Random Text Methods

    /**
     * Generates a random uppercase letter (A-Z).
     */
    @JvmStatic
    fun randomUppercase(): Char = randomInt(65, 90).toChar()

    /**
     * Generates a random lowercase letter (a-z).
     */
    @JvmStatic
    fun randomLowercase(): Char = randomInt(97, 122).toChar()

    /**
     * Generates a random digit (0-9).
     */
    @JvmStatic
    fun randomDigit(): Char = randomInt(48, 57).toChar()

    /**
     * Returns a random alphanumeric character (A-Z, a-z, 0-9) .
     */
    @JvmStatic
    fun randomAlphanumeric(): Char {
        val rand = randomInt(0, 30) // 62 values
        return if (rand < 13) randomUppercase() // 26/62 = 13/31 Uppercase
        else if (rand < 26) randomLowercase() // 26/62 = 13/31 Lowercase
        else randomDigit() // 10/62 = 5/31 Number
    }

}