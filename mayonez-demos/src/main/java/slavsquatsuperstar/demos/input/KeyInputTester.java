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
        var keySize = new Vec2(6);
        var keyPositions = new Vec2[]{
                new Vec2(-30, -5),
                new Vec2(-36, -11),
                new Vec2(-30, -11),
                new Vec2(-24, -11),
        };
        var keyNames = new String[]{"w", "a", "s", "d"};

        for (var i = 0; i < keyPositions.length; i++) {
            Color fillColor;
            if (KeyInput.keyDown(keyNames[i])) fillColor = Colors.GRAY;
            else fillColor = Colors.LIGHT_GRAY;
            getScene().getDebugDraw().fillShape(
                    new Rectangle(keyPositions[i], keySize), fillColor);

            getScene().getDebugDraw().drawShape(
                    new Rectangle(keyPositions[i], keySize), Colors.BLACK);
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

}
