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

        addNode(new TextLabel(getName(),
                new Vec2(Mayonez.getScreenWidth() * 0.5f,
                        Mayonez.getScreenHeight() - 50))
                .setFontSize(40));

        addNode(new KeyInputTester());
        addNode(new MouseInputTester());
    }

}
