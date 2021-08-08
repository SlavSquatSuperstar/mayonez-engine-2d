package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;

/**
 * Provides additional information about a raycast if passed into {@link Collider2D}.raycast().
 *
 * @author SlavSquatSuperstar
 */
public class RaycastResult {

    private final Vec2 contact; // contact point
    private final Vec2 normal; // contact normal
    private final float distance; // unit lengths along ray to intersection

    public RaycastResult(Vec2 contact, Vec2 normal, float distance) {
        this.contact = contact;
        this.normal = normal.unit();
        this.distance = distance;
    }

    public Vec2 getContact() {
        return contact;
    }

    public Vec2 getNormal() {
        return normal;
    }

    public float getDistance() {
        return distance;
    }

}
