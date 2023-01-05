package mayonez.scripts;

import mayonez.Logger;
import mayonez.Script;
import mayonez.math.Range;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.Collider;
import mayonez.physics.shapes.BoundingBox;

/**
 * Dictates what happens when an object reaches the edge of the scene.
 *
 * @author SlavSquatSuperstar
 */
public class KeepInScene extends Script {

    private Vec2 minPos, maxPos;
    private Mode mode;
    private Collider objectCollider = null;
    private BoundingBox objectBounds;
    private Rigidbody rb = null;
    private float bounce = 0f;

    /**
     * Create a new KeepInScene script and use the scene bounds.
     *
     * @param mode what to do when reaching boundaries
     */
    public KeepInScene(Mode mode) { // Use scene bounds
        this(null, null, mode);
    }

    /**
     * Create a new KeepInScene script and define an object's bounds.
     *
     * @param minPos the minimum coordinates the object's collider should reach
     * @param maxPos the maximum coordinates the object's collider should reach
     * @param mode   what to do when reaching boundaries
     */
    public KeepInScene(Vec2 minPos, Vec2 maxPos, Mode mode) {
        this.minPos = minPos;
        this.maxPos = maxPos;
        this.mode = mode;
    }

    @Override
    public void start() {
        Vec2 sceneHalfSize = getScene().getSize(); // fetch scene bounds
        if (minPos == null) minPos = sceneHalfSize.mul(-0.5f);
        if (maxPos == null) maxPos = sceneHalfSize.mul(0.5f);

        objectCollider = getCollider();
        if (objectCollider == null) {
            Logger.warn("%s needs a collider to function!", this);
            mode = null;
            setEnabled(false); // disable script if no collider
        }

        rb = getRigidbody();
        if (rb == null) {
            if (mode == Mode.BOUNCE) {
                Logger.warn("%s needs a rigidbody to bounce!", this);
                mode = Mode.STOP; // need rb to bounce
            }
        } else {
            bounce = -rb.getMaterial().getBounce();
        }
    }

    @Override
    public void update(float dt) {
        objectBounds = objectCollider.getMinBounds();
        Vec2 boxMin = objectBounds.min();
        Vec2 boxMax = objectBounds.max();

        // Edge Checking for x
        Range sceneBoundsX = new Range(minPos.x, maxPos.x);
        if (!sceneBoundsX.contains(objectBounds.getXRange())) {
            // Skip if too wide for scene
            if (objectBounds.getXRange().difference() <= sceneBoundsX.difference()) {
                // Detect if colliding with edge
                if (boxMin.x < minPos.x) onCrossBounds(Direction.LEFT);
                else if (boxMax.x > maxPos.x) onCrossBounds(Direction.RIGHT);

                // Detect if moved completely past edge
                if (boxMax.x < minPos.x) onExitBounds(Direction.LEFT);
                else if (boxMin.x > maxPos.x) onExitBounds(Direction.RIGHT);
            }
        }

        // Edge Checking for y
        Range sceneBoundsY = new Range(minPos.y, maxPos.y);
        if (!sceneBoundsY.contains(objectBounds.getYRange())) {
            if (objectBounds.getYRange().difference() <= sceneBoundsY.difference()) {
                if (boxMin.y < minPos.y) onCrossBounds(Direction.TOP);
                else if (boxMax.y > maxPos.y) onCrossBounds(Direction.BOTTOM);

                if (boxMax.y < minPos.y) onExitBounds(Direction.TOP);
                else if (boxMin.y > maxPos.y) onExitBounds(Direction.BOTTOM);
            }
        }
    }

