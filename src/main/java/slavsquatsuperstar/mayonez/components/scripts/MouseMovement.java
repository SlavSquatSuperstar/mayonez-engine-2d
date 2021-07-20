package slavsquatsuperstar.mayonez.components.scripts;

import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.MouseInput;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.primitives.Collider2D;

// TODO use mouse events and save states (held, released)

/**
 * A base class providing utility methods for scripts using mouse.
 */
public abstract class MouseMovement extends MovementScript {

    protected Collider2D collider; // Reference to object collider

    // Script Info
    protected boolean inverted;
    protected String button = "left mouse";

    // Internal Script
    protected float lastMx, lastMy;
    private boolean mouseHeld;

    public MouseMovement() {
        mode = MoveMode.FOLLOW_MOUSE;
    }

    public MouseMovement(MoveMode mode, float speed) {
        super(mode, speed);
    }

    @Override
    public void start() {
        super.start();
        collider = parent.getComponent(Collider2D.class);
    }

    @Override
    public void update(float dt) {
        MouseInput mouse = Game.mouse();
        if (!mouseHeld) {
            // If the mouse is up and then pressed on this object
            if (isMouseOnObject() && mouse.buttonDown(button)) {
                mouseHeld = true;
                onMouseDown();
            }
        } else {
            // If the mouse is pressed and then released
            if (!mouse.buttonDown(button)) {
                mouseHeld = false;
                onMouseUp();
            }
        }
        onMouseMove();
        lastMx = mouse.getX() + mouse.getDx();
        lastMy = mouse.getY() + mouse.getDy();
    }

    // Input Helper Methods

    // TODO Use MouseInput collision detection instead
    protected boolean isMouseOnObject() {
        // Using last mouse position is more reliable when mouse is moving fast
        if (collider != null)
               return collider.contains(new Vector2(scene().camera().getOffsetX() + lastMx, scene().camera().getOffsetY() + lastMy));
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
