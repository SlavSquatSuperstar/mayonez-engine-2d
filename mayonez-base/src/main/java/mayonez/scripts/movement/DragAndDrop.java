package mayonez.scripts.movement;

import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.scripts.input.*;

/**
 * Allows objects to be moved by clicking on them and dragging around the mouse.
 *
 * @author SlavSquatSuperstar
 */
public class DragAndDrop extends MouseInputScript {

    private static DragAndDrop activeInstance = null; // Make sure we only move one object at a time

    private final String button;
    protected Rigidbody rb; // Reference to object rigidbody
    private Vec2 lastMouse;

    public DragAndDrop(String button) {
        this.button = button;
    }

    @Override
    public void start() {
        super.start();
        lastMouse = new Vec2();
        rb = getRigidbody();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        lastMouse = getMousePos().add(getMouseDisp());
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
//            transform.setPosition(getMousePos());
            var move = getMousePos().sub(lastMouse).add(getMouseDisp());
//            var move = getMouseDisp();
            transform.move(move);
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
