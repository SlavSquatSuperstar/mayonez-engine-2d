package slavsquatsuperstar.mayonez.physics2d.colliders;

import slavsquatsuperstar.math.Vec2;

/**
 * Provides additional information about a raycast if passed into {@link Collider2D}.raycast().
 *
 * @author SlavSquatSuperstar
 */
public class RaycastResult {

    private Vec2 contact; // contact point
    private Vec2 normal; // contact normal
    private float distance; // unit lengths along ray to intersection

    public RaycastResult(Vec2 contact, Vec2 normal, float distance) {
        this.contact = contact;
        this.normal = normal.unitVector();
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
