package slavsquatsuperstar.demos.input;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.DemosAssets;

/**
 * A scene for testing all keyboard and mouse input work correctly.
 *
 * @author SlavSquatSuperstar
 */
public class InputTestScene extends Scene {

    public InputTestScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        getCamera().setCameraScale(10);

        var font = DemosAssets.getFont();
        if (font != null) {
            addObject(new GameObject("Scene Title Text") {
                @Override
                protected void init() {
                    addComponent(new UITextLabel(getScene().getName(),
                            new Vec2(Preferences.getScreenWidth() * 0.5f,
                                    Preferences.getScreenHeight() - 50),
                            font, Colors.BLACK, 32, 1));
                }
            });
        }
    }

}
