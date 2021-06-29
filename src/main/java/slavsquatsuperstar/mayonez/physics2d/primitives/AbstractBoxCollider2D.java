package slavsquatsuperstar.mayonez.physics2d.primitives;

import slavsquatsuperstar.mayonez.Vector2;

abstract class AbstractBoxCollider2D extends Collider2D {

    protected Vector2 size;

    public AbstractBoxCollider2D(Vector2 size) {
        this.size = size;
    }

    public float width() {
        return size.x;
    }

    public float height() {
        return size.y;
    }

    // unrotated min, in world coords
    public Vector2 min() {
        return center().sub(size.div(2f));
    }

    public Vector2 max() {
        return center().add(size.div(2f));
    }

    public abstract Vector2[] getVertices();

    private final Vector2 getIntervalOnAxis(Vector2 axis) {
        axis = axis.unit();
        Vector2 result = new Vector2();
        Vector2 vertices[] = getVertices();

        // project vertices on x axis and find min and max
        result.x = vertices[0].dot(axis);
        result.y = result.x;

        // x is min and y is max
        for (int i = 1; i < vertices.length; i++) {
            float projLength = vertices[i].dot(axis);
            if (projLength < result.x)
                result.x = projLength;
            if (projLength > result.y)
                result.y = projLength;
        }

        return result;
    }

    protected final boolean overlapOnAxis(AbstractBoxCollider2D box, Vector2 axis) {
        Vector2 thisInterval = this.getIntervalOnAxis(axis);
        Vector2 otherInterval = box.getIntervalOnAxis(axis);
        return (thisInterval.x <= otherInterval.y) && (otherInterval.x <= thisInterval.y);
    }

}
