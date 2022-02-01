package slavsquatsuperstar.mayonez.physics2d.collision

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D

/**
 * Stores information about a collision between two objects.
 *
 * @author SlavSquatSuperstar
 */
class Manifold(
    val self: Collider2D,
    val other: Collider2D,
    normal: Vec2,
    /**
     * How much the colliders are overlapping along the normal axis.
     *
     * @return the penetration distance
     */
    val depth: Float
) {

    private val contacts = ArrayList<Vec2>(2)

    // Properties

    /**
     * What direction the two colliders should be separated when resolving the collision.
     *
     * @return the direction of separation
     */
    val normal: Vec2 = normal.unit()

    // Contact Methods

    fun countContacts(): Int = contacts.size

    fun getContact(index: Int): Vec2 = contacts[index]

    fun addContact(contactPoint: Vec2) { // Should be package-protected
        contacts.add(contactPoint)
    }

    override fun toString(): String = "Collision ($self and $other)"
}