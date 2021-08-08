package slavsquatsuperstar.mayonez.physics2d.colliders;

import org.apache.commons.lang3.ArrayUtils;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold;
import slavsquatsuperstar.mayonez.physics2d.RaycastResult;

/**
 * An oriented bounding box (OBB), a rectangle that can be rotated. The sides will align with the object's rotation
 * angle.
 *
 * @author SlavSquatSuperstar
 */
public class BoxCollider2D extends PolygonCollider2D {

    private final Vec2 size;

    private BoxCollider2D(Vec2 min, Vec2 max) {
        super(new Vec2(min), new Vec2(max.x, min.y), new Vec2(max), new Vec2(min.x, max.y));
        this.size = max.sub(min);
    }

    public BoxCollider2D(Vec2 size) {
        this(size.div(-2), size.div(2));
    }

    // Shape Properties

    /**
     * Calculates the dimensions of this box factoring in the object's scale.
     *
     * @return the size in the world
     */
    public Vec2 size() {
        return size.mul(transform.scale);
    }

    public float width() {
        return size().x;
    }

    public float height() {
        return size().y;
    }

    /**
     * Returns the unscaled size of this box.
     *
     * @return the size in local space
     */
    // TODO parent send scale event to modify size directly
    protected Vec2 localSize() {
        return size;
    }

    // unrotated top left in local space
    protected Vec2 localMin() {
        return size.div(-2);
    }

    // unrotated bottom right in local space
    protected Vec2 localMax() {
        return size.div(2);
    }

    public float getRotation() {
        return transform.rotation;
    }

    @Override
    public Vec2[] getNormals() {
        return new Vec2[]{new Vec2(1, 0).rotate(getRotation()), new Vec2(0, 1).rotate(getRotation())};
    }

    // Shape vs Point

    @Override
    public boolean contains(Vec2 point) {
        Vec2 pointLocal = toLocal(point); // Rotate the point into the box's local space
        return MathUtils.inRange(pointLocal.x, localMin().x, localMax().x) && MathUtils.inRange(pointLocal.y, localMin().y, localMax().y);
    }

    @Override
    public Vec2 nearestPoint(Vec2 position) {
        if (contains(position))
            return position;
        // Transform the point into local space, clamp it, and then transform it back
        return toWorld(toLocal(position).clampInbounds(localMin(), localMax()));
    }

    // Shape vs Line

    @Override
    public RaycastResult raycast(Ray2D ray, float limit) {
        // Transform the ray to local space (but just rotate direction)
        Ray2D localRay = new Ray2D(toLocal(ray.origin), ray.direction.rotate(-getRotation()));
        float localLimit = ray.direction.mul(limit).div(transform.scale).len();

        // Parametric distance to min/max x and y axes of box
        Vec2 tNear = localMin().sub(localRay.origin).div(localRay.direction);
        Vec2 tFar = localMax().sub(localRay.origin).div(localRay.direction);

        // Swap near and far components if they're out of order
        if (tNear.x > tFar.x) {
            float temp = tNear.x;
            tNear.x = tFar.x;
            tFar.x = temp;
        }
        if (tNear.y > tFar.y) {
            float temp = tNear.y;
            tNear.y = tFar.y;
            tFar.y = temp;
        }

        if (tNear.x > tFar.y || tNear.y > tFar.x) // No intersection
            return null;

        // Parametric distances to near and far contact
        float tHitNear = Math.max(tNear.x, tNear.y);
        float tHitFar = Math.min(tFar.x, tFar.y);
//        float tHitNear = Math.max(Math.min(tNear.x, tFar.x), Math.min(tNear.y, tFar.y));
//        float tHitFar = Math.min(Math.max(tNear.x, tFar.x), Math.max(tNear.y, tFar.y));

        if (tHitFar < 0 || tHitNear > tHitFar) // Ray is pointing away
            return null;

        // If ray starts inside shape, use far for contact
        float distToBox = (tHitNear < 0) ? tHitFar : tHitNear;

        // Is the contact point past the ray limit?
        if (localLimit > 0 && distToBox > localLimit)
            return null;

        Vec2 normal = new Vec2(); // Use (0, 0) for diagonal collision
        if (tNear.x > tNear.y) // Horizontal collision
            normal = new Vec2(-Math.signum(localRay.direction.x), 0);
        else if (tNear.x < tNear.y) // Vertical collision
            normal = new Vec2(0, -Math.signum(localRay.direction.y));

        Vec2 contact = toWorld(localRay.getPoint(distToBox));
        return new RaycastResult(contact, normal.rotate(getRotation()), contact.sub(ray.origin).len());
    }

