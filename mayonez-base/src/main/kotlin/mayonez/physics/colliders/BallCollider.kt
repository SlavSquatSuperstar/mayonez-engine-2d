package mayonez.physics.colliders

import mayonez.math.Vec2
import mayonez.math.shapes.BoundingBox
import mayonez.math.shapes.Ellipse
import mayonez.math.shapes.Shape

/**
 * An oriented ellipse defined by a width and height, or a circle defined
 * by a radius, centered at the object's position.
 *
 * @constructor Constructs an ellipse with the given dimensions (not radii)
 * @author SlavSquatSuperstar
 */
class BallCollider(val size: Vec2) : Collider(Ellipse(Vec2(), size)) {

    /** Constructs a circle with the given radius (not diameter)q */
    constructor(radius: Float) : this(Vec2(radius * 2f))

    override fun getMinBounds(): BoundingBox {
        return transformToWorld().boundingCircle().boundingRectangle() // max is quicker than trig
    }

    override fun transformToWorld(): Shape { // use circle when possible
        val worldShape = super.transformToWorld()
        return if (worldShape is Ellipse && worldShape.isCircle) worldShape.boundingCircle()
        else worldShape
    }

}