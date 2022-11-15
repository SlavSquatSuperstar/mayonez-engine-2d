package slavsquatsuperstar.mayonez.physics.collision

import slavsquatsuperstar.mayonez.math.Vec2
import slavsquatsuperstar.mayonez.physics.shapes.Shape

/**
 * Stores information about a collision between two objects, including contacts and penetration
 *
 * @author SlavSquatSuperstar
 */
class Manifold(
    private val shape1: Shape,
    private val shape2: Shape,
    val normal: Vec2, // collision normal facing out from the first shape
    val depth: Float // positive penetration (overlap) distance along normal
) {

    // Collision Fields
    init {
        normal.set(normal.unit())
    }

    private val contacts = ArrayList<Vec2>(2)

    // Contact Methods

    fun numContacts(): Int = contacts.size

    fun getContacts(): List<Vec2> = contacts

    fun getContact(index: Int): Vec2 = contacts[index]

    fun addContact(contactPoint: Vec2) = contacts.add(contactPoint)

    override fun toString(): String = "Collision ($shape1 and $shape2, ${numContacts()} points)"
}