package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.util.MathUtils;

// TODO scale with transform
// TODO local instead of world space?
public class CircleCollider extends Collider2D {

    public float radius;

    public CircleCollider(float radius) {
        this.radius = radius;
    }

    @Override
    public boolean contains(Vector2 point) {
        return point.sub(center()).lengthSquared() <= radius * radius;
    }

    @Override
    // TODO put in superclass?
    public boolean intersects(Line2D line) {
        if (contains(line.start) || contains(line.end))
            return true;
        return raycast(new Ray2D(line), null);
    }

    public boolean intersects(CircleCollider circle) {
        float distSquared = this.center().sub(circle.center()).lengthSquared();
        float radiiSum = this.radius + circle.radius;
        return distSquared <= radiiSum * radiiSum;
    }

    public boolean intersects(AlignedBoxCollider2D aabb) {
        Vector2 min = aabb.min();
        Vector2 max = aabb.max();

        Vector2 closest = new Vector2(center());
        closest.x = MathUtils.clamp(closest.x, min.x, max.x);
        closest.y = MathUtils.clamp(closest.y, min.y, max.y);

        float distanceSquared = center().sub(closest).lengthSquared();
        return distanceSquared <= radius * radius;
    }

    public boolean intersects(BoxCollider2D box) {
        Vector2 min = box.min();
        Vector2 max = box.max();

        Vector2 localRadius = center().sub(box.center());
        localRadius = localRadius.rotate(box.rb.rotation(), new Vector2());

        Vector2 closest = new Vector2(center());
        closest.x = MathUtils.clamp(closest.x, min.x, max.x);
        closest.y = MathUtils.clamp(closest.y, min.y, max.y);

        float distanceSquared = center().sub(closest).lengthSquared();
        return distanceSquared <= radius * radius;
    }

    @Override
    public boolean raycast(Ray2D ray, RaycastResult result) {
        RaycastResult.reset(result);

        // Trace the ray's origin to the circle's center
        Vector2 originToCenter = center().sub(ray.origin);
        float radiusSquared = radius * radius;
        float lengthSquared = originToCenter.lengthSquared();

        // TODO create nearestToPoint() method
        // Project originToCenter onto the ray
        float projLength = originToCenter.dot(ray.direction);
        float distNearest = lengthSquared - projLength * projLength; // Closest distance from center to extended ray
        if (radiusSquared - distNearest < 0f) // dot result is negative, don't want imaginary
            return false;

        float f = (float) Math.sqrt(radiusSquared - distNearest);
        // unit lengths along projected vector
        float distToCircle = contains(ray.origin) ? projLength + f : projLength - f;

        boolean hit = ray.getLimit() <= -1 || distToCircle <= ray.getLimit(); // limit ray if constructed from line

        if (result != null) {
            Vector2 point = ray.origin.add(ray.direction.mul(distToCircle));
            Vector2 normal = point.sub(center()).unit();
            result.set(point, normal, distToCircle, hit);
        }

        return hit;
    }

}
