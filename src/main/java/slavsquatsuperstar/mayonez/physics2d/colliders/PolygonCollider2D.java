package slavsquatsuperstar.mayonez.physics2d.colliders;

import org.apache.commons.lang3.ArrayUtils;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;

public abstract class PolygonCollider2D extends Collider2D {

    private final Vec2[] vertices;

    // points in local space (relative to center)

    /**
     * Constructs a convex polygon from an array of vertices in counterclockwise order.
     *
     * @param vertices the vertices in counterclockwise order
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
            results[i] = transform.toWorld(vertices[i]);
        return results;
    }

    @Override
    public AlignedBoxCollider2D getMinBounds() {
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

    // Separating-Axis Theorem Methods

    public Edge2D[] getEdges() { // in world
        Vec2[] vertices = getVertices();
        Edge2D[] edges = new Edge2D[countVertices()];
        for (int i = 0; i < edges.length; i++)
            edges[i] = new Edge2D(vertices[i], vertices[(i + 1) % edges.length]); // Current vertex to next vertex
        return edges;
    }

    public Vec2[] getNormals() { // in world
        Edge2D[] edges = getEdges();
        Vec2[] normals = new Vec2[countVertices()];
        for (int i = 0; i < normals.length; i++)
            normals[i] = edges[i].toVector().rotate(-90).unitVector(); // Rotate 90 degrees clockwise to point outward
        return normals;
    }

    protected final MathUtils.Range getIntervalOnAxis(Vec2 axis) {
        Vec2[] vertices = getVertices();
        MathUtils.Range interval = new MathUtils.Range(0, 0);

        // Project vertices on the axis and find min and max
        interval.max = interval.min = vertices[0].dot(axis.unitVector());
        for (int i = 1; i < vertices.length; i++) {
            float projLength = vertices[i].dot(axis.unitVector());
            if (projLength < interval.min)
                interval.min = projLength;
            if (projLength > interval.max)
                interval.max = projLength;
        }
        return interval;
    }

    protected final boolean overlapOnAxis(PolygonCollider2D box, Vec2 axis) {
        MathUtils.Range thisInterval = this.getIntervalOnAxis(axis);
        MathUtils.Range otherInterval = box.getIntervalOnAxis(axis);
        return (thisInterval.min <= otherInterval.max) && (otherInterval.min <= thisInterval.max);
    }

    protected final float getAxisOverlap(PolygonCollider2D box, Vec2 axis) {
        MathUtils.Range thisInterval = this.getIntervalOnAxis(axis);
        MathUtils.Range otherInterval = box.getIntervalOnAxis(axis);
        return Math.min(Math.abs(thisInterval.min - otherInterval.max), Math.abs(otherInterval.min - thisInterval.max));
    }

    // Polygon vs Point

    @Override
    public boolean contains(Vec2 point) {
        Edge2D[] edges = getEdges();
        for (Edge2D edge : edges) {
            Vec2 edgeLine = edge.toVector();
            Vec2 projectedPoint = point.sub(edge.start).project(edgeLine);
            if (!MathUtils.inRange(projectedPoint.len(), 0, edgeLine.len()))
                return false;
        }
        return true;
    }

    @Override
    public Vec2 nearestPoint(Vec2 position) {
        return null;
    }

    // Polygon vs Line

    @Override
    public boolean intersects(Edge2D edge) {
        return false;
    }

    @Override
    public boolean raycast(Ray2D ray, RaycastResult result, float limit) {
        return false;
    }

    // Polygon vs Shape

    @Override
    public boolean detectCollision(Collider2D collider) {
        if (collider instanceof PolygonCollider2D)
            return this.intersects((PolygonCollider2D) collider);
        // TODO vs circle
        return false;
    }

    // Separating-Axis Theorem
    private boolean intersects(PolygonCollider2D polygon) {
        Vec2[] axes = ArrayUtils.addAll(this.getNormals(), polygon.getNormals());
        for (Vec2 axis : axes)
            if (!overlapOnAxis(polygon, axis))
                return false;
        return true;
    }
}
