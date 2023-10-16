package mayonez.math

import mayonez.math.equalsArray
import mayonez.math.minusArray
import mayonez.math.plusArray
import mayonez.math.timesFloat
import java.util.*

/**
 * A matrix of floats with 2 rows and 2 columns that stores values from
 * left to right, then top to bottom.
 *
 * @param mA the element at the first row and first column
 * @param mB the element at the first row and second column
 * @param mC the element at the second row and first column
 * @param mD the element at the second row and second column
 * @constructor Create a matrix from four numbers, with the values
 *     [(a, b), (c, d)].
 */
class Mat22(
    /** The element at the first row and first column of the matrix. */
    private var mA: Float,
    /** The element at the first row and second column of the matrix. */
    private var mB: Float,
    /** The element at the second row and first column of the matrix. */
    private var mC: Float,
    /** The element at the second row and second column of the matrix. */
    private var mD: Float
) {

    /** Create an identity matrix from an array of four floats */
    private constructor(values: FloatArray) : this(values[0], values[1], values[2], values[3])

    /** Create an identity matrix with the values [(1, 0), (0, 1)] */
    constructor() : this(1f, 0f, 0f, 1f)

    /**
     * Creates a rotation matrix from the given angle, with the values
     * [[(cos, -sin), (sin, cos)]].
     *
     * @degrees the angle measure in degrees
     */
    constructor(degrees: Float) : this(getRotationMatrix(degrees))

    /**
     * Create a matrix from two column (1x2) vectors, with the values [[(v1.x,
     * v2.x), (v1.y, v2.y)]]. Useful for storing linear transformations.
     */
    constructor(col1: Vec2, col2: Vec2) : this(col1.x, col2.x, col1.y, col2.y)

    // Matrix Elements

    /**
     * Returns the given row of the matrix as a vector.
     *
     * @param row which row of the matrix, zero-indexed
     * @return the specified row
     */
    private fun row(row: Int): Vec2 {
        return when (row) {
            0 -> Vec2(mA, mB)
            1 -> Vec2(mC, mD)
            else -> Vec2()
        }
    }

    /**
     * Returns the given column of the matrix as a vector.
     *
     * @param col which column of the matrix, zero-indexed
     * @return the specified column
     */
    private fun col(col: Int): Vec2 {
        return when (col) {
            0 -> Vec2(mA, mC)
            1 -> Vec2(mB, mD)
            else -> Vec2()
        }
    }

    /**
     * Calculates the determinant of this matrix.
     *
     * @return the determinant
     */
    fun determinant(): Float = (mA * mD) - (mB * mC)

    // Arithmetic Operations

    operator fun plus(m: Mat22): Mat22 {
        return Mat22(this.toArray().plusArray(m.toArray()))
    }

    operator fun minus(m: Mat22): Mat22 {
        return Mat22(this.toArray().minusArray(m.toArray()))
    }

    /**
     * Multiplies each component of this matrix by the given scalar.
     *
     * @param scalar a real number
     * @return the scaled matrix
     */
    operator fun times(scalar: Float): Mat22 {
        return Mat22(this.toArray().timesFloat(scalar))
    }

    // Matrix and Vector Multiplication

    /**
     * Applies this matrix to a vector as a linear transformation.
     *
     * @param v a 2D column vector
     * @return the transformed vector as a column
     */
    operator fun times(v: Vec2): Vec2 {
        return Vec2(row(0).dot(v), row(1).dot(v))
    }

    /**
     * Multiplies this matrix by another. The result is the same as applying
     * the other matrix and then applying this one. Note: Matrix multiplication
     * is generally not commutative.
     *
     * @param m a 2x2 matrix
     * @return the matrix product
     */
    operator fun times(m: Mat22): Mat22 {
//        val values = FloatArray(SIZE)
//        for (r in 0..<NUM_ROWS) {
//            for (c in 0..<NUM_COLS) {
//                values[r * NUM_COLS + c] = this.row(r).dot(m.col(c))
//            }
//        }
//        return Mat22(values)
        return Mat22(
            this.row(0).dot(m.col(0)),
            this.row(0).dot(m.col(1)),
            this.row(1).dot(m.col(0)),
            this.row(1).dot(m.col(1))
        )
    }

    // Object Overrides

    override fun equals(other: Any?): Boolean {
        return (other is Mat22) && this.equalsMatrix(other)
    }

    private fun equalsMatrix(other: Mat22): Boolean {
        return this.toArray().equalsArray(other.toArray())
    }

    private fun toArray(): FloatArray = floatArrayOf(mA, mB, mC, mD)

    override fun hashCode(): Int = Objects.hash(toArray())

    /**
     * A string representation of this matrix, in the form [[(m00, m01), (m10,
     * m11)]].
     */
    override fun toString(): String = "[${row(0)}, ${row(1)}]"

}

private fun getRotationMatrix(degrees: Float): FloatArray {
    val cos = FloatMath.cos(degrees)
    val sin = FloatMath.sin(degrees)
    return floatArrayOf(cos, -sin, sin, cos)
}