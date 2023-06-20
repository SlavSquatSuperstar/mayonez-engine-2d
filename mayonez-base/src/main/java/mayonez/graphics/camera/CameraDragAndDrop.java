package mayonez.graphics.camera;

import mayonez.input.*;
import mayonez.math.*;
import mayonez.scripts.movement.*;

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
        if (Input.buttonPressed(getButton())) {
            // Reset camera position by double-clicking
            if (Input.getMouseClicks() > 1) {
                transform.setPosition(new Vec2(0, 0));
            }
        }
    }

    @Override
    public void onMouseHeld() {
        if (Input.buttonDown(getButton())) {
            transform.move(getMouseDisp().mul(-1f)); // drags slowly on on AWT
        }
    }

    @Override
    public void onMouseUp() {
    }
}
