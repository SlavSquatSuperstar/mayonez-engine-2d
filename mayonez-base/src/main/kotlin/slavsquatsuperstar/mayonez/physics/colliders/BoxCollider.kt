package slavsquatsuperstar.mayonez.physics.colliders

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics.shapes.Polygon

/**
 * An oriented rectangle defined by a width and height centered around the object's position.
 *
 * @constructor Constructs a box with the given dimensions
 *
 * @author SlavSquatSuperstar
 */
class BoxCollider(val size: Vec2) : PolygonCollider(Polygon.rectangle(Vec2(), size)) {

}