package slavsquatsuperstar.mayonez.physics2d.colliders;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.math.MathUtils;

abstract class AbstractBoxCollider2D extends Collider2D {

    private final Vec2 size;

    public AbstractBoxCollider2D(Vec2 size) {
        this.size = size;
    }

    // Properties

    /**
     * Returns the unscaled size of this box.
     *
     * @return the size in local space
     */
    // TODO parent send scale event to modify size directly
    protected Vec2 localSize() {
        return size;
    }

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

    public float area() {
        return width() * height();
    }

    // unrotated top left in world coords
    public Vec2 min() {
        return center().sub(size().div(2));
    }

    // unrotated bottom right in world coords
    public Vec2 max() {
        return center().add(size().div(2));
    }

    public abstract Vec2[] vertices();

    protected final MathUtils.Range getIntervalOnAxis(Vec2 axis) {
        Vec2[] vertices = vertices();
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

    protected final boolean overlapOnAxis(AbstractBoxCollider2D box, Vec2 axis) {
        MathUtils.Range thisInterval = this.getIntervalOnAxis(axis);
        MathUtils.Range otherInterval = box.getIntervalOnAxis(axis);
        return (thisInterval.min <= otherInterval.max) && (otherInterval.min <= thisInterval.max);
    }

    protected final float getAxisOverlap(AbstractBoxCollider2D box, Vec2 axis) {
        MathUtils.Range thisInterval = this.getIntervalOnAxis(axis);
        MathUtils.Range otherInterval = box.getIntervalOnAxis(axis);
        return Math.min(Math.abs(thisInterval.min - otherInterval.max), Math.abs(otherInterval.min - thisInterval.max));
    }
}
