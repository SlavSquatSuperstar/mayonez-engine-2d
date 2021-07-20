package slavsquatsuperstar.mayonez.components.scripts;

import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
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
    private Rigidbody2D rb = null;

    public KeepInScene(Scene scene, Mode mode) { // Use scene bounds
        this(0, 0, scene.getWidth(), scene.getHeight(), mode);
    }

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
            if (mode == Mode.BOUNCE) {
                rb = objectCollider.getRigidbody();
                if (rb == null)
                    mode = Mode.STOP;
            }
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
        // TODO use line vs shape detection and announce collisions
        // ex: on collide left, set vel.x to 0 or inverse vel.x
        switch (mode) {
            case STOP:
                if (boxMin.x < minX)
                    parent.setX(minX + objectCollider.width() * 0.5f);
                else if (boxMax.x > maxX)
                    parent.setX(maxX - objectCollider.width() * 0.5f);

                if (boxMin.y < minY)
                    parent.setY(minY + objectCollider.height() * 0.5f);
                else if (boxMax.y > maxY)
                    parent.setY(maxY - objectCollider.height() * 0.5f);
                break;
            case BOUNCE:
                float bounce = -objectCollider.getBounce();
                if (boxMin.x < minX) {
                    parent.setX(minX + objectCollider.width() * 0.5f);
                    rb.velocity().x *= bounce;
                } else if (boxMax.x > maxX) {
                    parent.setX(maxX - objectCollider.width() * 0.5f);
                    rb.velocity().x *= bounce;
                }

                if (boxMin.y < minY) {
                    parent.setY(minY + objectCollider.height() * 0.5f);
                    rb.velocity().y *= bounce;
                } else if (boxMax.y > maxY) {
                    parent.setY(maxY - objectCollider.height() * 0.5f);
                    rb.velocity().y *= bounce;
                }
                break;
            case WRAP:
                if (boxMax.x < minX)
                    parent.setX(maxX + objectCollider.width() * 0.5f);
                else if (boxMin.x > maxX)
                    parent.setX(minX - objectCollider.width() * 0.5f);
                if (boxMax.y < minY)
                    parent.setY(maxY + objectCollider.height() * 0.5f);
                else if (boxMin.y > maxY)
                    parent.setY(minY - objectCollider.height() * 0.5f);
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
