package slavsquatsuperstar.mayonez.graphics;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;

/**
 * A scene camera for the Java engine.
 *
 * @author SlavSquatSuperstar
 */
// TODO elastic/smooth movement
// TODO allow drag, but snap back to subject
@UsesEngine(EngineType.AWT)
public final class JCamera extends Camera {

    public JCamera(Vec2 screenSize, float sceneScale) {
        super(screenSize, sceneScale);
    }

    @Override
    public void update(float dt) {
        if (getSubject() != null) transform.position.set(getSubject().transform.position);
    }

}
