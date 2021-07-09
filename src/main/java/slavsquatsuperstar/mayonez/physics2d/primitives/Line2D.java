package slavsquatsuperstar.mayonez.physics2d.primitives;

import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.util.MathUtils;

// TODO should extend collider?

/**
 * Represents a line segment with start and end points.
 */
public class Line2D extends Collider2D {

    public final Vector2 start, end;

    public Line2D(Vector2 start, Vector2 end) {
        this.start = start;
        this.end = end;
    }

    float getSlope() {
        return (end.y - start.y) / (end.x - start.x);
    }

    public Vector2 toVector() {
        return end.sub(start);
    }

    public AlignedBoxCollider2D getMinBounds() {
        AlignedBoxCollider2D aabb = new AlignedBoxCollider2D(toVector());
        Vector2 center = start.add(end).mul(0.5f);
        aabb.setRigidBody(new Rigidbody2D(0f)); // make static
        aabb.setTransform(new Transform(center));
        return aabb;
    }

    /**
     * Calculates whether the given point is on this line segment.
     *
     * @param point a point in 2D space
     * @return if the point is on the line
     */
    public boolean contains(Vector2 point) {
        // triangle side length or projection distance
        float distToStart = start.sub(point).length();
        float distToEnd = end.sub(point).length();
        float lineLength = toVector().length();
        return distToStart + distToEnd <= lineLength;
    }

    /**
     * Calculates whether the given point is on this line if it were extended infinitely in both directions.
     *
     * @param point a point in 2D space
     * @return if the point is colinear
     */
    public boolean isColinear(Vector2 point) {
        float slope = getSlope();
        if (Float.isInfinite(slope)) { // if vertical line, compare x-values
            return MathUtils.equals(point.x, start.x);
        } else if (MathUtils.equals(slope, 9)) { // if horizontal line, compare y values
            return MathUtils.equals(point.y, start.y);
        } else {
            float yInt = end.y - (slope * end.x);
            return MathUtils.equals(point.y, yInt + point.x * slope);
        }
    }

    public boolean intersects(Line2D line) {
        // If parallel, must be overlapping (co-linear)
        if (MathUtils.equals(this.getSlope(), line.getSlope())) {
            return this.contains(line.start) || this.contains(line.end) ||
                    line.contains(this.start) || line.contains(this.end);
        }

        // Calculate point of intersection using cross product
        Vector2 start1 = this.start;
        Vector2 start2 = line.start;
        Vector2 line1 = this.toVector();
        Vector2 line2 = line.toVector();
        float cross = line1.cross(line2);

        float len1 = start2.sub(start1).cross(line2) / cross;
        float len2 = start1.sub(start2).cross(line1) / -cross;

        if (MathUtils.inRange(len1, 0, 1) && MathUtils.inRange(len2, 0, 1)) {
            Vector2 contact = start1.add(line1.mul(len1));
            return true;
        }
        return false;
    }

    // TODO implement me
    @Override
    public boolean raycast(Ray2D ray, RaycastResult result) {
        return false;
    }

    @Override
    public boolean detectCollision(Collider2D collider) {
        return collider.intersects(this);
    }

    @Override
    public String toString() {
        return String.format("Start: %s, End: %s", start, end);
    }
}
