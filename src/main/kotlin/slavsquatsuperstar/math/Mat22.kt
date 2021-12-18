package slavsquatsuperstar.math

/**
 * A Mat22 represents a table of numbers with 2 rows and 2 columns.
 */
class Mat22 {

    /**
     * The element at the first row and first column of the matrix.
     */
    @JvmField
    var m00: Float

    /**
     * The element at the first row and second column of the matrix.
     */
    @JvmField
    var m01: Float

    /**
     * The element at the second row and first column of the matrix.
     */
    @JvmField
    var m10: Float

    /**
     * The element at the second row and second column of the matrix.
     */
    @JvmField
    var m11: Float

    /**
     * Initialize this matrix from four numbers, from left to right, then top to bottom.
     */
    constructor(m00: Float, m01: Float, m10: Float, m11: Float) {
        this.m00 = m00
        this.m01 = m01
        this.m10 = m10
        this.m11 = m11
    }

    /**
     * Initialize this matrix from two column vectors. Useful for storing linear transformations.
     */
    constructor(col1: Vec2, col2: Vec2) : this(col1.x, col2.x, col1.y, col2.y)

    /**
     * Initialize this matrix to the identity matrix, [(1, 0), (0, 1)]
     */
    constructor() : this(1f, 0f, 0f, 1f)

    /**
     * Creates a rotation matrix from the given angle.
     *
     * @degrees the angle measure in degrees
     */
    constructor(degrees: Float) {
        val cos = MathUtils.cos(degrees)
        val sin = MathUtils.sin(degrees)
        m00 = cos
        m01 = -sin
        m10 = sin
        m11 = cos
    }

    // Row/Column Vectors
    fun row1(): Vec2 = Vec2(m00, m01)
    fun row2(): Vec2 = Vec2(m10, m11)
    fun col1(): Vec2 = Vec2(m00, m10)
    fun col2(): Vec2 = Vec2(m01, m11)

    /**
     * Calculates the determinant of this matrix.
     *
     * @return the determinant
     */
    fun determinant(): Float = (m00 * m11) - (m01 * m10)

    // Arithmetic Operations

    operator fun plus(m: Mat22): Mat22 = Mat22(this.col1() + m.col1(), this.col2() + m.col2())

    operator fun minus(m: Mat22): Mat22 = Mat22(this.col1() - m.col1(), this.col2() - m.col2())

    /**
     * Multiplies each component of this matrix by the given scalar
     *
     * @param scalar a real number
     * @return the scaled matrix
     */
    operator fun times(scalar: Float): Mat22 = Mat22(m00 * scalar, m01 * scalar, m10 * scalar, m11 * scalar)

    // Matrix and Vector Multiplication

    /**
     * Applies this matrix to a vector as a linear transformation.
     *
     * @param v a 2D column vector
     * @return the transformed vector
     */
    operator fun times(v: Vec2): Vec2 = Vec2(m00 * v.x + m01 * v.y, m10 * v.x + m11 * v.y)

    /**
     * Multiplies this matrix by another. The result is the same as applying the other matrix and then applying this one.
     * Note: Matrix multiplication is generally not commutative.
     *
     * @param m a 2x2 matrix
     * @return the matrix product
     */
    operator fun times(m: Mat22): Mat22 = Mat22(
        this.row1().dot(m.col1()),
        this.row1().dot(m.col2()),
        this.row2().dot(m.col1()),
        this.row2().dot(m.col2())
    )

    /**
     * Flips this matrix over its primary diagonal.
     *
     * @return this matrix's transpose.
     */
    fun transpose(): Mat22 = Mat22(row1(), row2())

    // Overrides

    override fun hashCode(): Int {
        var result = m00.hashCode()
        result = 31 * result + m01.hashCode()
        result = 31 * result + m10.hashCode()
        result = 31 * result + m11.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Mat22)
            MathUtils.equals(m00, other.m00) && MathUtils.equals(m01, other.m01)
                    && MathUtils.equals(m10, other.m10) && MathUtils.equals(m11, other.m11)
        else super.equals(other)
    }

    override fun toString(): String = "[${row1()}, ${row2()}]"

}