package mayonez.graphics.camera;

import mayonez.graphics.*;
import mayonez.math.*;
import org.joml.*;

/**
 * A scene camera for the AWT engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
final class JCamera extends Camera {

    JCamera(Vec2 screenSize, float sceneScale) {
        super(screenSize, sceneScale);
    }

    // Camera Methods

    @Override
    public Vec2 getScreenOffset() {
        // view center - zoom offset
        // (position * scene_scale) - (0.5 * screen_size / zoom)
        return getViewCenter().sub(screenSize.mul(0.5f / getZoom()));
    }

    @Override
    public Matrix4f getViewMatrix() {
        return new Matrix4f(); // unused
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return new Matrix4f(); // unused
    }

    @Override
    public Vec2 toWorld(Vec2 screen) {
        var flippedPos = new Vec2(screen.x, screenSize.y - screen.y); // Mirror y
        var screenPos = flippedPos.add(getScreenOffset());
        return screenPos.div(sceneScale);
    }

}
