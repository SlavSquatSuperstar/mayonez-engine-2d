package mayonez.scripts.input;

import mayonez.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;

/**
 * Provides custom behaviors for when the mouse is clicked or dragged on an object.
 *
 * @author SlavSquatSuperstar
 */
public abstract class MouseInputScript extends Script {

    // Mouse State
    protected Vec2 lastMouse;
    private boolean mouseDown;

    protected Collider collider; // Reference to object collider

    public MouseInputScript() {
        lastMouse = new Vec2();
        mouseDown = false;
    }

    @Override
    public void start() {
        collider = getCollider();
    }

    @Override
    public void update(float dt) {
        if (!mouseDown) {
            checkMouseDown();
        } else {
            checkMouseUp();
            // mouse still down?
            if (mouseDown) checkMouseHeld();
        }
    }

    // Mouse Input Methods

    protected boolean isMouseOnObject() {
        return collider != null && collider.contains(getMousePos());
//        return collider != null && collider.contains(lastMouse);
    }

    /**
     * Check if the mouse gets pressed on this object.
     */
    private void checkMouseDown() {
        if (MouseInput.isPressed() && isMouseOnObject()) {
            mouseDown = true;
            onMouseDown();
        }
    }

    /**
     * Check if the mouse gets released after being pressed.
     */
    private void checkMouseUp() {
        if (!MouseInput.isPressed()) {
            mouseDown = false;
            onMouseUp();
        }
    }

    /**
     * Check if the mouse gets held while pressing this object.
     */
    private void checkMouseHeld() {
        onMouseHeld();
        lastMouse = getMousePos().add(getMouseDisp());
    }

    // Callback Methods

    /**
     * Custom behavior for when the mouse is pressed on this object's collider.
     */
    public void onMouseDown() {
    }

    /**
     * Custom behavior for when the mouse is dragged on held after being pressed.
     */
    public void onMouseHeld() {
    }

    /**
     * Custom behavior for when the mouse is released after being pressed.
     */
    public void onMouseUp() {
    }

    // Getter Methods

    /**
     * Query the mouse position.
     *
     * @return the mouse position
     */
    protected final Vec2 getMousePos() {
        return MouseInput.getPosition();
    }

    /**
     * Query the mouse displacement.
     *
     * @return the mouse displacement
     */
    protected final Vec2 getMouseDisp() {
        return MouseInput.getDisplacement();
    }

}
