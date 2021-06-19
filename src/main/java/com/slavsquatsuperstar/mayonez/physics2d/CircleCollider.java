package com.slavsquatsuperstar.mayonez.physics2d;

import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.util.MathUtils;

// TODO scale with transform
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
    // Projection
    // Source: https://www.youtube.com/watch?v=Yx1fo2YLJOs
    public boolean intersects(Line2D line) {
        // Return true if either endpoint is in the circle
        if (contains(line.start()) || contains(line.end()))
            return true;

//        return raycast(new Ray2D(line.start(), line.direction()), null);

        // Project the segment from start to center onto the original line object
        Vector2 projectFrom = center().sub(line.start());
        Vector2 projectOnto = line.toVector();
        Vector2 projected = projectOnto.mul(projectFrom.dot(projectOnto) / projectOnto.lengthSquared());

        // Check if the point on the line closest to the center is inside the circle
        Vector2 closest = line.start().add(projected);
        return contains(closest);

//		// Get the percentage of the line segment shared with the circle
//		float t = cntrToStart.dot(toProject) / toProject.dot(toProject); // parameterized position
//		if (t < 0f || t > 1f)
//			return false;
//
//		// Find the point closest to the segment (endpoint of projected line)
//		Vector2 closest = line.start().add(toProject).mul(t);
//		return contains(closest);
    }

    // Raycast
    // Source: https://www.youtube.com/watch?v=23kTf-36Fcw
//    public boolean raycast(Ray2D ray, RaycastResult result) {
//        Vector2 originToCenter = center().sub(ray.origin);
//        float radiusSquared = radius * radius;
//        float lengthSquared = originToCenter.lengthSquared();
//
//        // Project vector
//        float a = originToCenter.dot(ray.direction);
//        float bSq = lengthSquared - a * a;
//        if (radiusSquared - bSq < 0f) // dot result is negative, don't want imaginary
//            return false;
//
//        float f = (float) Math.sqrt(radiusSquared - bSq);
//        float t; // unit lengths along projected vector
//        if (lengthSquared < radiusSquared) // ray starts inside circle
//            t = a + f;
//        else
//            t = a - f;
//
//        if (result != null) {
//            Vector2 point = ray.origin.add(ray.direction.mul(t));
//            Vector2 normal = point.sub(center()).unit();
//            result.set(point, normal, t, true);
//        }
//
//        return true; // ray will intersect circle eventually
//    }

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
        Vector2 min = box.localMin();
        Vector2 max = box.localMax();

        Vector2 localRadius = center().sub(box.center());
        localRadius = localRadius.rotate(box.rb.rotation(), new Vector2());

        Vector2 closest = new Vector2(center());
        closest.x = MathUtils.clamp(closest.x, min.x, max.x);
        closest.y = MathUtils.clamp(closest.y, min.y, max.y);

        float distanceSquared = center().sub(closest).lengthSquared();
        return distanceSquared <= radius * radius;
    }

}