    // Shape vs Shape

    @Override
    public CollisionManifold getCollisionInfo(Collider2D collider) {
        if (collider instanceof BoxCollider2D)
            return getCollisionInfo((BoxCollider2D) collider);
        return super.getCollisionInfo(collider);
    }

    private CollisionManifold getCollisionInfo(BoxCollider2D box) {
        Vec2[] normalsA = this.getNormals();
        Vec2[] normalsB = box.getNormals();
        if (!detectCollision(box))
            return null;

        // SAT: Find axis with minimum overlap
        Vec2[] axes = ArrayUtils.addAll(normalsA, normalsB);
        float[] overlaps = new float[axes.length];
        for (int i = 0; i < overlaps.length; i++)
            overlaps[i] = getAxisOverlap(box, axes[i]);
        int minOverlapIndex = MathUtils.minIndex(overlaps);

        float overlap = overlaps[minOverlapIndex];
        Vec2 axis = axes[minOverlapIndex];
        Vec2 dist = box.center().sub(this.center());
        Vec2 normal = dist.project(axis).unit();
        Vec2 side = normal.rotate(90);
        MathUtils.Range penetration = new MathUtils.Range(box.getIntervalOnAxis(normal).min, this.getIntervalOnAxis(normal).max);

        CollisionManifold collision = new CollisionManifold(this, box, normal, overlap);
        if (MathUtils.equals(this.getRotation() % 90, box.getRotation() % 90)) { // Orthogonal intersection = 2 contact points
            MathUtils.Range sideA = this.getIntervalOnAxis(side); // vertices projected onto side normal
            MathUtils.Range sideB = box.getIntervalOnAxis(side);
            MathUtils.Range collisionFace = new MathUtils.Range(Math.max(sideA.min, sideB.min),
                    Math.min(sideA.max, sideB.max));

            Vec2 faceA = normal.mul(penetration.min);
            collision.addContact(faceA.add(side.mul(collisionFace.max)));
            collision.addContact(faceA.add(side.mul(collisionFace.min)));
        } else { // Diagonal intersection = 1 contact point
            float dotWithAxis = normal.dot(new Vec2(1, 0).rotate(getRotation()));
            // Other box is inside this, find the furthest vertex inside this box
            if (MathUtils.equals(dotWithAxis, 0) || MathUtils.equals(dotWithAxis, 1) || MathUtils.equals(dotWithAxis, -1)) {
//            if (ArrayUtils.contains(normalsA, normal)) {
                Vec2[] vertices = box.getVertices();
                float[] projections = new float[vertices.length];
                for (int i = 0; i < projections.length; i++)
                    projections[i] = vertices[i].dot(normal);
                int minProjIndex = MathUtils.minIndex(projections);
                float height = vertices[minProjIndex].projectedLength(side);

                collision.addContact(normal.mul(penetration.min).add(side.mul(height)));
            } else { // This box inside other box, find vertex furthest inside other box
                Vec2[] vertices = this.getVertices();
                float[] projections = new float[vertices.length];
                for (int i = 0; i < projections.length; i++)
                    projections[i] = vertices[i].dot(normal);
                int maxProjIndex = MathUtils.maxIndex(projections);
                float height = vertices[maxProjIndex].projectedLength(side);

                collision.addContact(normal.mul(penetration.max).add(side.mul(height)));
            }
        }
        return collision;
    }
}
