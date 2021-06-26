package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.util.MathUtils;

public class Line2D {

    private Vector2 start, end;

    public Line2D(Vector2 start, Vector2 end) {
        this.start = start;
        this.end = end;
    }

    public Vector2 start() {
        return start;
    }

    public Vector2 end() {
        return end;
    }

    public Vector2 toVector() {
        return end.sub(start);
    }

    // TODO intersects another line? intermediate value theorem

    public boolean contains(Vector2 point) {
        float slope = (end.y - start.y) / (end.x - start.x);
        if (Float.isInfinite(slope)) { // vertical line (just compare x-values)
            return MathUtils.equals(point.x, start.x);
        } else {
            float yInt = end.y - (slope * end.x);
            return MathUtils.equals(point.y, yInt + point.x * slope);
        }
    }

    @Override
    public String toString() {
        return String.format("Start: %s, End: %s", start, end);
    }
}
