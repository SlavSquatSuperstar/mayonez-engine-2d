package mayonez.scripts;

import mayonez.math.FloatMath;
import mayonez.math.Vec2;
import mayonez.Logger;
import mayonez.Script;
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
    private float nBounce = 0f;

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
        Vec2 sceneHalfSize = getScene().getSize().mul(0.5f); // fetch scene bounds
        if (minPos == null) minPos = sceneHalfSize.mul(-1);
        if (maxPos == null) maxPos = sceneHalfSize;

        objectCollider = getCollider();
        if (objectCollider == null) {
            Logger.warn("%s needs a collider to function!", this);
            mode = null;
            setEnabled(false); // disable script if no collider
        }

        rb = objectCollider.getRigidbody();
        if (rb == null) {
            if (mode == Mode.BOUNCE) {
                Logger.warn("%s needs a rigidbody to bounce!", this);
                mode = Mode.STOP; // need rb to bounce
            }
        } else {
            nBounce = -rb.getMaterial().getBounce();
        }
    }

    @Override
    public void update(float dt) {
        objectBounds = objectCollider.getMinBounds();
        Vec2 boxMin = objectBounds.min();
        Vec2 boxMax = objectBounds.max();

        // Edge Checking for x
        if (!FloatMath.inRange(boxMin.x, minPos.x, maxPos.x - objectBounds.width)) {
            // Detect if colliding with edge
            if (boxMin.x < minPos.x) onCrossBounds(Direction.LEFT);
            else if (boxMax.x > maxPos.x) onCrossBounds(Direction.RIGHT);

            // Detect if moved completely past edge
            if (boxMax.x < minPos.x) onExitBounds(Direction.LEFT);
            else if (boxMin.x > maxPos.x) onExitBounds(Direction.RIGHT);
        }

        // Edge Checking for y
        if (!FloatMath.inRange(boxMin.y, minPos.y, maxPos.y - objectBounds.height)) {
            if (boxMin.y < minPos.y) onCrossBounds(Direction.TOP);
            else if (boxMax.y > maxPos.y) onCrossBounds(Direction.BOTTOM);

            if (boxMax.y < minPos.y) onExitBounds(Direction.TOP);
            else if (boxMin.y > maxPos.y) onExitBounds(Direction.BOTTOM);
        }

//        if (gameObject.name.equals("Camera")) {
//            System.out.println(transform.position);
//        }
    }

    // Collision Event Methods

    /**
     * A function called when the object has reached or crossed its bounds but not exited completely. Can be overridden
     * to provide custom behavior.
     *
     * @param direction which edge the object has crossed
     */
    protected void onCrossBounds(Direction direction) {
        switch (direction) {
            case LEFT -> {
                switch (mode) {
                    case STOP -> { // Align with edge of screen
                        setX(minPos.x + objectBounds.width * 0.5f);
                        if (rb != null) rb.getVelocity().x = 0;
                    }
                    case BOUNCE -> rb.getVelocity().x *= nBounce;
                }
            }
            case RIGHT -> {
                switch (mode) {
                    case STOP -> {
                        setX(maxPos.x - objectBounds.width * 0.5f);
                        if (rb != null) rb.getVelocity().x = 0;
                    }
                    case BOUNCE -> rb.getVelocity().x *= nBounce;
                }
            }
            case TOP -> {
                switch (mode) {
                    case STOP -> {
                        setY(minPos.y + objectBounds.height * 0.5f);
                        if (rb != null) rb.getVelocity().y = 0;
                    }
                    case BOUNCE -> rb.getVelocity().y *= nBounce;
                }
            }
            case BOTTOM -> {
                switch (mode) {
                    case STOP -> {
                        setY(maxPos.y - objectBounds.height * 0.5f);
                        if (rb != null) rb.getVelocity().y = 0;
                    }
                    case BOUNCE -> rb.getVelocity().y *= nBounce;
                }
            }
        }
    }

    /**
     * A function called when the object has moved outside its bounds and exited completely. Can be overridden
     * to provide custom behavior.
     *
     * @param direction which edge the object has crossed
     */
    protected void onExitBounds(Direction direction) {
        switch (direction) {
            case LEFT -> {
                switch (mode) {
                    case WRAP -> setX(maxPos.x + objectBounds.width * 0.5f);
                    case DELETE -> gameObject.setDestroyed();
                }
            }
            case RIGHT -> {
                switch (mode) {
                    case WRAP -> setX(minPos.x - objectBounds.width * 0.5f);
                    case DELETE -> gameObject.setDestroyed();
                }
            }
            case TOP -> {
                switch (mode) {
                    case WRAP -> setY(maxPos.y + objectBounds.height * 0.5f);
                    case DELETE -> gameObject.setDestroyed();
                }
            }
            case BOTTOM -> {
                switch (mode) {
                    case WRAP -> setY(minPos.y - objectBounds.height * 0.5f);
                    case DELETE -> gameObject.setDestroyed();
                }
            }
        }
    }

    private void setX(float x) {
        transform.position.x = x;
    }

    private void setY(float y) {
        transform.position.y = y;
    }

    // Enums

    /**
     * How to treat an object once it has exited its bounds.
     */
    // TODO move to top level?
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
        DELETE
    }

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
