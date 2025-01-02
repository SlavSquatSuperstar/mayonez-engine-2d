package mayonez.scripts.camera;

import mayonez.graphics.camera.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.scripts.*;

/**
 * Prevents the scene camera from scrolling past a certain limit.
 *
 * @author SlavSquatSuperstar
 */
public class CameraKeepInScene extends KeepInScene {

    private Camera camera;

    public CameraKeepInScene(Vec2 minPos, Vec2 maxPos) {
        super(minPos, maxPos, Mode.STOP);
    }

    @Override
    protected void start() {
        camera = getScene().getCamera();
        if (camera == null) setEnabled(false);
    }

    @Override
    protected BoundingBox getObjectBounds() {
        return new BoundingBox(camera.getPosition(), camera.getSize());
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
