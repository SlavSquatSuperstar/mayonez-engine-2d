package slavsquatsuperstar.mayonez

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2

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
    constructor() : this(Vec2(), 0f, Vec2(1f, 1f))
    constructor(position: Vec2) : this(position, 0f, Vec2(1f, 1f))
    constructor(position: Vec2, scale: Vec2) : this(position, 0f, scale)

    val rotationRadians: Float
        get() = MathUtils.toRadians(rotation)

    // Property Mutator Methods
    /**
     * Translates the parent object along the x and y axes.
     *
     * @param displacement how much and which direction to move
     * @return this transform
     */
    fun move(displacement: Vec2): Transform {
        position += displacement
        return this
    }

    /**
     * Rotates the parent object around its center.
     *
     * @param degrees the counterclockwise angle
     * @return this transform
     */
    fun rotate(degrees: Float): Transform {
        rotation += degrees
        return this
    }

    /**
     * Stretches the parent object and all its components by the given factors along the x and y axes.
     *
     * @param scale the new x and y size compared to the current dimensions
     * @return this transform
     */
    fun resize(scale: Vec2): Transform {
        this.scale *= scale
        return this
    }

    // Space Transform Methods

    /**
     * Returns the x-axis, or the vector (1, 0), in this transform's local space.
     *
     * @return the local x-axis
     */
    val direction: Vec2
        get() = Vec2(1f, 0f).rotate(rotation)

    /**
     * Transforms a point from world space to the object's local space, with this Transform's position serving as the
     * origin.
     *
     * @param world a 2D point in the world
     * @return the localized point
     */
    // TODO move to collider?
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
                    MathUtils.equals(this.rotation, other.rotation) &&
                    this.scale == other.scale
            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = position.hashCode()
        result = 31 * result + rotation.hashCode()
        result = 31 * result + scale.hashCode()
        return result
    }

//    override fun equals(other: Any?): Boolean {
//        return if (other is Transform) {
//            other.position == this.position &&
//                    MathUtils.equals(other.rotation, this.rotation)
//            other.scale == this.scale
//        } else false
//    }

    override fun toString() = String.format("Position: %s, Rotation: %.2f, Scale: %s", position, rotation, scale)
}