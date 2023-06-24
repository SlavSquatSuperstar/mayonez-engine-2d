package mayonez.math

import mayonez.util.*

/**
 * A class with methods dedicated to generating random numbers and
 * primitive types.
 *
 * @author SlavSquatSuperstar
 */
// TODO set seed
object Random {

    // Random Numbers

    /**
     * Generates a uniform random float between the two provided bounds. All
     * numbers in the range have an equal chance of occurring.
     *
     * @param min the lower bound
     * @param max the upper bound
     * @return the random float
     */
    @JvmStatic
    fun randomFloat(min: Float, max: Float): Float {
        val interval = Interval(min, max)
        return (Math.random() * (interval.difference())).toFloat() + interval.min
    }

    /**
     * Generates a random uniform integer between the two provided bounds. All
     * numbers in the range have an equal chance of occurring.
     *
     * @param min the lower bound (inclusive)
     * @param max the upper bound (inclusive)
     * @return the random integer
     */
    @JvmStatic
    fun randomInt(min: Int, max: Int): Int {
        val interval = Interval(min.toFloat(), max.toFloat())
        return (Math.random() * (interval.difference() + 1) + interval.min).toInt()
    }

    /**
     * Generates a random [Vec2] between the provided min and max vectors. All
     * vectors in the rectangular region have an equal chance of occurring.
     *
     * @param min the lower bound vector
     * @param max the upper bound vector
     * @return the random vector
     */
    @JvmStatic
    fun randomVector(min: Vec2, max: Vec2): Vec2 {
        return Vec2(randomFloat(min.x, max.x), randomFloat(min.y, max.y))
    }

    /**
     * Generates a random [Vec2] between the provided x and y bounds. All
     * vectors in the rectangular region have an equal chance of occurring.
     *
     * @param minX the lower x bound
     * @param maxX the upper x bound
     * @param minY the lower y bound
     * @param maxY the upper y bound
     * @return the random vector
     */
    @JvmStatic
    fun randomVector(minX: Float, maxX: Float, minY: Float, maxY: Float): Vec2 {
        return Vec2(randomFloat(minX, maxX), randomFloat(minY, maxY))
    }

    // Random Experiments

    /**
     * Generates a random event with a certain percent chance of succeeding,
     * equal to a biased coin flip.
     *
     * @param percent the chance of succeeding, from 0-1
     * @return true the given percentage of the time, otherwise false
     */
    @JvmStatic
    fun randomPercent(percent: Float): Boolean = Math.random() < percent

    /**
     * Generates a random event with a 50% chance of succeeding, equal to a
     * fair coin flip.
     *
     * @return true half the time, otherwise false
     */
    @JvmStatic
    fun randomBoolean(): Boolean = Math.random() < 0.5f

    // Random Text Methods

    /**
     * Generates a random uppercase letter.
     *
     * @return a random character A-Z
     */
    @JvmStatic
    fun randomUppercase(): Char = randomInt(65, 90).toChar()

    /**
     * Generates a random lowercase letter.
     *
     * @return a random character a-z
     */
    @JvmStatic
    fun randomLowercase(): Char = randomInt(97, 122).toChar()

    /**
     * Generates a random digit.
     *
     * @return a random character 0-9
     */
    @JvmStatic
    fun randomDigit(): Char = randomInt(48, 57).toChar()

    /**
     * Returns a random alphanumeric character.
     *
     * @return a random character A-Z, a-z, or 0-9.
     */
    @JvmStatic
    fun randomAlphanumeric(): Char {
        val rand = randomInt(0, 30) // 62 values
        return if (rand < 13) randomUppercase() // 26/62 = 13/31 Uppercase
        else if (rand < 26) randomLowercase() // 26/62 = 13/31 Lowercase
        else randomDigit() // 10/62 = 5/31 Number
    }

    // Random Color Methods

    /**
     * Generates a random opaque RGB color.
     *
     * @return a color with random RGB values 0-255
     */
    @JvmStatic
    fun randomColor(): MColor = MColor(randomInt(0, 255), randomInt(0, 255), randomInt(0, 255))

}