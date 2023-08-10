package mayonez.math

import mayonez.util.MColor

/**
 * Generates random numbers and primitive types. Most methods will use a
 * uniform distribution, where all valid values have an equal chance
 * of occurring.
 *
 * @author SlavSquatSuperstar
 */
object Random {

    private val rand = java.util.Random()

    // Seed Methods

    /**
     * Sets the initial state of the random number generator. By default,
     * the seed is set from the computer's local time.
     *
     * A random generator will always produce the same sequence of results
     * with a given seed. However, if only given any number, predicting
     * the next output will still be difficult, meaning the results are still
     * random in the long run.
     *
     * @param seed the random generator seed
     */
    @JvmStatic
    fun setSeed(seed: Long) {
        rand.setSeed(seed)
    }

    // Random Numbers

    /**
     * Generates a uniform random float between the two provided bounds. All
     * numbers in the range have a roughly equal chance of occurring.
     *
     * Note: In reality, the upper bound is technically excluded and can never occur.
     * However, since the sample space (floats) is continuous, all elements individually
     * have a probability of 0. Therefore, ignoring the bound does not significantly
     * affect the results.
     *
     * @param min the lower bound, inclusive
     * @param max the upper bound, inclusive
     * @return the random float
     */
    @JvmStatic
    fun randomFloat(min: Float, max: Float): Float {
        val interval = Interval(min, max)
        return (rand.nextFloat() * (interval.difference())) + interval.min
    }

    /**
     * Generates a random uniform integer between the two provided bounds. All
     * numbers in the range have a roughly equal chance of occurring.
     *
     * @param min the lower bound, inclusive
     * @param max the upper bound, inclusive
     * @return the random integer
     */
    @JvmStatic
    fun randomInt(min: Int, max: Int): Int {
        val interval = Interval(min.toFloat(), max.toFloat())
        return (rand.nextFloat() * (interval.difference() + 1) + interval.min).toInt()
    }

    /**
     * Generates a random float under a Gaussian (normal) distribution with the given
     * mean, µ, and standard deviation, σ. The mean and standard deviation define the center
     * and spread of the distribution, respectively.
     *
     * Values closest to µ will be the most likely to occur, while values get
     * progressively less common farther from µ. On average, 99.5% values occur
     * within µ ± 3σ, or three standard deviations from the mean.
     *
     * @param mean the distribution mean, µ
     * @param stdev the distribution standard deviation, σ
     * @return the random Gaussian float
     */
    @JvmStatic
    fun randomGaussian(mean: Float, stdev: Float): Float {
        return rand.nextGaussian(mean.toDouble(), stdev.toDouble()).toFloat()
    }

    // TODO random Gaussian range

    // Random Points

    /**
     * Generates a random angle between 0-360 degrees.
     *
     * @return the random angle
     */
    @JvmStatic
    fun randomAngle(): Float {
        return randomFloat(0f, 360f)
    }

    /**
     * Generates a random [Vec2] between the provided min and max vectors. All
     * points in the rectangular region have a roughly equal chance of occurring.
     *
     * @param min the lower bound vector, inclusive
     * @param max the upper bound vector, inclusive
     * @return the random vector
     */
    @JvmStatic
    fun randomVector(min: Vec2, max: Vec2): Vec2 {
        return Vec2(randomFloat(min.x, max.x), randomFloat(min.y, max.y))
    }

    /**
     * Generates a random [Vec2] between the provided x and y bounds. All
     * vectors in the rectangular region have a roughly equal chance of occurring.
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
     * Generates a random event with a specified average percent chance of
     * succeeding, equal to a biased coin flip.
     *
     * @param percent the chance of succeeding, between 0-1
     * @return true the given percentage of the time, otherwise false
     */
    @JvmStatic
    fun randomPercent(percent: Float): Boolean = rand.nextFloat() < percent

    /**
     * Generates a random event with a 50% average chance of succeeding,
     * equal to a fair coin flip.
     *
     * @return true half the time, otherwise false
     */
    @JvmStatic
    fun randomBoolean(): Boolean = rand.nextFloat() < 0.5f

    // Random Characters

    /**
     * Generates a random uniform uppercase letter.
     *
     * @return a random character A-Z
     */
    @JvmStatic
    fun randomUppercase(): Char = randomInt(65, 90).toChar()

    /**
     * Generates a random uniform lowercase letter.
     *
     * @return a random character a-z
     */
    @JvmStatic
    fun randomLowercase(): Char = randomInt(97, 122).toChar()

    /**
     * Generates a random uniform digit.
     *
     * @return a random character 0-9
     */
    @JvmStatic
    fun randomDigit(): Char = randomInt(48, 57).toChar()

    /**
     * Returns a random uniform alphanumeric character.
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
     * Generates a random uniform color with alpha set to 255.
     *
     * @return a color with random RGB values 0-255
     */
    @JvmStatic
    fun randomColor(): MColor = randomColor(0, 255)

    /**
     * Generates a random uniform color with R, G, and B components between two
     * values and with alpha set to 255.
     *
     * @param minComp the minimum value for RGB components
     * @param maxComp the maximum value for RGB components
     * @return a color with random RGB values min-max
     */
    @JvmStatic
    fun randomColor(minComp: Int, maxComp: Int): MColor {
        return MColor(randomInt(minComp, maxComp), randomInt(minComp, maxComp), randomInt(minComp, maxComp))
    }

}