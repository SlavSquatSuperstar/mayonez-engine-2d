package com.slavsquatsuperstar.mayonez.physics2d;

import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.util.MathUtil;

public class CircleCollider extends Collider2D {

    public float radius;

    public CircleCollider(float radius) {
        this.radius = radius;
    }

    public Vector2 center() {
        return transform.position.add(new Vector2(radius, radius));
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

    public boolean intersects(CircleCollider circle) {
        float distSquared = this.center().sub(circle.center()).lengthSquared();
        float radiiSum = this.radius + circle.radius;
        return distSquared <= radiiSum * radiiSum;
    }

    public boolean intersects(AlignedBoxCollider2D aabb) {
        Vector2 min = aabb.min();
        Vector2 max = aabb.max();

        Vector2 closest = new Vector2(center());
        closest.x = MathUtil.clamp(closest.x, min.x, max.x);
        closest.y = MathUtil.clamp(closest.y, min.y, max.y);

        float distanceSquared = center().sub(closest).lengthSquared();
        return distanceSquared <= radius * radius;
    }

    public boolean intersects(BoxCollider2D box) {
        Vector2 min = box.localMin();
        Vector2 max = box.localMax();

        Vector2 localRadius = center().sub(box.center());
        localRadius = localRadius.rotate(box.rb.rotation(), new Vector2());

        Vector2 closest = new Vector2(center());
        closest.x = MathUtil.clamp(closest.x, min.x, max.x);
        closest.y = MathUtil.clamp(closest.y, min.y, max.y);

        float distanceSquared = center().sub(closest).lengthSquared();
        return distanceSquared <= radius * radius;
    }

}
