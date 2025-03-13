package slavsquatsuperstar.demos.renderer;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.graphics.font.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import slavsquatsuperstar.demos.DemoScene;
import slavsquatsuperstar.demos.DemosAssets;

/**
 * A scene for testing the efficiency of circle rendering.
 *
 * @author SlavSquatSuperstar
 */
public class CirclesTestScene extends DemoScene {

    private static final int SCENE_SCALE = 32;
    private static final Vec2 SCENE_HALF_SIZE
            = new Vec2(Preferences.getScreenWidth(), Preferences.getScreenHeight())
            .div(SCENE_SCALE * 2f);
    private static final int NUM_CIRCLES = 1500;

    public CirclesTestScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        getCamera().setCameraScale(SCENE_SCALE);

        for (int i = 0; i < NUM_CIRCLES; i++) {
            var position = Random.randomVector(
                    SCENE_HALF_SIZE.mul(-1f), SCENE_HALF_SIZE
            );
            var scale = new Vec2(Random.randomFloat(0.1f, 1.0f));
            var transform = new Transform(position, 0, scale);

            addObject(new GameObject("Ball " + (i + 1), transform) {
                @Override
                protected void init() {
                    addComponent(new BallCollider(new Vec2(1f)));
                    var color = Colors.randomColor();
                    var fill = true;
                    addComponent(new ShapeSprite(color, fill));
                }
            });
        }

        addObject(new GameObject("FPS Counter") {
            @Override
            protected void init() {
                TextLabel fpsText;
                var font = DemosAssets.getFont();
                if (font == null) return;

                addComponent(new UISprite(
                        new Vec2(110, Preferences.getScreenHeight() - 50),
                        new Vec2(180, 50), Colors.LIGHT_GRAY
                ));

                addComponent(fpsText = new UITextLabel(
                        "FPS: _", new Vec2(100, Preferences.getScreenHeight() - 50),
                        font, Colors.BLACK, 36, 1
                ) {
                    @Override
                    protected void update(float dt) {
                        setMessage("FPS: " + Mayonez.getRenderFPS());
                    }
                });
                fpsText.setAnchor(Anchor.TOP_LEFT);
            }
        });
    }

}
