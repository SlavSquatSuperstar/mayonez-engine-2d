package slavsquatsuperstar.demos.input;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.math.shapes.*;

/**
 * Detects that key input features work correctly.
 *
 * @author SlavSquatSuperstar
 */
public class KeyInputTester extends Script {

    private static final Vec2 KEY_SIZE = new Vec2(6);
    private static final KeySprite[] KEY_SPRITES = {
            new KeySprite(
                    "w",
                    new Vec2(-30, -5), KEY_SIZE
            ),
            new KeySprite(
                    "s",
                    new Vec2(-30, -11), KEY_SIZE
            ),
            new KeySprite(
                    "a",
                    new Vec2(-36, -11), KEY_SIZE
            ),
            new KeySprite(
                    "d",
                    new Vec2(-24, -11), KEY_SIZE
            ),
    };

    private boolean toggleEnabled;

    @Override
    protected void start() {
        toggleEnabled = false;
    }

    @Override
    protected void update(float dt) {
        // Draw key axes
        var axes = new Vec2(KeyInput.getAxis("horizontal"),
                KeyInput.getAxis("vertical"));
        getScene().getDebugDraw().drawVector(
                new Vec2(-30, 15), axes.unit().mul(8), Colors.GREEN);

        getScene().getDebugDraw().drawShape(
                new Circle(new Vec2(-30, 15), 9f), Colors.BLACK);

        // Draw keys held
        for (var key : KEY_SPRITES) {
            var keyRect = new Rectangle(key.position, key.size);

            var fillColor = KeyInput.keyDown(key.name)
                    ? Colors.GRAY : Colors.LIGHT_GRAY;
            getScene().getDebugDraw().fillShape(keyRect, fillColor);
            getScene().getDebugDraw().drawShape(keyRect, Colors.BLACK);
        }

        // Test keys pressed
        if (KeyInput.keyPressed("space")) {
            toggleEnabled = !toggleEnabled;
        }

        var toggleColor = toggleEnabled ? Colors.DARK_RED : Colors.DARK_BLUE;
        var toggleRect = new Rectangle(new Vec2(-30, -20), new Vec2(18, 6));
        getScene().getDebugDraw().fillShape(toggleRect, toggleColor);
        getScene().getDebugDraw().drawShape(toggleRect, Colors.BLACK);
    }

    // Helper Class

    private record KeySprite(
            String name, Vec2 position, Vec2 size
    ) {
    }

}
