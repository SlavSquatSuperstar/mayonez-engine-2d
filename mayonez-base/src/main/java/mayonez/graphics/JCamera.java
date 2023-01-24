package mayonez.graphics;

import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.math.Vec2;

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
        var worldPos = flippedPos.add(getOffset());
        return worldPos.div(sceneScale);
    }

}
