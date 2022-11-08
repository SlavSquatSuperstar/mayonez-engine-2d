package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.mayonez.math.Vec2;
import slavsquatsuperstar.mayonez.input.MouseInput;

/**
 * Allows objects to be picked up and moved using the mouse.
 *
 * @author SlavSquatSuperstar
 */
public class DragAndDrop extends MouseScript {

    private static DragAndDrop activeInstance = null; // Make sure we only move one object at a time
    private final boolean alwaysAllow; // Always enable drag and drop.

    public DragAndDrop(String button) {
        this(button, false, false);
    }

    public DragAndDrop(String button, boolean inverted, boolean alwaysAllow) {
        super(MoveMode.POSITION, 0);
        this.inverted = inverted;
        this.button = button;
        this.alwaysAllow = alwaysAllow;
    }

    @Override
    public void onMouseDown() {
        if (activeInstance == null) activeInstance = this;
    }

    @Override
    public void onMouseMove() {
        if ((activeInstance == this || alwaysAllow) && isMouseHeld()) {
            if (rb != null) {
                rb.setVelocity(new Vec2()); // Stop the object
                rb.setAngVelocity(0);
            }
            transform.move(getRawInput());
        }
    }

    @Override
    public void onMouseUp() {
        if (activeInstance == this) activeInstance = null;
    }

    @Override
    protected Vec2 getRawInput() {
        return MouseInput.getPosition().sub(lastMouse).add(MouseInput.getDisplacement()).mul(inverted ? -1 : 1);
    }

    @Override
    public void onDestroy() {
        onMouseUp(); // If parent destroyed while this instance is active, free up active instance
    }
}
