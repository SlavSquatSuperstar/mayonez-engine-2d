package mayonez.physics.colliders

import mayonez.math.*
import mayonez.math.shapes.*

/**
 * An oriented rectangle defined by a width and height centered around the
 * object's position.
 *
 * @constructor Constructs a box with the given dimensions
 * @author SlavSquatSuperstar
 */
class BoxCollider(val size: Vec2) : PolygonCollider(Rectangle(Vec2(), size))