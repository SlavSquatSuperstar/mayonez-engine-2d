package mayonez.scripts.camera;

import mayonez.graphics.camera.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.scripts.mouse.*;

/**
 * Allows the scene camera to be moved by dragging the mouse and
 * its position to be reset by double-clicking.
 *
 * @author SlavSquatSuperstar
 */
// TODO snap back to subject
public class CameraDragAndDrop extends DragAndDrop {

    private Camera camera;

    public CameraDragAndDrop(String button) {
        super(button);
    }

    @Override
    protected void start() {
        super.start();
        camera = getScene().getCamera();
        if (camera == null) setEnabled(false);
        else camera.setSubject(null);
    }

    // Mouse Input Callbacks

    @Override
    public void onMouseDown() {
        if (MouseInput.buttonPressed(getButton())) {
            // Reset camera position by double-clicking
            if (MouseInput.isDoubleClick()) {
                camera.getTransform().setPosition(new Vec2(0, 0));
            }
        }
    }

    @Override
    public void onMouseHeld() {
        if (MouseInput.buttonDown(getButton())) {
            System.out.println("button held");
            camera.getTransform().move(getMouseDisp().mul(-1f)); // drags slowly on AWT
        }
    }

    @Override
    public void onMouseUp() {
        // Don't do anything
    }

    protected boolean isMouseOnObject() {
        return true;
    }

}