    /**
     * Check and object's edges against the scene bounds.
     *
     * @param sceneBds  the area the object is allowed to go
     * @param objectBds the object bounding box
     * @param minDir    the minimum (lesser) direction
     * @param maxDir    the maximum (greater) direction
     */
    private void checkEdges(Range sceneBds, Range objectBds, Direction minDir, Direction maxDir) {
        if (sceneBds.contains(objectBds)) return; // Skip if still in scene
        if (objectBds.difference() > sceneBds.difference()) return;  // Skip if too wide for scene

        // Detect if colliding with edge
        if (objectBds.min < sceneBds.min) onCrossBounds(minDir);
        else if (objectBds.max > sceneBds.max) onCrossBounds(maxDir);

        // Detect if moved completely past edge
        if (objectBds.min < sceneBds.min) onExitBounds(minDir);
        else if (objectBds.max > sceneBds.max) onExitBounds(maxDir);
    }

    // Collision Event Methods

    /**
     * A function called when the object has reached or crossed its bounds but not exited completely.
     * Can be overridden to provide custom behavior.
     *
     * @param direction which edge the object has crossed
     */
    protected void onCrossBounds(Direction direction) {
        switch (mode) {
            case STOP -> {
                alignInside(direction);
                stop(direction);
            }
            case BOUNCE -> {
                alignInside(direction);
                bounce(direction);
            }
        }
    }

    /**
     * A function called when the object has moved outside its bounds and exited completely.
     * Can be overridden to provide custom behavior.
     *
     * @param direction which edge the object has crossed
     */
    protected void onExitBounds(Direction direction) {
        switch (mode) {
            case WRAP -> alignOutside(direction);
            case DESTROY -> gameObject.setDestroyed();
        }
    }

    // Position/Velocity Setters

    private void alignInside(Direction dir) { // Align object to inside of bounds
        switch (dir) {
            case LEFT -> setX(minPos.x + objectBounds.width * 0.5f);
            case RIGHT -> setX(maxPos.x - objectBounds.width * 0.5f);
            case TOP -> setY(minPos.y + objectBounds.height * 0.5f);
            case BOTTOM -> setY(maxPos.y - objectBounds.height * 0.5f);
        }
    }

    private void alignOutside(Direction dir) { // Align object to outside of bounds
        switch (dir) {
            case LEFT -> setX(maxPos.x + objectBounds.width * 0.5f);
            case RIGHT -> setX(minPos.x - objectBounds.width * 0.5f);
            case TOP -> setY(maxPos.y + objectBounds.height * 0.5f);
            case BOTTOM -> setY(minPos.y - objectBounds.height * 0.5f);
        }
    }

    private void setX(float x) {
        transform.getPosition().x = x;
    }

    private void setY(float y) {
        transform.getPosition().y = y;
    }

    private void stop(Direction dir) {
        if (rb == null) return;
        switch (dir) {
            case LEFT, RIGHT -> rb.getVelocity().x = 0f;
            case TOP, BOTTOM -> rb.getVelocity().y = 0f;
        }
    }

    private void bounce(Direction dir) {
        if (rb == null) return;
        switch (dir) {
            case LEFT, RIGHT -> rb.getVelocity().x *= bounce;
            case TOP, BOTTOM -> rb.getVelocity().y *= bounce;
        }
    }

    // Enums

    /**
     * How to treat an object once it has exited its bounds.
     */
    public enum Mode {
        /**
         * Clamp an object's position to the scene when touching an edge and set speed to 0 if a rigidbody is present.
         */
        STOP,
        /**
         * Make an object bounce back when touching an edge. Requires a rigidbody and depends on the physics material.
         */
        BOUNCE,
        /**
         * Move an object to the opposite side of the scene when passing an edge.
         */
        WRAP,
        /**
         * Remove an object from the scene when passing an edge.
         */
        DESTROY
    }

    /**
     * In what direction an object has gone out of bounds.
     */
    protected enum Direction {
        /**
         * The left edge of the scene.
         */
        LEFT,
        /**
         * The right edge of the scene.
         */
        RIGHT,
        /**
         * The top edge of the scene.
         */
        TOP,
        /**
         * The bottom edge of the scene.
         */
        BOTTOM
    }

}
