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

        Color color;
        if (MouseInput.buttonDown("left mouse")) {
            color = Colors.BLUE;
        } else if (MouseInput.buttonDown("right mouse")) {
            color = Colors.GREEN;
        } else {
            color = Colors.WHITE;
        }

        // Draw circle(s) at click positions
        getScene().getDebugDraw().drawShape(new Circle(pos, 1), color);
        if (MouseInput.isDoubleClick()) {
            getScene().getDebugDraw().drawShape(new Circle(pos, 2), color);
        }

        // Draw move displacement
        getScene().getDebugDraw().drawVector(pos,
                MouseInput.getDisplacement().mul(-1), Colors.RED);

        // Draw drag displacement
        if (dragged) {
            getScene().getDebugDraw().drawLine(dragStart, pos, color);
        }

        // Draw scroll displacement
        var scroll = MouseInput.getScroll();
        if (scroll.len() > 0f) {
            getScene().getDebugDraw().drawVector(pos, scroll, Colors.ORANGE);
        }
    }

}
