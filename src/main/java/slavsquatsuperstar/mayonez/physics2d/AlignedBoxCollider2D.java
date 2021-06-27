package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.Vector2;

// TODO scale with transform

/**
 * Represents an axis-aligned bounding box, a rectangle that is is never rotated.
 * The sides will always align with the x and y axes.
 */
public class AlignedBoxCollider2D extends Collider2D {

    private Vector2 size;

    public AlignedBoxCollider2D(Vector2 size) {
        this.size = size;
    }

    public float width() {
        return size.x;
    }

    public float height() {
        return size.y;
    }

    public Vector2 min() {
        return center().sub(size.div(2f));
    }

    public Vector2 max() {
        return center().add(size.div(2f));
    }

    @Override
    public boolean contains(Vector2 point) {
        return point.x >= min().x && point.x <= max().x && point.y >= min().y && point.y <= max().y;
    }

    @Override
    public boolean intersects(Line2D line) {
        if (contains(line.start) || contains(line.end))
            return true;
        return raycast(new Ray2D(line), null);
    }

    public boolean intersects(CircleCollider circle) {
        return circle.intersects(this);
    }

    @Override
    public boolean raycast(Ray2D ray, RaycastResult result) {
        RaycastResult.reset(result);

        // Component division
        Vector2 dir = ray.direction;
        Vector2 min = min().sub(ray.origin);
        min.x /= dir.x;
        min.y /= dir.y;
        Vector2 max = max().sub(ray.origin);
        max.x /= dir.x;
        max.y /= dir.y;

        float tmin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tmax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));
        if (tmax < 0f || tmin > tmax)
            return false;

        float distToBox = (tmin < 0f) ? tmax : tmin;
        boolean hit = distToBox > 0f;
        if (ray.getLimit() > -1)
            hit = hit && distToBox * distToBox < ray.getLimit(); // limit ray if constructed from line

        if (result != null) {
            Vector2 point = ray.origin.add(ray.direction.mul(distToBox));
            Vector2 normal = point.sub(center()).unit();
            result.set(point, normal, distToBox, hit);
        }

        return hit;
    }
}
