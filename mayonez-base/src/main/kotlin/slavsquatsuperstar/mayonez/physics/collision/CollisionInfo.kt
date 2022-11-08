package slavsquatsuperstar.mayonez.physics.collision

import slavsquatsuperstar.mayonez.math.Vec2
import slavsquatsuperstar.mayonez.physics.shapes.Shape

/**
 * Stores information about a collision between two objects. Also referred to as a collision manifold.
 *
 * @author SlavSquatSuperstar
 */
class CollisionInfo(
    val self: Shape,
    val other: Shape,
    normal: Vec2, // collision normal facing out from the reference shape
    val depth: Float // positive penetration (separation) distance along normal
) {

    // Properties

    private val contacts = ArrayList<Vec2>(2)

    val normal: Vec2 = normal.unit()

    // Contact Methods

    fun numContacts(): Int = contacts.size

    fun getContact(index: Int): Vec2 = contacts[index]

    fun addContact(contactPoint: Vec2) = contacts.add(contactPoint)

    fun flip(): CollisionInfo {
        val col = CollisionInfo(other, self, -normal, depth)
        for (i in 0 until numContacts()) col.addContact(getContact(i))
        return col
    }

    override fun toString(): String = "Collision ($self and $other)"
}