package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;

/**
 * Dictates what happens when an object reaches the edge of the scene.
 *
 * @author SlavSquatSuperstar
 */
// TODO create line objects in scene and detect collision with tag "bounds"
public class KeepInScene extends Script {

    public float minX, minY, maxX, maxY;
    private Mode mode;
    private Collider2D objectCollider = null;
    private AlignedBoxCollider2D boundingBox = null;
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
            objectCollider = getCollider();
            if (mode == Mode.BOUNCE || mode == Mode.STOP) {
                rb = objectCollider.getRigidbody();
                if (rb == null)
                    mode = Mode.STOP;
            }
        } catch (NullPointerException e) {
            Logger.log("%s needs a collider to function!", this);
            boundingBox = new AlignedBoxCollider2D(new Vec2());
            boundingBox.setTransform(transform);
            mode = Mode.STOP;
        }
    }

    @Override
    public void update(float dt) {
        if (objectCollider != null)
            boundingBox = objectCollider.getMinBounds();
        Vec2 boxMin = boundingBox.min();
        Vec2 boxMax = boundingBox.max();

        // Edge Checking for x
        // Skip checking if still in scene bounds
        if (!MathUtils.inRange(boxMin.x, minX, maxX - boundingBox.width())) {
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
        if (!MathUtils.inRange(boxMin.y, minY, maxY - boundingBox.height())) {
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
                if (rb != null) rb.getVelocity().x = 0;
                break;
            case BOUNCE: // Reverse velocity if bouncing
                rb.getVelocity().x *= -objectCollider.getMaterial().getBounce();
                break;
            default: // Stop if neither
                return;
        }
        setX(minX + boundingBox.width() * 0.5f); // Align with edge of screen for both
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
        setX(maxX - boundingBox.width() * 0.5f);
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
        setY(minY + boundingBox.height() * 0.5f);

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
        setY(maxY - boundingBox.height() * 0.5f);
    }

    public void onPassLeft() {
        switch (mode) {
            case WRAP -> setX(maxX + boundingBox.width() * 0.5f);
            case DELETE -> parent.destroy();
        }
    }

    public void onPassRight() {
        switch (mode) {
            case WRAP -> setX(minX - boundingBox.width() * 0.5f);
            case DELETE -> parent.destroy();
        }
    }

    public void onPassTop() {
        switch (mode) {
            case WRAP -> setY(maxY + boundingBox.height() * 0.5f);
            case DELETE -> parent.destroy();
        }
    }

    public void onPassBottom() {
        switch (mode) {
            case WRAP -> setY(minY - boundingBox.height() * 0.5f);
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
