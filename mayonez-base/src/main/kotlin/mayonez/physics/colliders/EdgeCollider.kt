package mayonez.physics.colliders

import mayonez.math.*
import mayonez.math.shapes.*

/**
 * A line segment defined by a length and centered around the
 * object's position. Using a thin [BoxCollider] may provide more accurate
 * collision detection for dynamic objects.
 *
 * @constructor Constructs an edge with the given dimensions
 * @author SlavSquatSuperstar
 */
// TODO angular resolution is incorrect
class EdgeCollider(val length: Float) : Collider(getEdge(length))

private fun getEdge(length: Float): Edge {
    val halfLen = length * 0.5f;
    return Edge(Vec2(-halfLen, 0f), Vec2(halfLen, 0f))
}