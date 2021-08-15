package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.mayonez.MouseInput;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;

// TODO use mouse events and save states (held, released)

/**
 * Provides utility methods for scripts using mouse.
 *
 * @author SlavSquatSuperstar
 */
public abstract class MouseScript extends MovementScript {

    protected Collider2D collider; // Reference to object collider

    // Script Info
    protected boolean inverted;
    protected String button = "left mouse";

    // Internal Script
    protected float lastMx, lastMy;
    private boolean mouseHeld;

    public MouseScript() {
        mode = MoveMode.FOLLOW_MOUSE;
    }

    public MouseScript(MoveMode mode, float speed) {
        super(mode, speed);
    }

    @Override
    public void start() {
        super.start();
        collider = getCollider();
    }

    @Override
    public void update(float dt) {
        if (!mouseHeld) {
            // If the mouse is up and then pressed on this object
            if (isMouseOnObject() && MouseInput.buttonDown(button)) {
                mouseHeld = true;
                onMouseDown();
            }
        } else {
            // If the mouse is pressed and then released
            if (!MouseInput.buttonDown(button)) {
                mouseHeld = false;
                onMouseUp();
            }
        }

        onMouseMove();
        lastMx = MouseInput.getX() + MouseInput.getDx();
        lastMy = MouseInput.getY() + MouseInput.getDy();
    }

    // Input Helper Methods

    // TODO Use MouseInput collision detection instead
    protected boolean isMouseOnObject() {
        // Using last mouse position is more reliable when mouse is moving fast
        if (collider != null)
            return collider.contains(new Vec2(getScene().camera().getOffset().x + lastMx, getScene().camera().getOffset().y + lastMy));
        return true;
    }

    // Mouse Event Methods

    /**
     * What to do when mouse pointer changes location.
     */
    public void onMouseMove() {}

    /**
     * What to do when the specified button is pressed.
     */
    public void onMouseDown() {}

    /**
     * What to do when the specified button is released.
     */
    public void onMouseUp() {}

    public boolean isMouseHeld() {
        return mouseHeld;
    }
}
