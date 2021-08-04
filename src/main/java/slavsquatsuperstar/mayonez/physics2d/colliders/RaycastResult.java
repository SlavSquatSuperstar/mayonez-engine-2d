package slavsquatsuperstar.mayonez.physics2d.colliders;

import slavsquatsuperstar.math.Vec2;

/**
 * Provides additional information about a raycast if passed into {@link Collider2D}.raycast().
 *
 * @author SlavSquatSuperstar
 */
public class RaycastResult {

    private Vec2 contact = new Vec2(); // contact point
    private Vec2 normal = new Vec2(); // contact normal
    private float distance = -1; // unit lengths along ray to intersection

    public static void reset(RaycastResult result) {
        if (result != null)
            result.set(new Vec2(), new Vec2(), -1);
    }

    void set(Vec2 contact, Vec2 normal, float distance) {
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
