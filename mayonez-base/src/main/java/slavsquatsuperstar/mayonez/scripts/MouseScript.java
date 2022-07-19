package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics.colliders.Collider;

// TODO use mouse events and save states (held, released)

/**
 * Provides utility methods for scripts using mouse.
 *
 * @author SlavSquatSuperstar
 */
public abstract class MouseScript extends MovementScript {

    protected Collider collider; // Reference to object collider

    // Script Info
    protected boolean inverted; // Moving the mouse will move the object in the opposite way
    protected String button = "left mouse";

    // Internal Script
    protected Vec2 lastMouse = new Vec2();
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
        lastMouse = MouseInput.getWorldPos().add(MouseInput.getWorldDisp());
    }

    // Input Helper Methods

    // TODO Use MouseInput collision detection instead
    protected boolean isMouseOnObject() {
        // Using last mouse position is more reliable when mouse is moving fast
        return collider == null || collider.contains(getScene().getCamera().getOffset().add(lastMouse));
    }

    // Mouse Event Methods

    /**
     * What to do when mouse pointer changes location.
     */
    public void onMouseMove() {}

    /**
     * What to do when the specified button is pressed on this object.
     */
    public void onMouseDown() {}

    /**
     * What to do when the specified button is released.
     */
    public void onMouseUp() {}

    /**
     * If the specified mouse button is held down.
     *
     * @return if the button is down
     */
    public boolean isMouseHeld() {
        return mouseHeld;
    }
}
