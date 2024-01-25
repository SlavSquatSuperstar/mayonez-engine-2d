package mayonez.scripts.mouse;

import mayonez.math.*;
import mayonez.physics.dynamics.*;

/**
 * Allows objects to be moved by clicking on them and dragging around the mouse.
 *
 * @author SlavSquatSuperstar
 */
public class DragAndDrop extends MouseInputScript {

    private static DragAndDrop activeInstance = null; // Make sure we only move one object at a time

    protected Rigidbody rb; // Reference to object rigidbody
    private Vec2 lastMouse;

    public DragAndDrop(String button) {
        super(button);
    }

    @Override
    protected void start() {
        super.start();
        lastMouse = new Vec2();
        rb = getRigidbody();
    }

    @Override
    protected void update(float dt) {
        super.update(dt);
        lastMouse = getMousePos().add(getMouseDisp());
    }

    // Callback Methods

    @Override
    public void onMouseDown() {
        if (activeInstance == null) {
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
    protected void onDestroy() {
        onMouseUp(); // If object destroyed while this instance is active, free up active instance
    }

}
