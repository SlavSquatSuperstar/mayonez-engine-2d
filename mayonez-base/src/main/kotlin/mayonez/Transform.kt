package mayonez

import mayonez.annotations.*
import mayonez.math.*
import java.util.*

/**
 * Stores the position, rotation and scale of a GameObject and provides
 * additional methods.
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
     * @param rotation the rotation
     */
    constructor(position: Vec2, rotation: Float) : this(position, rotation, Vec2(1f))

    companion object {
        // Factory Methods

        /**
         * Creates a transform that will rotate anything by given angle but
         * preserve position and scale.
         *
         * @param rotation the rotation angle
         * @return the rotation transform
         */
        @JvmStatic
        fun rotateInstance(rotation: Float) = Transform(Vec2(), rotation, Vec2(1f))

        /**
         * Creates a transform that will scale anything by given factor but
         * preserve position and rotation.
         *
         * @param scale the scale factor
         * @return the scale transform
         */
        @JvmStatic
        fun scaleInstance(scale: Vec2) = Transform(Vec2(), 0f, scale)
    }

    // Transform Properties

    /** Where the object is located in the scene. */
    var position: Vec2 = Vec2(position)
        set(position) {
            field.set(position)
        }

    // Internal field
    private var angle: Angle = Angle.createDegrees(rotation)

    /** The angle the object is oriented, in degrees. */
    var rotation: Float
        get() = angle.degrees
        set(rotation) {
            angle = Angle.createDegrees(rotation)
        }

    /** How large the object is along each of its axes. */
    var scale: Vec2 = Vec2(scale)
        set(scale) {
            field.set(scale)
        }

    // Property Mutator Methods

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

    /**
     * Applies this transform to another transform, combining the position,
     * rotation, and scale of both.
     *
     * @param other another transform
     * @return the combined transformation, or a copy if other is null
     */
    fun combine(other: Transform?): Transform {
        return if (other == null) copy()
        else Transform(
            this.position + (other.position * this.scale).rotate(this.rotation),
            this.rotation + other.rotation,
            this.scale * other.scale
        )
    }

    // Space Transform Methods

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
     * Transforms a point from world space to the object's local space, with
     * this Transform's position serving as the origin.
     *
     * @param world a 2D point in the world
     * @return the localized point
     */
    fun toLocal(world: Vec2): Vec2 = ((world - position) / scale).rotate(-rotation)

    /**
     * Transforms a point from the object's local space to world space, with
     * this Transform's position serving as the * origin.
     *
     * @param local a localized 2D point
     * @return the point in the world
     */
    fun toWorld(local: Vec2): Vec2 = (local.rotate(rotation) * scale) + position

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
        return (other is Transform) && this.equalsTransform(other)
    }

    private fun equalsTransform(other: Transform): Boolean {
        return (this.position == other.position)
                && MathUtils.equals(this.rotation, other.rotation)
                && (this.scale == other.scale)
    }

    override fun hashCode(): Int = Objects.hash(position, rotation, scale)

    override fun toString() = String.format("Position: %s, Rotation: %.2f, Scale: %s", position, rotation, scale)
}