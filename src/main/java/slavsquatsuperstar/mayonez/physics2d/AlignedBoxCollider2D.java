package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.Vector2;

// TODO scale with transform

/**
 * Represents an axis-aligned bounding box, a rectangle that is is never rotated.
 * The sides will always align with the x and y axes.
 */
public class AlignedBoxCollider2D extends AbstractBoxCollider2D {

    public AlignedBoxCollider2D(Vector2 size) {
        super(size);
    }

    @Override
    public Vector2[] getVertices() {
        return new Vector2[]{
                new Vector2(min()), new Vector2(min().x, max().y), new Vector2(max().x, min().y), new Vector2(max())
        };
    }

    @Override
    public boolean contains(Vector2 point) {
        return point.x >= min().x && point.x <= max().x && point.y >= min().y && point.y <= max().y;
    }

    @Override
    public boolean detectCollision(Collider2D collider) {
        if (collider == this)
            return false;

        if (collider instanceof CircleCollider) {
            return intersects((CircleCollider) collider);
        } else if (collider instanceof AlignedBoxCollider2D) {
            return intersects((AlignedBoxCollider2D) collider);
        } else if (collider instanceof BoxCollider2D) {
            return intersects((BoxCollider2D) collider);
        } else {
            return false;
        }
    }

    boolean intersects(CircleCollider circle) {
        return circle.intersects(this);
    }

    boolean intersects(AlignedBoxCollider2D aabb) {
        // if there is overlap in interval for both x and y, then boxes are colliding
        return overlapOnAxis(aabb, new Vector2(0, 1)) && overlapOnAxis(aabb, new Vector2(1, 0));
    }

    boolean intersects(BoxCollider2D box) {
        // rotate around box center, or origin?
        Vector2[] axes = {
                new Vector2(0, 1), new Vector2(1, 0),
                new Vector2(0, 1).rotate(box.rb.getRotation(), new Vector2()),
                new Vector2(1, 0).rotate(box.rb.getRotation(), new Vector2())
        };

        // top right - min, bottom left - min
        for (Vector2 axis : axes)
            if (!overlapOnAxis(box, axis))
                return false;

        return true;
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
