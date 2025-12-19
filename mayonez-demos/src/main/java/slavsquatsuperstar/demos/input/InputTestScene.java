package slavsquatsuperstar.demos.input;

import mayonez.*;
import mayonez.graphics.font.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.DemoScene;

/**
 * A scene for testing all keyboard and mouse input work correctly.
 *
 * @author SlavSquatSuperstar
 */
public class InputTestScene extends DemoScene {

    public InputTestScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        getCamera().setCameraScale(10);

        addObject(new GameObject("Scene Title Text") {
            @Override
            protected void init() {
                addComponent(new TextLabel(getScene().getName(),
                        new Vec2(Mayonez.getScreenWidth() * 0.5f,
                                Mayonez.getScreenHeight() - 50))
                        .setFontSize(40));
            }
        });

        addObject(new GameObject("Input Detector") {
            @Override
            protected void init() {
                addComponent(new KeyInputTester());
                addComponent(new MouseInputTester());
            }
        });
    }

}
