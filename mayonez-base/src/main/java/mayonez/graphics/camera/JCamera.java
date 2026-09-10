package mayonez.graphics.camera;

import mayonez.application.*;
import mayonez.math.*;

/**
 * A scene camera for the AWT engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesBackend(Backend.AWT)
final class JCamera extends Camera {

    JCamera(Vec2 screenSize) {
        super(screenSize);
    }

    // Camera Methods

    @Override
    public Vec2 getScreenOffset() {
        // view center - zoom offset
        // (position * scene_scale) - (0.5 * screen_size / zoom)
        return getScreenCenter().sub(screenSize.mul(0.5f / getZoom()));
    }

    @Override
    public Vec2 toWorldPosition(Vec2 screenPos) {
        var flippedPos = new Vec2(screenPos.x, screenSize.y - screenPos.y); // Mirror y
        var offsetPos = flippedPos.add(getScreenOffset());
        return offsetPos.mul(getInvCameraScale());
    }

    @Override
    public Vec2 toWorldDisplacement(Vec2 screenDisp) {
        var flippedDisp = new Vec2(screenDisp.x, -screenDisp.y);
        return flippedDisp.mul(getInvCameraScale());
    }

}
