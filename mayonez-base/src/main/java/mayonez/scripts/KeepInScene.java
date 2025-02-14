package mayonez.scripts;

import mayonez.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;

import java.util.*;

/**
 * Detects when an object reaches the edge of or exits a given boundary
 * and sets custom behavior.
 *
 * @author SlavSquatSuperstar
 */
public class KeepInScene extends Script {

    private final Vec2 minPos, maxPos;
    private Mode mode;
    private Collider objectCollider = null;
    private BoundingBox objectBounds;
    private Rigidbody rb = null;

    /**
     * Create a new KeepInScene script with the given bounds
     *
     * @param minPos the minimum coordinates the object's collider should reach
     * @param maxPos the maximum coordinates the object's collider should reach
     * @param mode   what to do when reaching boundaries
     */
    public KeepInScene(Vec2 minPos, Vec2 maxPos, Mode mode) {
        super(UpdateOrder.SCRIPT);
        this.minPos = minPos;
        this.maxPos = maxPos;
        this.mode = mode;
    }

    @Override
    protected void start() {
        objectCollider = getCollider();
        if (objectCollider == null) {
            Logger.warn("%s needs a collider to function!", this);
            mode = null;
            setEnabled(false);
        }
        rb = getRigidbody();
    }

    @Override
    protected void update(float dt) {
        objectBounds = getObjectBounds();

        // Edge Checking for x
        var sceneBoundsX = new Interval(minPos.x, maxPos.x);
        checkEdges(sceneBoundsX, objectBounds.getXInterval(), Direction.LEFT, Direction.RIGHT);

        // Edge Checking for y
        var sceneBoundsY = new Interval(minPos.y, maxPos.y);
        checkEdges(sceneBoundsY, objectBounds.getYInterval(), Direction.TOP, Direction.BOTTOM);
    }

    // Bounds Helper Methods

    protected BoundingBox getObjectBounds() {
        return objectCollider.getMinBounds();
    }

    /**
     * Check and object's edges against the scene bounds.
     *
     * @param sceneBds  the area the object is allowed to go
     * @param objectBds the object bounding box
     * @param minDir    the minimum (lesser) direction
     * @param maxDir    the maximum (greater) direction
     */
    private void checkEdges(Interval sceneBds, Interval objectBds, Direction minDir, Direction maxDir) {
        if (sceneBds.contains(objectBds)) return; // Skip if still in scene
        if (objectBds.difference() > sceneBds.difference()) return;  // Skip if too wide for scene

        // Detect if colliding with edge
        if (objectBds.min < sceneBds.min) onCrossBounds(minDir);
        else if (objectBds.max > sceneBds.max) onCrossBounds(maxDir);

        // Detect if moved completely past edge
        if (objectBds.max < sceneBds.min) onExitBounds(minDir);
        else if (objectBds.min > sceneBds.max) onExitBounds(maxDir);
    }

    // Collision Event Methods

    /**
     * A function called when the object has reached or crossed its bounds but not exited completely.
     * Can be overridden to provide custom behavior.
     *
     * @param direction which edge the object has crossed
     */
    protected void onCrossBounds(Direction direction) {
        if (Objects.requireNonNull(mode) == Mode.STOP) {
            alignInside(direction);
            stop(direction);
        }
    }

    /**
     * A function called when the object has moved outside its bounds and exited completely.
     * Can be overridden to provide custom behavior.
     *
     * @param direction which edge the object has crossed
     */
    protected void onExitBounds(Direction direction) {
        if (Objects.requireNonNull(mode) == Mode.WRAP) {
            alignOutside(direction);
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

    protected void setX(float x) {
        transform.getPosition().x = x;
    }

    protected void setY(float y) {
        transform.getPosition().y = y;
    }

    private void stop(Direction dir) {
        if (rb == null) return;
        switch (dir) {
            case LEFT, RIGHT -> rb.getVelocity().x = 0f;
            case TOP, BOTTOM -> rb.getVelocity().y = 0f;
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
         * Move an object to the opposite side of the scene when passing an edge.
         */
        WRAP
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
