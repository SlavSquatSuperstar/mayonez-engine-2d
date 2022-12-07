package mayonez

import mayonez.annotations.Mutating
import mayonez.math.FloatMath
import mayonez.math.Vec2
import java.util.*

/**
 * Stores the position, rotation and scale of a GameObject and provides additional methods.
 *
 * @author SlavSquatSuperstar
 */
class Transform(
    /**
     * Where the object is located in the scene.
     */
    @JvmField
    var position: Vec2,

    /**
     * The angle the object is oriented, in degrees.
     */
    @JvmField
    var rotation: Float,

    /**
     * How much the object is being stretched along its axes.
     */
    @JvmField
    var scale: Vec2,
) {
    constructor() : this(Vec2(), 0f, Vec2(1f))

    constructor(position: Vec2) : this(position, 0f, Vec2(1f))

    constructor(position: Vec2, rotation: Float) : this(position, rotation, Vec2(1f))

    companion object {
        @JvmStatic
        fun translateInstance(translation: Vec2) = Transform(translation, 0f, Vec2(1f))

        @JvmStatic
        fun rotateInstance(rotation: Float) = Transform(Vec2(), rotation, Vec2(1f))

        @JvmStatic
        fun scaleInstance(scale: Vec2) = Transform(Vec2(), 0f, scale)
    }

    // Property Mutator Methods
    /**
     * Translates the parent object along the x and y axes.
     *
     * @param translation how much and which direction to move
     * @return this transform
     */
    @Mutating
    fun move(translation: Vec2): Transform {
        position += translation
        return this
    }

    /**
     * Rotates the parent object around its center.
     *
     * @param degrees the counterclockwise angle
     * @return this transform
     */
    @Mutating
    fun rotate(degrees: Float): Transform {
        rotation += degrees
        return this
    }

    /**
     * Stretches the parent object and all its components by the given factors along the x and y axes.
     *
     * @param factor the new x and y size compared to the current dimensions
     * @return this transform
     */
    @Mutating
    fun scale(factor: Vec2): Transform {
        this.scale *= factor
        return this
    }

    /**
     * Applies this transform to another transform, combining the position, rotation, and scale of both.
     *
     * @param other another transform
     * @return the combined transformation, or copy if other is null
     */
    fun combine(other: Transform?): Transform {
        return if (other == null) copy()
        else Transform(this.position + other.position, this.rotation + other.rotation, this.scale * other.scale)
    }

    // Space Transform Methods

    /**
     * Returns the positive x-axis, or the vector (1, 0), in this transform's local space.
     *
     * @return the local x-axis
     */
    val right: Vec2
        get() = Vec2(1f, 0f).rotate(rotation)

    /**
     * Returns the positive y-axis, or the vector (0, 1), in this transform's local space.
     *
     * @return the local y-axis
     */
    val up: Vec2
        get() = Vec2(0f, 1f).rotate(rotation)

    /**
     * Transforms a point from world space to the object's local space, with this Transform's position serving as the
     * origin.
     *
     * @param world a 2D point in the world
     * @return the localized point
     */
    fun toLocal(world: Vec2): Vec2 = ((world - position) / scale).rotate(-rotation)

    /**
     * Transforms a point from the object's local space to world space, with this Transform's position serving as the *
     * origin.
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

    // Overrides

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other is Transform -> this.position == other.position &&
                    FloatMath.equals(this.rotation, other.rotation) &&
                    this.scale == other.scale

            else -> false
        }
    }

    override fun hashCode(): Int = Objects.hash(position, rotation, scale);

//    override fun equals(other: Any?): Boolean {
//        return if (other is Transform) {
//            other.position == this.position &&
//                    MathUtils.equals(other.rotation, this.rotation)
//            other.scale == this.scale
//        } else false
//    }

    override fun toString() = String.format("Position: %s, Rotation: %.2f, Scale: %s", position, rotation, scale)
}