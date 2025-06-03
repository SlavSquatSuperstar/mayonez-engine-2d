package mayonez.physics.colliders

import mayonez.annotations.*
import mayonez.math.*
import mayonez.math.shapes.*

/**
 * A line segment defined by a length and centered around the object's position.
 * An EdgeCollider can be used to model the ground or a wall, but using a thin
 * [BoxCollider] is recommended for more accurate collision detection
 * for dynamic objects.
 *
 * @constructor Constructs an edge with the given dimensions
 * @author SlavSquatSuperstar
 */
// TODO angular resolution is incorrect
@ExperimentalFeature
class EdgeCollider(val length: Float) : Collider(getEdge(length))

private fun getEdge(length: Float): Edge {
    val halfLen = length * 0.5f
    return Edge(Vec2(-halfLen, 0f), Vec2(halfLen, 0f))
}