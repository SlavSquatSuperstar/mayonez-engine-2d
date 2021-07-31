package slavsquatsuperstar.mayonez.physics2d.colliders;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;

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

    public float getLength() {
        return start.distance(end);
    }

    public Vec2 toVector() {
        return end.sub(start);
    }

    public Edge2D getReverse() {
        return new Edge2D(end, start);
    }

    public Vec2 center() {
        return new Vec2(start.x + end.x, start.y + end.y).mul(0.5f);
    }

    // Line vs Points

    /**
     * Calculates whether the given point is on this line segment.
     *
     * @param point a point in 2D space
     * @return if the point is on the line
     */
    // TODO project on point and find t, or is that too expensive?
    public boolean contains(Vec2 point) {
        // triangle side length or projection distance
        float distToStart = start.distance(point);
        float distToEnd = end.distance(point);
        float length = toVector().len();
        return MathUtils.equals(distToStart + distToEnd, length);
    }

    public Vec2 nearestPoint(Vec2 position) {
        if (contains(position))
            return position;
//        Vector2 inBounds = position.clampInbounds(start, end);
//        return start.add(inBounds.sub(start).project(toVector()));

        float angle = toVector().angle();
        Vec2 localStart = start.rotate(-angle, center());
        Vec2 localEnd = end.rotate(-angle, center());
        Vec2 localPoint = position.rotate(-angle, center());
        Vec2 localNearest = new Vec2(MathUtils.clamp(localPoint.x, localStart.x, localEnd.x), localStart.y);
        return localNearest.rotate(angle, center());
    }

    // Line vs Line Methods

    public boolean intersects(Edge2D edge) {
        // If parallel, must be overlapping (co-linear)
        if (MathUtils.equals(this.getSlope(), edge.getSlope())) {
            return this.contains(edge.start) || this.contains(edge.end) ||
                    edge.contains(this.start) || edge.contains(this.end);
        }

        // Calculate point of intersection using cross product
        Vec2 start1 = this.start;
        Vec2 start2 = edge.start;
        Vec2 line1 = this.toVector();
        Vec2 line2 = edge.toVector();
        float cross = line1.cross(line2);

        float len1 = start2.sub(start1).cross(line2) / cross;
        float len2 = start1.sub(start2).cross(line1) / -cross;

        if (MathUtils.inRange(len1, 0, 1) && MathUtils.inRange(len2, 0, 1)) {
            Vec2 contact = start1.add(line1.mul(len1));
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Start: %s, End: %s", start, end);
    }
}
