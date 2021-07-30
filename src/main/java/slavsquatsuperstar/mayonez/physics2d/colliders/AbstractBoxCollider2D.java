package slavsquatsuperstar.mayonez.physics2d.colliders;

import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.util.MathUtils;

abstract class AbstractBoxCollider2D extends Collider2D {

    protected Vector2 size;

    public AbstractBoxCollider2D(Vector2 size) {
        this.size = size;
    }

    // Properties

    public float width() {
        return size.x;
    }

    public float height() {
        return size.y;
    }

    public float area() {
        return width() * height();
    }

    // unrotated top left in world coords
    public Vector2 min() {
        return center().sub(size.mul(0.5f));
    }

    // unrotated bottom right in world coords
    public Vector2 max() {
        return center().add(size.mul(0.5f));
    }

    public abstract Vector2[] getVertices();

    protected final MathUtils.Range getIntervalOnAxis(Vector2 axis) {
        Vector2[] vertices = getVertices();
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

    protected final boolean overlapOnAxis(AbstractBoxCollider2D box, Vector2 axis) {
        MathUtils.Range thisInterval = this.getIntervalOnAxis(axis);
        MathUtils.Range otherInterval = box.getIntervalOnAxis(axis);
        return (thisInterval.min <= otherInterval.max) && (otherInterval.min <= thisInterval.max);
    }

    protected final float getAxisOverlap(AbstractBoxCollider2D box, Vector2 axis) {
        MathUtils.Range thisInterval = this.getIntervalOnAxis(axis);
        MathUtils.Range otherInterval = box.getIntervalOnAxis(axis);
        return Math.min(Math.abs(thisInterval.min - otherInterval.max), Math.abs(otherInterval.min - thisInterval.max));
    }

}
