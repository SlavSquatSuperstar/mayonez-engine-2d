package slavsquatsuperstar.mayonez.physics2d.primitives;

import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.util.MathUtils;

/**
 * A line segment with start and end points.
 *
 * @author SlavSquatSuperstar
 */
public class Edge2D extends Collider2D {

    public final Vector2 start, end;

    public Edge2D(Vector2 start, Vector2 end) {
        this.start = start;
        this.end = end;
    }

    // Properties

    float getSlope() {
        return (end.y - start.y) / (end.x - start.x);
    }

    public Vector2 toVector() {
        return end.sub(start);
    }

    @Override
    public Vector2 center() {
        return new Vector2(start.x + end.x, start.y + end.y).mul(0.5f);
    }

    @Override
    public AlignedBoxCollider2D getMinBounds() {
        AlignedBoxCollider2D aabb = new AlignedBoxCollider2D(toVector());
        Vector2 center = start.add(end).mul(0.5f);
        return aabb.setTransform(new Transform(center)).setRigidBody(new Rigidbody2D(0f));
        // make rigidbody static
    }

    /**
     * Calculates whether the given point is on this line segment.
     *
     * @param point a point in 2D space
     * @return if the point is on the line
     */
    public boolean contains(Vector2 point) {
        // triangle side length or projection distance
        float distToStart = start.distance(point);
        float distToEnd = end.distance(point);
        float length = toVector().len();
        return MathUtils.equals(distToStart + distToEnd, length);
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

    @Override
    public boolean detectCollision(Collider2D collider) {
        return collider.intersects(this);
    }

    @Override
    public boolean intersects(Edge2D edge) {
        // If parallel, must be overlapping (co-linear)
        if (MathUtils.equals(this.getSlope(), edge.getSlope())) {
            return this.contains(edge.start) || this.contains(edge.end) ||
                    edge.contains(this.start) || edge.contains(this.end);
        }

        // Calculate point of intersection using cross product
        Vector2 start1 = this.start;
        Vector2 start2 = edge.start;
        Vector2 line1 = this.toVector();
        Vector2 line2 = edge.toVector();
        float cross = line1.cross(line2);

        float len1 = start2.sub(start1).cross(line2) / cross;
        float len2 = start1.sub(start2).cross(line1) / -cross;

        if (MathUtils.inRange(len1, 0, 1) && MathUtils.inRange(len2, 0, 1)) {
            Vector2 contact = start1.add(line1.mul(len1));
            return true;
        }
        return false;
    }

    @Override
    public Vector2 nearestPoint(Vector2 position) {
        if (contains(position))
            return position;
//        Vector2 inBounds = position.clampInbounds(start, end);
//        return start.add(inBounds.sub(start).project(toVector()));

        float angle = toVector().angle();
        Vector2 localStart = start.rotate(-angle, center());
        Vector2 localEnd = end.rotate(-angle, center());
        Vector2 localPoint = position.rotate(-angle, center());
        Vector2 localNearest = new Vector2(MathUtils.clamp(localPoint.x, localStart.x, localEnd.x), localStart.y);
        return localNearest.rotate(angle, center());

    }

    // TODO implement me
    @Override
    public boolean raycast(Ray2D ray, RaycastResult result, float limit) {
        return false;
    }

    @Override
    public String toString() {
        return String.format("Start: %s, End: %s", start, end);
    }
}
