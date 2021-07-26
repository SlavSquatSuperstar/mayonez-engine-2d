package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.Collider2D;
import slavsquatsuperstar.util.MathUtils;

/**
 * Dictates what happens when an object reaches the edge of the scene.
 *
 * @author SlavSquatSuperstar
 */
// TODO create line objects in scene and detect collision with tag "bounds"
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
            if (mode == Mode.BOUNCE || mode == Mode.STOP) {
                rb = objectCollider.getRigidbody();
                if (rb == null)
                    mode = Mode.STOP;
            }
        } catch (NullPointerException e) {
            Logger.warn("%s needs a collider to function!", this);
            objectCollider = new AlignedBoxCollider2D(new Vector2());
            objectCollider.setTransform(transform);
        }
    }

    @Override
    public void update(float dt) {
        Vector2 boxMin = objectCollider.min();
        Vector2 boxMax = objectCollider.max();

        Logger.log("Position: %s", transform.position);

        // Edge Checking for x
        // Skip checking if still in scene bounds
        if (!MathUtils.inRange(boxMin.x, minX, maxX - objectCollider.width())) {
            // Detect if colliding with edge
            if (boxMin.x < minX)
                onReachLeft();
            else if (boxMax.x > maxX)
                onReachRight();

            // Detect if moved completely past edge
            if (boxMax.x < minX)
                onPassLeft();
            else if (boxMin.x > maxX)
                onPassRight();
        }

        // Edge Checking for y
        if (!MathUtils.inRange(boxMin.y, minY, maxY - objectCollider.height())) {
            if (boxMin.y < minY)
                onReachTop();
            else if (boxMax.y > maxY)
                onReachBottom();

            if (boxMax.y < minY)
                onPassTop();
            else if (boxMin.y > maxY)
                onPassBottom();
        }
    }

    // Collision Event Methods

    public void onReachLeft() {
        switch (mode) {
            case STOP: // Set velocity to 0 if stopping
                if (rb != null)
                    rb.velocity().x = 0;
                break;
            case BOUNCE: // Reverse velocity if bouncing
                rb.velocity().x *= -objectCollider.getBounce();
                break;
            default: // Stop if neither
                return;
        }
        parent.setX(minX + objectCollider.width() * 0.5f); // Align with edge of screen for both
    }

    public void onReachRight() {
        switch (mode) {
            case STOP:
                if (rb != null)
                    rb.velocity().x = 0;
                break;
            case BOUNCE:
                rb.velocity().x *= -objectCollider.getBounce();
                break;
            default:
                return;
        }
        parent.setX(maxX - objectCollider.width() * 0.5f);
    }

    public void onReachTop() {
        switch (mode) {
            case STOP:
                if (rb != null)
                    rb.velocity().y = 0;
                break;
            case BOUNCE:
                rb.velocity().y *= -objectCollider.getBounce();
                break;
            default:
                return;
        }
        parent.setY(minY + objectCollider.height() * 0.5f);

    }

    public void onReachBottom() {
        switch (mode) {
            case STOP:
                if (rb != null)
                    rb.velocity().y = 0;
                break;
            case BOUNCE:
                rb.velocity().y *= -objectCollider.getBounce();
                break;
            default:
                return;
        }
        parent.setY(maxY - objectCollider.height() * 0.5f);
    }

    public void onPassLeft() {
        if (mode == Mode.WRAP)
            parent.setX(maxX + objectCollider.width() * 0.5f);
        else if (mode == Mode.DELETE)
            parent.destroy();
    }

    public void onPassRight() {
        if (mode == Mode.WRAP)
            parent.setX(minX - objectCollider.width() * 0.5f);
        else if (mode == Mode.DELETE)
            parent.destroy();
    }

    public void onPassTop() {
        if (mode == Mode.WRAP)
            parent.setY(maxY + objectCollider.height() * 0.5f);
        else if (mode == Mode.DELETE)
            parent.destroy();
    }

    public void onPassBottom() {
        if (mode == Mode.WRAP)
            parent.setY(minY - objectCollider.height() * 0.5f);
        else if (mode == Mode.DELETE)
            parent.destroy();
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

    private enum Direction {
        LEFT, RIGHT, TOP, BOTTOM
    }
}
