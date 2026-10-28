package mayonez

import mayonez.annotations.*
import mayonez.math.*
import mayonez.math.shapes.Rectangle
import java.util.*

/**
 * Stores the position, rotation and scale of a [Node] and converts points
 * between different coordinate systems. Each coordinate system has an origin,
 * rotation, and two scaled basis vectors.
 *
 * A transform `T(x) = A•x + b` maps a point `x` from its Node's local space to
 * a point `T(x)` in the parent Node's space or the global space. Each transform
 * is equal to a linear transformation `A`, which combines a scale and rotation
 * `R•S`, and a translation `b`. Transforms can also be represented as 2x3
 * matrices or 3x3 augmented matrices. For more on matrix multiplication, see
 * [Mat22].
 *
 * A transform is invertible if none of the scale components are zero (i.e. the
 * determinant is non-zero). An inverse transform `T^-1(x)` maps world space
 * back to local space, such that `T^-1(T(x)) = x` for all `x`. Additionally,
 * the identity transform is a special transform `I` such that `I(x) = x` for
 * all `x`.
 *
 * Transforms may also be concatenated to get the global transform of any node
 * inside a tree. Child transforms are applied before parent transforms, such
 * that `T_glo(x) = T_par(T_ch(x))`.
 *
 * Note: This class is a simplified version of [java.awt.geom.AffineTransform],
 * in that the scale axes are always perpendicular. Normally, concatenating
 * affine transforms will cause shearing if the outer scale axes have different
 * lengths and the inner rotation is not a multiple of 90 degrees.
 *
 * See also
 * - [Godot: Matrices and transforms](https://docs.godotengine.org/en/stable/tutorials/math/matrices_and_transforms.html)
 * - [Wikipedia: Affine transformation](https://en.wikipedia.org/wiki/Affine_transformation)
 *
 * @author SlavSquatSuperstar
 */
class Transform(position: Vec2, rotation: Float, scale: Vec2) {

    // Constructors

    /**
     * Creates an identity transform with position (0, 0), rotation 0, and
     * scale (1, 1).
     */
    constructor() : this(Vec2(), 0f, Vec2(1f))

    /**
     * Creates a transform with a given position and with rotation and scale
     * set to identity.
     *
     * @param position the position
     */
    constructor(position: Vec2) : this(position, 0f, Vec2(1f))

    /**
     * Creates a transform with a given position and rotation with scale set to
     * identity.
     *
     * @param position the position
     * @param rotation the rotation, in degrees
     */
    constructor(position: Vec2, rotation: Float) : this(position, rotation, Vec2(1f))

    companion object {
        // Factory Methods

        /**
         * Creates a transform that will rotate anything by a given angle but
         * preserve position and scale.
         *
         * @param rotation the rotation angle, in degrees
         * @return the rotation transform
         */
        @JvmStatic
        fun rotateInstance(rotation: Float) = Transform(Vec2(), rotation, Vec2(1f))

        /**
         * Creates a transform that will scale anything by a given factor but
         * preserve position and rotation.
         *
         * @param scale the scale factor
         * @return the scale transform
         */
        @JvmStatic
        fun scaleInstance(scale: Vec2) = Transform(Vec2(), 0f, scale)
    }

    // Transform Properties

    /** The transform's origin. */
    var position: Vec2 = Vec2(position)
        set(position) {
            field.set(position)
        }

    // Internal field
    private var angle: Angle = Angle.createDegrees(rotation)

    /** The transform's rotation, in degrees. */
    var rotation: Float
        get() = angle.degrees
        set(rotation) {
            angle = Angle.createDegrees(rotation)
        }

    /** The length of each of the transform's basis vectors. */
    var scale: Vec2 = Vec2(scale)
        set(scale) {
            field.set(scale)
        }

    /**
     * The transform's determinant, equal to its scale components multiplied.
     */
    val determinant: Float
        get() = scale.x * scale.y

    // Transform Mutators

