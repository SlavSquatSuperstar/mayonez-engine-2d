package slavsquatsuperstar.mayonez.physics2d.colliders;

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

    public Vec2[] getVertices() { // in world
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

    public Vec2[] getNormals() { // in world
        Vec2[] vertices = getVertices();
        Vec2[] normals = new Vec2[countVertices()];
        for (int i = 0; i < normals.length; i++) {
            Vec2 edge = vertices[(i + 1) % normals.length].sub(vertices[i]); // Next vertex - current vertex
            normals[i] = edge.rotate(-90).unitVector(); // Rotate 90 degrees clockwise to point outward
        }
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
        return false;
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
        return false;
    }
}
