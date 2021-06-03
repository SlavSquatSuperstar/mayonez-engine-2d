package com.slavsquatsuperstar.mayonez.physics2d;

import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.util.MathUtil;

// Oriented Bounding Box (can be rotated)
public class BoxCollider2D extends Collider2D {

    private Vector2 size;

    public BoxCollider2D(Vector2 size) {
        this.size = size;
    }

    public Vector2 min() {
        return rb.position();
    }

    public Vector2 max() {
        return rb.position().add(size);
    }

    public Vector2 size() {
        return size;
    }

    @Override
    public Vector2 center() {
        return min().add(max().div(2));
    }

    public Vector2[] getVertices() {
        Vector2 min = min();
        Vector2 max = max();
        Vector2[] vertices = new Vector2[]{new Vector2(min), new Vector2(max), new Vector2(min.x, max.y),
                new Vector2(max.x, min.y)};

        // floating point inaccuracy, solve with epsilon comparison
        if (MathUtil.equals(rb.rotation, 0f)) {
            for (Vector2 v : vertices) {
                // Rotate a point about the center by a rotation
                v = v.rotate(rb.rotation, center());
            }
        }

        return vertices;
    }

    @Override
    public boolean contains(Vector2 point) {
        // Translate the point into the box's local space
        Vector2 pointLocal = new Vector2(point);
        pointLocal = pointLocal.rotate(rb.rotation, rb.position());

        Vector2 min = min();
        Vector2 max = max();

        return pointLocal.x <= max.x && min.x <= pointLocal.x && pointLocal.y <= max.y && min.y <= pointLocal.y;
    }

    @Override
    public boolean intersects(Line2D l) {
        float rot = -rb.rotation;
        Vector2 localStart = l.start().rotate(rot, center());
        Vector2 localEnd = l.end().rotate(rot, center());

        Line2D localLine = new Line2D(localStart, localEnd);
        AlignedBoxCollider2D aabb = new AlignedBoxCollider2D(max().sub(min()));
        aabb.rb = this.rb;
        return aabb.intersects(localLine);
    }
}
