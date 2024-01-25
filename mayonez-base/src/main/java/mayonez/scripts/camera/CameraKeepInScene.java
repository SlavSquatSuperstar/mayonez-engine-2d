package mayonez.scripts.camera;

import mayonez.graphics.camera.*;
import mayonez.math.shapes.*;
import mayonez.scripts.*;

/**
 * Prevents the scene camera from scrolling past a certain limit.
 *
 * @author SlavSquatSuperstar
 */
public class CameraKeepInScene extends KeepInScene {

    private Camera camera;

    public CameraKeepInScene() {
        super(Mode.STOP);
    }

    @Override
    protected void start() {
        setMoveBounds();
        camera = getScene().getCamera();
        if (camera == null) setEnabled(false);
    }

    @Override
    protected BoundingBox getObjectBounds() {
        return camera.getBounds();
    }

    // KeepInScene Overrides

    @Override
    protected void setX(float x) {
        camera.getTransform().getPosition().x = x;
    }

    @Override
    protected void setY(float y) {
        camera.getTransform().getPosition().y = y;
    }

}
