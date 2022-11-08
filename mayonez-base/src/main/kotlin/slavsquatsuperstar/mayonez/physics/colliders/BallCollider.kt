package slavsquatsuperstar.mayonez.physics.colliders

import slavsquatsuperstar.mayonez.math.Vec2
import slavsquatsuperstar.mayonez.physics.shapes.Ellipse
import slavsquatsuperstar.mayonez.physics.shapes.Shape

/**
 * An oriented ellipse defined by a width and height, or a circle defined by a radius, centered at the object's position.
 *
 * @constructor Constructs an ellipse with the given dimensions (not radii)
 *
 * @author SlavSquatSuperstar
 */
class BallCollider(val size: Vec2) : Collider(Ellipse(Vec2(), size)) {

    /**
     * Constructs a circle with the given radius (not diameter)q
     */
    constructor(radius: Float) : this(Vec2(radius * 2f))

    override fun transformToWorld(): Shape { // use circle when possible
        val worldShape = super.transformToWorld()
        return if (worldShape is Ellipse && worldShape.isCircle) worldShape.boundingCircle()
        else worldShape
    }

}