    /**
     * Translates the parent object along the x and y axes.
     *
     * @param translation how much and which direction to move
     */
    @Mutating
    fun move(translation: Vec2) {
        position += translation
    }

    /**
     * Rotates the parent object around its center.
     *
     * @param degrees the counterclockwise angle
     */
    @Mutating
    fun rotate(degrees: Float) {
        angle.rotateDegrees(degrees)
    }

    /**
     * Rotates the parent object around its center.
     *
     * @param angle the counterclockwise angle
     */
    @Mutating
    fun rotate(angle: Angle) {
        this.angle += angle
    }

    /**
     * Stretches the parent object and all its components by the given factors
     * along the x and y axes.
     *
     * @param factor the new x and y size compared to the current dimensions
     */
    @Mutating
    fun scale(factor: Vec2) {
        this.scale *= factor
    }
    // Transform Operations

    /**
     * Applies this transform to the given point. In other words, transforms a point
     * from world space to the object's local space, with this transform's position
     * serving as the origin.
     *
     * @param local a localized 2D point
     * @return the point in the world
     */
    fun apply(local: Vec2): Vec2 {
        // Scale, rotate, then translate (preserves length of basis vectors)
        // T(x) = A•x + b, where A = R•S
        return (local * scale).rotate(rotation) + position
    }

    /**
     * Applies this transform's inverse to the given point. In other words,
     * transforms a point from the object's local space to world space, with
     * this transform's position serving as the origin.
     *
     * @param world a 2D point in the world
     * @return the localized point
     */
    fun applyInverse(world: Vec2): Vec2 {
        // Translate, rotate, then scale
        // T^1(x) = A^-1•(x - b), where A^-1 = S^-1•R^-1
        return (world - position).rotate(-rotation) / scale
    }

    /**
     * Concatenates this transform with another transform. The resulting
     * transform will have position equal to this transform applied to the
     * other transform's position, rotation equal to the sum of both rotations,
     * and scale equal to the product of both scales (without rotation).
     *
     * Like matrix multiplication, applying the combined transform is the same
     * as applying the other transform first and then this one. Concatenating
     * transforms is not commutative, so reversing the order of the transforms
     * may result in a different result.
     *
     * Note that unlike [java.awt.geom.AffineTransform], no shearing/skewing
     * of the basis vectors occurs if the first scale is not uniform.
     *
     * @param other another transform
     * @return the combined transformation, or a copy if other is null
     */
    fun combine(other: Transform?): Transform {
        return if (other == null) copy()
        else Transform(
            this.apply(other.position),
            this.rotation + other.rotation,
            this.scale * other.scale
        )
    }

    // Transform Space Methods

    /**
     * Returns the positive x-axis, or the vector (1, 0), in this transform's
     * local space.
     *
     * @return the local x-axis
     */
    val right: Vec2
        get() = angle.rotation * Vec2(1f, 0f)

    /**
     * Returns the positive y-axis, or the vector (0, 1), in this transform's
     * local space.
     *
     * @return the local y-axis
     */
    val up: Vec2
        get() = angle.rotation * Vec2(0f, 1f)

    /**
     * Gets the oriented bounding box (OBB) defined by this transform, with
     * the center equal to the position, size equal to the scale, and angle
     * equal to the rotation.
     *
     * @return the bounding rectangle
     */
    fun getBoundingRectangle(): Rectangle {
        return Rectangle(position, scale, rotation)
    }

    // Copy Methods

    fun copy() = Transform(position, rotation, scale)

    @Mutating
    fun set(from: Transform) {
        this.position.set(from.position)
        this.rotation = from.rotation
        this.scale.set(from.scale)
    }


    // Object Overrides

    override fun equals(other: Any?): Boolean {
        return (other is Transform)
                && (this.position == other.position)
                && MathUtils.equals(this.rotation, other.rotation)
                && (this.scale == other.scale)
    }

    override fun hashCode(): Int = Objects.hash(position, rotation, scale)

    override fun toString() = String.format("Position: %s, Rotation: %.2f, Scale: %s", position, rotation, scale)

}