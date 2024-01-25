package mayonez.scripts.mouse;

import mayonez.*;
import mayonez.input.Button;
import mayonez.input.MouseInput;
import mayonez.math.Vec2;
import mayonez.physics.colliders.Collider;

/**
 * Provides custom behaviors for when the mouse is clicked or dragged on an object.
 *
 * @author SlavSquatSuperstar
 */
public abstract class MouseInputScript extends Script {

    // Mouse State
    protected Vec2 lastMouse;
    private boolean mouseDown;
    private final String button;

    protected Collider collider; // Reference to object collider

    /**
     * Create a MouseInputScript, setting the button this script should check.
     *
     * @param button the button name, or null for any
     */
    public MouseInputScript(String button) {
        super(UpdateOrder.INPUT);
        this.button = button;
        lastMouse = new Vec2();
        mouseDown = false;
    }

    /**
     * Create a MouseInputScript, with the button set to Left Mouse (Mouse 1).
     */
    public MouseInputScript() {
        this(Button.LEFT_MOUSE.toString());
    }

    @Override
    protected void start() {
        collider = getCollider();
    }

    @Override
    protected void update(float dt) {
        if (!mouseDown) {
            checkMouseDown();
        } else {
            checkMouseUp();
            if (mouseDown) checkMouseHeld();
        }
    }

    // Mouse Input Methods

    /**
     * Check if the mouse is pressed on this object this frame.
     */
    private void checkMouseDown() {
        if (isMouseButtonDown() && isMouseOnObject()) {
            mouseDown = true;
            onMouseDown();
        }
    }

    /**
     * Check if the mouse is released after being pressed.
     */
    private void checkMouseUp() {
        if (!isMouseButtonDown()) {
            mouseDown = false;
            onMouseUp();
        }
    }

    /**
     * Check if the mouse is continuously held while after pressing on this object.
     */
    // TODO this could be useful elsewhere
    private void checkMouseHeld() {
        onMouseHeld();
        lastMouse = getMousePos().add(getMouseDisp());
    }

    protected boolean isMouseButtonDown() {
        if (button == null) {
            return MouseInput.isPressed();
        } else {
            // TODO down vs pressed
            return MouseInput.buttonDown(button);
        }
    }

    // TODO Use MouseInput collision detection instead
    protected boolean isMouseOnObject() {
        return collider != null && collider.contains(getMousePos());
    }

    // Callback Methods

    /**
     * Custom behavior for when the mouse is pressed on this object's collider.
     */
    public void onMouseDown() {
    }

    /**
     * Custom behavior for when the mouse is dragged after being pressed on this object.
     */
    public void onMouseHeld() {
    }

    /**
     * Custom behavior for when the mouse is released after being pressed.
     */
    public void onMouseUp() {
    }

    // Mouse Getters

    /**
     * Which mouse button this script should detect.
     *
     * @return the button name
     */
    protected final String getButton() {
        return button;
    }

    /**
     * Query the mouse position.
     *
     * @return the mouse position
     */
    protected final Vec2 getMousePos() {
        return MouseInput.getPosition();
    }

    /**
     * Query the mouse drag displacement.
     *
     * @return the drag displacement
     */
    protected final Vec2 getMouseDisp() {
        return MouseInput.getDisplacement();
    }

}
