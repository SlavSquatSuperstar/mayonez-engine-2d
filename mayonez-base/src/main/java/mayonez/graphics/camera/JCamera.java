package mayonez.graphics.camera;

import mayonez.annotations.*;
import mayonez.math.*;

/**
 * A scene camera for the AWT engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public final class JCamera extends Camera {

    public JCamera(Vec2 screenSize, float sceneScale) {
        super(screenSize, sceneScale);
    }

    @Override
    public Vec2 toWorld(Vec2 screen) {
        var flippedPos = new Vec2(screen.x, screenSize.y - screen.y); // Mirror y
        var screenPos = flippedPos.add(getScreenOffset());
        return screenPos.div(sceneScale);
    }

}
