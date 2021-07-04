package slavsquatsuperstar.mayonez.components.scripts;

import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.primitives.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.Collider2D;
import slavsquatsuperstar.util.MathUtils;

/**
 * Performs edge-handling for objects
 */
public class KeepInScene extends Script {

    public float minX, minY, maxX, maxY;
    private Mode mode;
    private AlignedBoxCollider2D objectCollider;

    public KeepInScene(float minX, float minY, float maxX, float maxY, Mode mode) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.mode = mode;
    }

    @Override
    public void start() {
        try {
            objectCollider = parent.getComponent(Collider2D.class).getMinBounds();
        } catch (NullPointerException e) {
            Logger.log("Script KeepInScene needs a collider to function!");
            objectCollider = new AlignedBoxCollider2D(new Vector2());
            objectCollider.setTransform(parent.transform);
        }
    }

    @Override
    public void update(float dt) {
        Vector2 boxMin = objectCollider.min();
        Vector2 boxMax = objectCollider.max();
        // TODO preferably create a method for resolving collisions between shapes and lines
        // TODO announce collisions
        switch (mode) {
            case STOP:
            case BOUNCE:
                if (boxMin.x < minX)
                    parent.setX(minX + objectCollider.width() / 2);
                else if (boxMax.x > maxX)
                    parent.setX(maxX - objectCollider.width() / 2);

                if (boxMin.y < minY)
                    parent.setY(minY + objectCollider.height() / 2);
                else if (boxMax.y > maxY)
                    parent.setY(maxY - objectCollider.height() / 2);
                break;
            case WRAP:
                if (boxMax.x < minX)
                    parent.setX(maxX + objectCollider.width() / 2);
                else if (boxMin.x > maxX)
                    parent.setX(minX - objectCollider.width() / 2);
                if (boxMax.y < minY)
                    parent.setY(maxY + objectCollider.height() / 2);
                else if (boxMin.y > maxY)
                    parent.setY(this.minY - objectCollider.height() / 2);
                break;
            case DELETE:
                if (!MathUtils.inRange(boxMin.x, minX - objectCollider.width(), maxX) ||
                        !MathUtils.inRange(boxMin.y, minY - objectCollider.height(), maxY))
                    parent.destroy();
                break;
        }

    }

    public enum Mode {
        /**
         * Set objects' speed to 0 when touching an edge.
         */
        STOP,
        /**
         * Make objects bounce back when touching an edge.
         */
        BOUNCE,
        /**
         * Move objects to the opposite side when touching an edge.
         */
        WRAP,
        /**
         * Remove objects from the scene when touching an edge.
         */
        DELETE
    }
}
