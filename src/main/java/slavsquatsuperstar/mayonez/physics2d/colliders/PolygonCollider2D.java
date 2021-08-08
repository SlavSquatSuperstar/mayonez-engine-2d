package slavsquatsuperstar.mayonez.physics2d.colliders;

import org.apache.commons.lang3.ArrayUtils;
import slavsquatsuperstar.math.Mat22;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold;
import slavsquatsuperstar.mayonez.physics2d.RaycastResult;

public abstract class PolygonCollider2D extends Collider2D {

    private final Vec2[] vertices;

    // points in local space (relative to center)

    /**
     * Constructs a convex polygon from an array of vertices in clockwise order.
     *
     * @param vertices the vertices in clockwise order
     */
    public PolygonCollider2D(Vec2... vertices) {
        this.vertices = vertices;
    }

    // Shape Properties

    public float getRotation() {
        return transform.rotation;
    }

    public final int countVertices() {
        return vertices.length;
    }

    public Vec2[] getVertices() { // rotated in world
        Vec2[] results = new Vec2[countVertices()];
        for (int i = 0; i < results.length; i++)
            results[i] = toWorld(vertices[i]);
        return results;
    }

    @Override
    public AlignedBoxCollider2D getMinBounds() { // TODO Support function
        Vec2[] vertices = getVertices();
        float[] verticesX = new float[vertices.length];
        float[] verticesY = new float[vertices.length];

        // Get the min and max coordinates of any point on the box
        for (int i = 0; i < vertices.length; i++) {
            verticesX[i] = vertices[i].x;
            verticesY[i] = vertices[i].y;
        }
        Vec2 newMin = new Vec2(MathUtils.min(verticesX), MathUtils.min(verticesY));
        Vec2 newMax = new Vec2(MathUtils.max(verticesX), MathUtils.max(verticesY));

        Vec2 aabbSize = newMax.sub(newMin).div(transform.scale);
        return new AlignedBoxCollider2D(aabbSize).setTransform(transform).setRigidBody(rb);
    }

    // Intersection Helper Methods

    public Edge2D[] getEdges() { // in world
        Vec2[] vertices = getVertices();
        Edge2D[] edges = new Edge2D[vertices.length];
        for (int i = 0; i < edges.length; i++)
            edges[i] = new Edge2D(vertices[i], vertices[(i + 1) % edges.length]); // Current vertex to next vertex
        return edges;
    }

    public Vec2[] getNormals() { // in world
        Edge2D[] edges = getEdges();
        Vec2[] normals = new Vec2[countVertices()];
        Mat22 rot = new Mat22(0, 1, -1, 0); // Rotate 90 degrees counterclockwise to point outward
        for (int i = 0; i < normals.length; i++)
            normals[i] = rot.times(edges[i].toVector()).unit();
        return normals;
    }

    // Separating-Axis Theorem Methods

    // Project vertices on the axis and find min and max
    protected final MathUtils.Range getIntervalOnAxis(Vec2 axis) {
        Vec2[] vertices = getVertices();
        float[] projections = new float[countVertices()];
        for (int i = 0; i < projections.length; i++)
            projections[i] = vertices[i].dot(axis);
        return new MathUtils.Range(MathUtils.min(projections), MathUtils.max(projections));
    }

    protected final boolean overlapOnAxis(PolygonCollider2D polygon, Vec2 axis) {
        MathUtils.Range thisInterval = this.getIntervalOnAxis(axis);
        MathUtils.Range otherInterval = polygon.getIntervalOnAxis(axis);
        return (thisInterval.min <= otherInterval.max) && (otherInterval.min <= thisInterval.max);
    }

    // Assume overlap on axis
    protected final float getAxisOverlap(PolygonCollider2D polygon, Vec2 axis) {
        MathUtils.Range thisInterval = this.getIntervalOnAxis(axis);
        MathUtils.Range otherInterval = polygon.getIntervalOnAxis(axis);
        return Math.min(Math.abs(thisInterval.min - otherInterval.max), Math.abs(otherInterval.min - thisInterval.max));
    }

    // Polygon vs Point

    @Override
    public boolean contains(Vec2 point) {
        Edge2D[] edges = getEdges();
        for (Edge2D edge : edges) {
            Vec2 edgeLine = edge.toVector();
            float projLength = point.sub(edge.start).projectedLength(edgeLine);
            if (!MathUtils.inRange(projLength, 0, edge.length()))
                return false;
        }
        return true;
    }

    @Override
    public Vec2 nearestPoint(Vec2 position) {
        if (contains(position))
            return position;
        Edge2D[] edges = getEdges();
        float[] distances = new float[edges.length];
        for (int i = 0; i < distances.length; i++)
            distances[i] = position.distance(edges[i].nearestPoint(position));
        Edge2D nearestEdge = edges[MathUtils.minIndex(distances)];
        return nearestEdge.nearestPoint(position);
    }

    // Polygon vs Line

    @Override
    public RaycastResult raycast(Ray2D ray, float limit) {
        Edge2D[] edges = getEdges();
        float[] distances = new float[edges.length];
        for (int i = 0; i < distances.length; i++) {
            RaycastResult rc = edges[i].raycast(ray, limit);
            distances[i] = (rc != null) ? rc.getDistance() : Float.MAX_VALUE;
        }
        int minIndex = MathUtils.minIndex(distances);
        float minDist = distances[minIndex];
        if (minDist == Float.MAX_VALUE) // no successful raycasts
            return null;
        Vec2 normal = edges[minIndex].toVector().rotate(-90).unit();
        return new RaycastResult(ray.getPoint(minDist), normal, minDist);
    }

    // Polygon vs Shape

    @Override
    public boolean detectCollision(Collider2D collider) {
        if (collider instanceof PolygonCollider2D)
            return intersects((PolygonCollider2D) collider);
        return getCollisionInfo(collider) != null;
    }

    // Separating-Axis Theorem
    private boolean intersects(PolygonCollider2D polygon) {
        Vec2[] axes = ArrayUtils.addAll(this.getNormals(), polygon.getNormals());
        for (Vec2 axis : axes)
            if (!overlapOnAxis(polygon, axis))
                return false;
        return true;
    }

    @Override
    public CollisionManifold getCollisionInfo(Collider2D collider) {
        if (collider instanceof CircleCollider)
            return getCollisionInfo((CircleCollider) collider);
//        if (collider instanceof PolygonCollider2D)
//            return getCollisionInfo((PolygonCollider2D) collider);
        return null;
    }
    private CollisionManifold getCollisionInfo(CircleCollider circle) {
        Vec2 closestToCircle = this.nearestPoint(circle.center());
        if (!circle.contains(closestToCircle))
            return null;

        float depth = circle.radius() - closestToCircle.distance(circle.center());
        Vec2 normal = circle.center().sub(closestToCircle).unit();
        CollisionManifold result = new CollisionManifold(this, circle, normal, depth);
        result.addContact(closestToCircle.sub(normal.mul(depth)));
        return result;
    }

}
