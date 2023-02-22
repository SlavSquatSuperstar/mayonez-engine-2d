package mayonez.scripts.movement;

import mayonez.input.MouseInput;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.scripts.input.MouseInputScript;

/**
 * Allows objects to be moved by clicking on them and dragging around the mouse.
 *
 * @author SlavSquatSuperstar
 */
public class DragAndDrop extends MouseInputScript {

    private static DragAndDrop activeInstance = null; // Make sure we only move one object at a time

    private final String button;
    protected Rigidbody rb; // Reference to object rigidbody

    public DragAndDrop(String button) {
        this.button = button;
    }

    @Override
    public void start() {
        super.start();
        rb = getRigidbody();
    }

    // Callback Methods

    @Override
    public void onMouseDown() {
        if (activeInstance == null && MouseInput.buttonPressed(button)) {
            activeInstance = this;
        }
    }

    @Override
    public void onMouseHeld() {
        if (activeInstance == this) {
            // Stop the object's motion
            if (rb != null) {
                rb.setVelocity(new Vec2());
                rb.setAngVelocity(0);
            }
            transform.move(getMouseDisp());
        }
    }

    @Override
    public void onMouseUp() {
        if (activeInstance == this) {
            activeInstance = null;
        }
    }

    @Override
    public void onDestroy() {
        onMouseUp(); // If object destroyed while this instance is active, free up active instance
    }

    // Getters

    protected String getButton() {
        return button;
    }
}
