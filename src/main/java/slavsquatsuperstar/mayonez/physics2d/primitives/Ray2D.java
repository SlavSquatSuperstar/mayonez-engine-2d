package slavsquatsuperstar.mayonez.physics2d.primitives;

import slavsquatsuperstar.mayonez.Vector2;

public class Ray2D {

    public final Vector2 origin, direction;
    private float limit = -1; // Default no limit

    public Ray2D(Vector2 origin, Vector2 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Ray2D(Line2D line) {
        this(line.start, line.toVector().unitVector());
        this.limit = line.length();
    }

    float getLimit() {
        return limit;
    }
}
