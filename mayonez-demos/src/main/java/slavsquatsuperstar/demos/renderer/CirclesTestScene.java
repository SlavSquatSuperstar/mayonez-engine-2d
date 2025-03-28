package slavsquatsuperstar.demos.renderer;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.graphics.font.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import slavsquatsuperstar.demos.DemoScene;
import slavsquatsuperstar.demos.DemosAssets;

/**
 * A scene for testing the efficiency of circle rendering.
 *
 * @author SlavSquatSuperstar
 */
public class CirclesTestScene extends DemoScene {

    private static final int SCENE_SCALE = 16;
    private static final Vec2 SCENE_HALF_SIZE
            = new Vec2(Preferences.getScreenWidth(), Preferences.getScreenHeight())
            .div(SCENE_SCALE * 2f);

    private static final int NUM_CIRCLES = 2000;
    private static final float MIN_RADIUS = 0.1f;
    private static final float MAX_RADIUS = 1.0f;

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

            addObject(new GameObject("Ball " + (i + 1), position) {
                @Override
                protected void init() {
                    var color = Colors.randomColor();
                    var fill = Random.randomBoolean();
                    var stroke = Random.randomInt(1, 3);

                    var shape = getShape(Random.randomBoolean());
                    var sprite = new ShapeSprite(shape, color, fill);
                    sprite.setStrokeSize(stroke);
                    addComponent(sprite);
                }
            });
        }

        addObject(new GameObject("FPS Counter") {
            @Override
            protected void init() {
                TextLabel fpsText;
                var font = DemosAssets.getFont();

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

    private static Shape getShape(boolean isEllipse) {
        if (isEllipse) {
            var width = Random.randomFloat(MIN_RADIUS, MAX_RADIUS) * 2f;
            var height = width * Random.randomFloat(0.75f, 1.25f);
            var size = new Vec2(width, height);
            var rotation = Random.randomAngle();
            return new Ellipse(new Vec2(0f), size, rotation);
        } else {
            var radius = Random.randomFloat(MIN_RADIUS, MAX_RADIUS);
            return new Circle(new Vec2(0f), radius);
        }
    }

}
