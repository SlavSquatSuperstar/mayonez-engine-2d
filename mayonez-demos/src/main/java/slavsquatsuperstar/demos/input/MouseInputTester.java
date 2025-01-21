package slavsquatsuperstar.demos.input;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.math.shapes.*;

/**
 * Detects that mouse input features work correctly.
 *
 * @author SlavSquatSuperstar
 */
public class MouseInputTester extends Script {

    private boolean dragged;
    private Vec2 dragStart;

    @Override
    protected void start() {
        dragged = false;
        dragStart = null;
    }

    @Override
    protected void update(float dt) {
        var pos = MouseInput.getPosition();

        // Update drag start
        if (!dragged && MouseInput.isAnyDown()) {
            dragged = true;
            dragStart = pos;
        } else if (dragged && !MouseInput.isAnyDown()) {
            dragged = false;
            dragStart = null;
        }

        // Draw buttons held
        var buttonSize = new Vec2(6, 12);
        var buttonPositions = new Vec2[]{
                new Vec2(30, -11),
                new Vec2(36, -11),
        };
        var buttonNames = new String[]{"left mouse", "right mouse"};

        for (var i = 0; i < buttonPositions.length; i++) {
            Color fillColor;
            if (MouseInput.buttonDown(buttonNames[i])) fillColor = Colors.GRAY;
            else fillColor = Colors.LIGHT_GRAY;
            getScene().getDebugDraw().fillShape(
                    new Rectangle(buttonPositions[i], buttonSize), fillColor);

            getScene().getDebugDraw().drawShape(
                    new Rectangle(buttonPositions[i], buttonSize), Colors.BLACK);
        }

        var color = Colors.ORANGE;

        // Draw circle(s) at click positions
        getScene().getDebugDraw().drawShape(new Circle(pos, 1), color);
        if (MouseInput.isDoubleClick()) {
            getScene().getDebugDraw().drawShape(new Circle(pos, 2), color);
        }

        // Draw move displacement
        getScene().getDebugDraw().drawVector(pos,
                MouseInput.getDisplacement().mul(-1), color);

        // Draw drag displacement
        if (dragged) {
            getScene().getDebugDraw().drawLine(dragStart, pos, color);
        }

        // Draw scroll displacement
        var scroll = MouseInput.getScroll();
        if (scroll.len() > 0f) {
            getScene().getDebugDraw().drawVector(
                    new Vec2(30, 15), scroll, Colors.GREEN);
        }

        getScene().getDebugDraw().drawShape(
                new Circle(new Vec2(30, 15), 9f), Colors.BLACK);
    }

}
