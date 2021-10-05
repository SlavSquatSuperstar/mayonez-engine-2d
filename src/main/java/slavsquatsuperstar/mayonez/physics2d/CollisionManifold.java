package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stores information about a collision between two objects.
 *
 * @author SlavSquatSuperstar
 */
public class CollisionManifold {

    private final Collider2D self, other;
    private final Vec2 normal;
    private final float depth;
    private final List<Vec2> contacts = new ArrayList<>(2);

    public CollisionManifold(Collider2D self, Collider2D other, Vec2 normal, float depth) {
        this.self = self;
        this.other = other;
        this.normal = normal.unit();
        this.depth = depth;
    }

    // Getters

    public Collider2D getSelf() {
        return self;
    }

    public Collider2D getOther() {
        return other;
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

    public Vec2 getContact(int index) {
        return contacts.get(index);
    }

    public void addContact(Vec2 contactPoint) {
        contacts.add(contactPoint);
    }

    public void addContacts(Vec2... contactPoints) {
        contacts.addAll(Arrays.asList(contactPoints));
    }

    @Override
    public String toString() {
        return String.format("Collision (%s and %s)", self, other);
    }
}
