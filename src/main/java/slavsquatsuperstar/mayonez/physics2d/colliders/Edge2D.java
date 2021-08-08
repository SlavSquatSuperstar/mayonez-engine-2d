package slavsquatsuperstar.mayonez.physics2d.colliders;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.RaycastResult;

/**
 * A 2D line segment with start and end points.
 *
 * @author SlavSquatSuperstar
 */
public class Edge2D {

    public final Vec2 start, end;

    public Edge2D(Vec2 start, Vec2 end) {
        this.start = start;
        this.end = end;
    }

    // Properties

    float getSlope() {
        return (end.y - start.y) / (end.x - start.x);
    }

    public float length() {
        return start.distance(end);
    }

    public Vec2 toVector() {
        return end.sub(start);
    }

    // Line vs Point

    /**
     * Calculates whether the given point is on this line segment.
     *
     * @param point a point in 2D space
     * @return if the point is on the line
     */
    public boolean contains(Vec2 point) {
        if (point.equals(start) || point.equals(end))
            return true;
        return nearestPoint(point).equals(point);
    }

    public Vec2 nearestPoint(Vec2 position) {
        float length = length();
        float projLength = position.sub(start).dot(toVector()) / length; // find point shadow on line
        if (projLength > length) // pat line end
            return end;
        else if (projLength < 0) // behind line start
            return start;
        return start.add(toVector().mul(projLength / length)); // inside line
    }

    // right (clockwise) is positive
    public float distance(Vec2 point) {
        return point.sub(start).projectedLength(toVector().rotate(90));
    }

    /**
     * Returns the distance from a point to this line relative to that of other points, and which side the point is on.
     *
     * @param point a point
     * @return A negative value if the point is to the right of this line, a positive value if the point is to the left
     * of this line, and zero if the point is on the line (if extended infinitely).
     */
    public float pseudoDistance(Vec2 point) {
        return point.sub(start).dot(toVector().rotate(90));
    }

    // Line vs Line

    public boolean intersects(Edge2D edge) {
        // If parallel, must be overlapping (co-linear)
        if (MathUtils.equals(this.getSlope(), edge.getSlope())) {
            return this.contains(edge.start) || this.contains(edge.end) ||
                    edge.contains(this.start) || edge.contains(this.end);
        }
        return raycast(new Ray2D(edge), edge.length()) != null;
    }

    public RaycastResult raycast(Ray2D ray, float limit) {
        float length = this.length();
        Vec2 start1 = this.start;
        Vec2 start2 = ray.origin;

        // Calculate point of intersection using cross product
        Vec2 line1 = this.toVector().div(length);
        Vec2 line2 = ray.direction;
        float cross = line1.cross(line2);

        // Parametric lengths along rays
        float dist1 = start2.sub(start1).cross(line2) / cross;
        float dist2 = start1.sub(start2).cross(line1) / -cross;

        if (!MathUtils.inRange(dist1, 0, length)) // Contact point outside line
            return null;
        if (limit > 0 && !MathUtils.inRange(dist2, 0, limit)) // Contact point exceeds ray limit
            return null;
        else if (dist2 < 0) // Contact point behind ray
            return null;

        Vec2 contact = start1.add(line1.mul(dist1));
        // rotate left or right depending on which side ray started form
        Vec2 normal = line1.rotate(Math.signum(pseudoDistance(start2)) * 90);
        return new RaycastResult(contact, normal, dist1);
    }

    @Override
    public String toString() {
        return String.format("Start: %s, End: %s", start, end);
    }
}
