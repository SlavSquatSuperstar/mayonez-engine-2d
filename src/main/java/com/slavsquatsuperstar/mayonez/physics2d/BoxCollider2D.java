package com.slavsquatsuperstar.mayonez.physics2d;

import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.util.MathUtils;

/**
 * Represents an object-oriented bounding box, a rectangle that can be rotated.
 * The sides will align with the object's rotation angle.
 */
public class BoxCollider2D extends Collider2D {

    private Vector2 size;

    public BoxCollider2D(Vector2 size) {
        this.size = size;
    }

    public float width() {
        return size.x;
    }

    public float height() {
        return size.y;
    }

    public Vector2 localMin() {
        return center().sub(size.div(2f));
    }

    public Vector2 worldMin() {
        return localMin().rotate(transform.rotation, center());
    }

    public Vector2 localMax() {
        return center().add(size.div(2f));
    }

    public Vector2 worldMax() {
        return localMax().rotate(transform.rotation, center());
    }

    public Vector2[] getVertices() {
        Vector2 min = localMin();
        Vector2 max = localMax();
        Vector2[] vertices = new Vector2[]{new Vector2(min), new Vector2(max), new Vector2(min.x, max.y),
                new Vector2(max.x, min.y)};

        // floating point inaccuracy, solve with epsilon comparison
        if (MathUtils.equals(rb.rotation(), 0f)) {
            for (Vector2 v : vertices) {
                // Rotate a point about the center by a rotation
                v = v.rotate(rb.rotation(), center());
            }
        }

        return vertices;
    }

    @Override
    public boolean contains(Vector2 point) {
        // Translate the point into the box's local space
        Vector2 pointLocal = new Vector2(point);
        pointLocal = pointLocal.rotate(rb.rotation(), rb.position());

        Vector2 min = localMin();
        Vector2 max = localMax();

        return pointLocal.x <= max.x && min.x <= pointLocal.x && pointLocal.y <= max.y && min.y <= pointLocal.y;
    }

    @Override
    public boolean intersects(Line2D line) {
        float rot = -rb.rotation();
        Vector2 localStart = line.start().rotate(rot, center());
        Vector2 localEnd = line.end().rotate(rot, center());
        // rotate the line into AABB's local space

        Line2D localLine = new Line2D(localStart, localEnd);
        // Create AABB with same size
        AlignedBoxCollider2D aabb = new AlignedBoxCollider2D(localMax().sub(localMin()));
        aabb.rb = this.rb;
        return aabb.intersects(localLine);
    }

    public boolean intersects(CircleCollider circle) {
        return circle.intersects(this);
    }

    public AlignedBoxCollider2D toAABB() {
        AlignedBoxCollider2D aabb;

        if (MathUtils.equals(rb.rotation(), 0)) {
            aabb = new AlignedBoxCollider2D(size);
        } else {
            Vector2[] vertices = getVertices();
            Vector2 newMin = vertices[0];
            Vector2 newMax = vertices[0];
            for (int i = 1; i < vertices.length; i++) {
                Vector2 v = vertices[i];

                if (v.x < newMin.x)
                    newMin.x = v.x;
                else if (v.x < newMax.x)
                    newMax.x = v.x;

                if (v.y < newMin.y)
                    newMin.y = v.y;
                else if (v.y < newMax.y)
                    newMax.y = v.y;
            }
            aabb = new AlignedBoxCollider2D(newMax.sub(newMin));
        }

        aabb.rb = this.rb;
        aabb.transform = this.transform;
        return aabb;
    }
}
