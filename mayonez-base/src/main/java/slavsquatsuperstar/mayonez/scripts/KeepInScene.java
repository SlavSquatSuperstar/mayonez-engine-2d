package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.shapes.BoundingRectangle;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.Collider;

/**
 * Dictates what happens when an object reaches the edge of the scene.
 *
 * @author SlavSquatSuperstar
 */
// TODO create line objects in scene and detect collision with tag "bounds"
public class KeepInScene extends Script {

    private final Vec2 minPos, maxPos;
    private Mode mode;
    private Collider objectCollider = null;
    private BoundingRectangle boundingBox;
    private Rigidbody rb = null;

    public KeepInScene(Scene scene, Mode mode) { // Use scene bounds
        this(new Vec2(), scene.getSize(), mode);
    }

    public KeepInScene(Vec2 minPos, Vec2 maxPos, Mode mode) {
        this.minPos = minPos;
        this.maxPos = maxPos;
        this.mode = mode;
    }

    @Override
    public void start() {
        objectCollider = getCollider();
        if (objectCollider == null) {
            Logger.log("%s needs a collider to function!", this);
            mode = Mode.STOP;
            setEnabled(false); // disable script if no collider
        }

        rb = objectCollider.getRigidbody();
        if (rb == null && mode == Mode.BOUNCE) mode = Mode.STOP; // if no rb than can't use physics reactions
    }

    @Override
    public void update(float dt) {
        boundingBox = objectCollider.getMinBounds();
        Vec2 boxMin = boundingBox.min();
        Vec2 boxMax = boundingBox.max();

        // Edge Checking for x
        // Skip checking if still in scene bounds
        if (!MathUtils.inRange(boxMin.x, minPos.x, maxPos.x - boundingBox.height)) {
            // Detect if colliding with edge
            if (boxMin.x < minPos.x)
                onReachLeft();
            else if (boxMax.x > maxPos.x)
                onReachRight();

            // Detect if moved completely past edge
            if (boxMax.x < minPos.x)
                onPassLeft();
            else if (boxMin.x > maxPos.x)
                onPassRight();
        }

        // Edge Checking for y
        if (!MathUtils.inRange(boxMin.y, minPos.y, maxPos.y - boundingBox.height)) {
            if (boxMin.y < minPos.y)
                onReachTop();
            else if (boxMax.y > maxPos.y)
                onReachBottom();

            if (boxMax.y < minPos.y)
                onPassTop();
            else if (boxMin.y > maxPos.y)
                onPassBottom();
        }
    }

    // Collision Event Methods

    public void onReachLeft() {
        switch (mode) {
            case STOP: // Set velocity to 0 if stopping
                if (rb != null) rb.getVelocity().x = 0;
                break;
            case BOUNCE: // Reverse velocity if bouncing
                rb.getVelocity().x *= -objectCollider.getMaterial().getBounce();
                break;
            default: // Stop if neither
                return;
        }
        setX(minPos.x + boundingBox.width * 0.5f); // Align with edge of screen for both
    }

    public void onReachRight() {
        switch (mode) {
            case STOP:
                if (rb != null) rb.getVelocity().x = 0;
                break;
            case BOUNCE:
                rb.getVelocity().x *= -objectCollider.getMaterial().getBounce();
                break;
            default:
                return;
        }
        setX(maxPos.x - boundingBox.width * 0.5f);
    }

    public void onReachTop() {
        switch (mode) {
            case STOP:
                if (rb != null) rb.getVelocity().y = 0;
                break;
            case BOUNCE:
                rb.getVelocity().y *= -objectCollider.getMaterial().getBounce();
                break;
            default:
                return;
        }
        setY(minPos.y + boundingBox.height * 0.5f);

    }

    public void onReachBottom() {
        switch (mode) {
            case STOP:
                if (rb != null) rb.getVelocity().y = 0;
                break;
            case BOUNCE:
                rb.getVelocity().y *= -objectCollider.getMaterial().getBounce();
                break;
            default:
                return;
        }
        setY(maxPos.y - boundingBox.height * 0.5f);
    }

    public void onPassLeft() {
        switch (mode) {
            case WRAP -> setX(maxPos.x + boundingBox.width * 0.5f);
            case DELETE -> parent.destroy();
        }
    }

    public void onPassRight() {
        switch (mode) {
            case WRAP -> setX(minPos.x - boundingBox.width * 0.5f);
            case DELETE -> parent.destroy();
        }
    }

    public void onPassTop() {
        switch (mode) {
            case WRAP -> setY(maxPos.y + boundingBox.height * 0.5f);
            case DELETE -> parent.destroy();
        }
    }

    public void onPassBottom() {
        switch (mode) {
            case WRAP -> setY(minPos.y - boundingBox.height * 0.5f);
            case DELETE -> parent.destroy();
        }
    }

    private void setX(float x) {
        transform.position.x = x;
    }

    private void setY(float y) {
        transform.position.y = y;
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
