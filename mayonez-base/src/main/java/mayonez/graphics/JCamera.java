package mayonez.graphics;

import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.math.Vec2;

/**
 * A scene camera for the Java engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public final class JCamera extends Camera {

    public JCamera(Vec2 screenSize, float sceneScale) {
        super(screenSize, sceneScale);
    }

}
