package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.Vector2;

public class RaycastResult {

    private Ray2D ray = new Ray2D(new Vector2(), new Vector2());
    private float t = -1; // unit lengths from ray origin to contact point
    private boolean hit = false;

    public void set(Vector2 point, Vector2 normal, float t, boolean hit) {
        ray = new Ray2D(point, normal);
        this.t = t;
        this.hit = hit;
    }

    public Vector2 contactPoint() {
        return hit ? ray.origin.add(ray.direction.mul(t)) : null;
    }

    public static void reset(RaycastResult result) {
        if (result != null) {
            result.ray = new Ray2D(new Vector2(), new Vector2());
            result.t = -1;
            result.hit = false;
        }
    }

}
