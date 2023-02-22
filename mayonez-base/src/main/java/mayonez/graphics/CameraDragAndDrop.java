package mayonez.graphics;

import mayonez.input.MouseInput;
import mayonez.math.Vec2;
import mayonez.scripts.movement.DragAndDrop;

/**
 * Allows the scene camera to be moved by dragging the mouse and
 * its position to be reset by double-clicking.
 *
 * @author SlavSquatSuperstar
 */
class CameraDragAndDrop extends DragAndDrop {

    public CameraDragAndDrop(String button) {
        super(button);
    }

    @Override
    public void onMouseDown() {
        if (MouseInput.buttonPressed(getButton())) {
            // Reset camera position by double-clicking
            if (MouseInput.getClicks() == 2) {
                transform.setPosition(new Vec2(0, 0));
            }
        }
    }

    @Override
    public void onMouseHeld() {
        if (MouseInput.buttonDown(getButton())) {
            transform.move(getMouseDisp().mul(-1f)); // drags slowly on on AWT
        }
    }

    @Override
    public void onMouseUp() {
    }
}
