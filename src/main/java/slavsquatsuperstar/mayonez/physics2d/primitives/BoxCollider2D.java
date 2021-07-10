package slavsquatsuperstar.mayonez.physics2d.primitives;

import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.util.MathUtils;

/**
 * Represents an oriented bounding box, a rectangle that can be rotated.
 * The sides will align with the object's rotation angle.
 */
public class BoxCollider2D extends AbstractBoxCollider2D {

    public BoxCollider2D(Vector2 size) {
        super(size);
    }

    // TODO world- min/max methods?

    // Properties

    public Vector2[] getVertices() {
        Vector2 min = min();
        Vector2 max = max();
        Vector2[] vertices = new Vector2[]{new Vector2(min), new Vector2(max), new Vector2(min.x, max.y),
                new Vector2(max.x, min.y)};

        if (!MathUtils.equals(getRotation(), 0)) {
            for (int i = 0; i < vertices.length; i++)
                // Rotate a point about the center by a rotation
                vertices[i] = vertices[i].rotate(getRotation(), getCenter());
        }
        return vertices;
    }

    public AlignedBoxCollider2D getMinBounds() {
        AlignedBoxCollider2D aabb;

        if (MathUtils.equals(getRotation(), 0)) {
            aabb = new AlignedBoxCollider2D(size);
        } else {
            Vector2[] vertices = getVertices();
            Vector2 newMin = vertices[0];
            Vector2 newMax = vertices[0];
            for (int i = 1; i < vertices.length; i++) {
                Vector2 v = vertices[i];

                if (v.x < newMin.x)
                    newMin.x = v.x;
                else if (v.x > newMax.x)
                    newMax.x = v.x;

                if (v.y < newMin.y)
                    newMin.y = v.y;
                else if (v.y > newMax.y)
                    newMax.y = v.y;
            }
            aabb = new AlignedBoxCollider2D(newMax.sub(newMin));
        }

        return aabb.setTransform(transform).setRigidBody(rb);
    }

    // Collision Methods

    @Override
    public boolean contains(Vector2 point) {
        // Translate the point into the box's local space
        Vector2 pointLocal = point.rotate(-getRotation(), getCenter());
        return MathUtils.inRange(pointLocal.x, min().x, max().x) && MathUtils.inRange(pointLocal.y, min().y, max().y);
    }

    @Override
    public boolean intersects(Line2D line) {
        float rot = -getRotation();

        // rotate the line into the AABB's local space
        Vector2 localStart = line.start.rotate(rot, getCenter());
        Vector2 localEnd = line.end.rotate(rot, getCenter());
        Line2D localLine = new Line2D(localStart, localEnd);

        // Create AABB with same size
        AlignedBoxCollider2D aabb = new AlignedBoxCollider2D(size);
        aabb.rb = this.rb;
        aabb.transform = this.transform;
        return aabb.intersects(localLine);
    }

    @Override
    public boolean detectCollision(Collider2D collider) {
        if (collider == this)
            return false;

        if (collider instanceof CircleCollider) {
            return intersects((CircleCollider) collider);
        } else if (collider instanceof AlignedBoxCollider2D) {
            return intersects((AlignedBoxCollider2D) collider);
        } else if (collider instanceof BoxCollider2D) {
            return intersects((BoxCollider2D) collider);
        } else {
            return false;
        }
    }

    boolean intersects(CircleCollider circle) {
        return circle.intersects(this);
    }

    boolean intersects(AlignedBoxCollider2D aabb) {
        return aabb.intersects(this);
    }

    boolean intersects(BoxCollider2D box) {
        // rotate around box center, or origin?
        Vector2[] axes = {
                new Vector2(0, 1).rotate(this.getRotation(), new Vector2()),
                new Vector2(1, 0).rotate(this.getRotation(), new Vector2()),
                new Vector2(0, 1).rotate(box.getRotation(), new Vector2()),
                new Vector2(1, 0).rotate(box.getRotation(), new Vector2())
        };

        // top right - min, bottom left - min
        for (Vector2 axis : axes)
            if (!overlapOnAxis(box, axis))
                return false;

        return true;
    }

    @Override
    public boolean raycast(Ray2D ray, RaycastResult result) {
        float rot = -getRotation();

        // Rotate the line into the AABB's local space
        Vector2 localOrigin = ray.origin.rotate(rot, getCenter());
        Vector2 localDir = ray.direction.rotate(rot, getCenter());
        Ray2D localRay = new Ray2D(localOrigin, localDir);

        // Create AABB with same size
        AlignedBoxCollider2D aabb = new AlignedBoxCollider2D(size);
        aabb.rb = this.rb;
        return aabb.raycast(ray, result);

        // Gabe's Method
//        Vector2 xAxis = new Vector2(1, 0).rotate(rot, new Vector2());
//        Vector2 yAxis = new Vector2(0, 1).rotate(rot, new Vector2());
//
//        Vector2 rayToCenter = center().sub(ray.origin);
//        Vector2 f = new Vector2(xAxis.dot(ray.direction), yAxis.dot(ray.direction));
//        Vector2 e = new Vector2(xAxis.dot(rayToCenter), yAxis.dot(rayToCenter));
//
//        Vector2 halfSize = size.div(2);
//        float[] tArr = new float[4];
//        for (int i = 0; i < 2; i++) {
//            if (MathUtils.equals(i, 0f)) { // if perpendicular and not inside box
//                if (e.components()[i] - halfSize.components()[i] > 0 || -e.components()[i] + halfSize.components()[i] < 0)
//                    return false;
//            }
//            tArr[i * 2] = (e.components()[i] + size.components()[i]) / f.components()[i]; // tmax
//            tArr[i * 2 + 1] = (e.components()[i] - size.components()[i]) / f.components()[i]; // tmin
//        }
//
//        float tmin = Math.max(Math.min(tArr[0], tArr[1]), Math.min(tArr[2], tArr[3]));
//        float tmax = Math.min(Math.max(tArr[0], tArr[1]), Math.max(tArr[2], tArr[3]));
//
//        float dist = (tmin < 0f) ? tmax : tmin;
//        boolean hit = dist > 0f; //&& t * t < ray.getMaximum();
//
//        if (result != null) {
//            Vector2 point = ray.origin.add(ray.direction.mul(dist));
//            Vector2 normal = point.sub(center()).unit();
//            result.set(point, normal, dist, hit);
//        }
//
//        return hit;
    }

    public float getRotation() {
        return transform.rotation;
    }
}
