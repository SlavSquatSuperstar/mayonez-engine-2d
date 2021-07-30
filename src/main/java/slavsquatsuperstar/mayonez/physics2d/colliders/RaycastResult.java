package slavsquatsuperstar.mayonez.physics2d.colliders;

import slavsquatsuperstar.mayonez.Vector2;

/**
 * Provides additional information about a raycast if passed into {@link Collider2D}.raycast().
 *
 * @author SlavSquatSuperstar
 */
public class RaycastResult {

    private Vector2 contact = new Vector2(); // intersection point
    private Vector2 normal = new Vector2(); // contact normal
    private float distance = -1; // unit lengths from ray origin to contact point

    public static void reset(RaycastResult result) {
        if (result != null) {
            result.contact = new Vector2();
            result.normal = new Vector2();
            result.distance = -1;
        }
    }

    void set(Vector2 contact, Vector2 normal, float distance) {
        this.contact = contact;
        this.normal = normal.unitVector();
        this.distance = distance;
    }

    public Vector2 getContact() {
        return contact;
    }

    public Vector2 getNormal() {
        return normal;
    }

    public float getDistance() {
        return distance;
    }

}
