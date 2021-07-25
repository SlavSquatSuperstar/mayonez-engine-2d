package slavsquatsuperstar.mayonez.physics2d.primitives;

import slavsquatsuperstar.mayonez.Vector2;

/**
 * An object in space with an origin point, a direction, and a length of one (a unit vector).
 *
 * @author SlavSquatSuperstar
 */
public class Ray2D {

    public final Vector2 origin;
    public final Vector2 direction;

    public Ray2D(Vector2 origin, Vector2 direction) {
        this.origin = origin;
        this.direction = direction.unitVector();
    }

    public Ray2D(Edge2D edge) {
        this(edge.start, edge.toVector());
    }

    /**
     * Returns a point along this ray at the specified distance.
     *
     * @param distance parameterized distance
     * @return the point
     */
    public Vector2 getPoint(float distance) {
        return origin.add(direction.mul(distance));
    }

    @Override
    public String toString() {
        return String.format("Origin: %s, Direction, %s", origin, direction);
    }

}
