package slavsquatsuperstar.mayonez.physics2d;

import org.apache.commons.lang3.tuple.ImmutablePair;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores information about a collision between two objects.
 *
 * @author SlavSquatSuperstar
 */
public class CollisionManifold {

    private final ImmutablePair<Collider2D, Collider2D> colliders;
    private final Vec2 normal;
    private final float depth;
    private final List<Vec2> contacts = new ArrayList<>();

    public CollisionManifold(Collider2D collider1, Collider2D collider2, Vec2 normal, float depth) {
        colliders = new ImmutablePair<>(collider1, collider2);
        this.normal = normal.unitVector();
        this.depth = depth;
    }

    // Getters

    public Collider2D getSelf() {
        return colliders.left;
    }

    public Collider2D getOther() {
        return colliders.right;
    }

    /**
     * What direction the two colliders should be separated when resolving the collision.
     *
     * @return the direction of separation
     */
    public Vec2 getNormal() {
        return normal;
    }

    /**
     * How much the colliders are overlapping along the normal axis.
     *
     * @return the penetration distance
     */
    public float getDepth() {
        return depth;
    }

    public int countContacts() {
        return contacts.size();
    }

    /**
     * Where the two colliders are intersecting, in first object's local space.
     *
     * @return the contact points
     */
    public List<Vec2> getContacts() {
        return contacts;
    }

    public void addContactPoint(Vec2 contactPoint) {
        contacts.add(contactPoint);
    }

    @Override
    public String toString() {
        return String.format("Collision (%s and %s)", getSelf(), getOther());
    }
}
