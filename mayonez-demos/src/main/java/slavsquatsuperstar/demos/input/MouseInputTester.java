package slavsquatsuperstar.demos.input;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.ShapeBrush;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.math.shapes.*;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Detects that mouse input features work correctly.
 *
 * @author SlavSquatSuperstar
 */
public class MouseInputTester extends Script {

    private static final ButtonSprite[] MOUSE_BUTTONS = {
            new ButtonSprite(
                    "left mouse",
                    new Vec2(27, -12), new Vec2(6, 15),
                    0
            ),
            new ButtonSprite(
                    "right mouse",
                    new Vec2(33, -12), new Vec2(6, 15),
                    0
            ),
            new ButtonSprite(
                    "middle mouse",
                    new Vec2(30, -9), new Vec2(2, 4),
                    1
            ),
    };
    private static final float DOUBLE_CLICK_TIME = 0.5f;
    private static final float DOUBLE_CLICK_RADIUS = 2f;
    private static final float MOUSE_DISPLACEMENT_TIME = 0.25f;

    private Deque<MouseMotion> mouseMotions;
    private boolean dragged;
    private Vec2 dragStart;
    private Deque<DoubleClick> doubleClicks;

    @Override
    protected void start() {
        mouseMotions = new ArrayDeque<>();
        dragged = false;
        dragStart = null;
        doubleClicks = new ArrayDeque<>();
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
        for (var button : MOUSE_BUTTONS) {
            var buttonRect = new Rectangle(button.position, button.size);

            var fillColor = MouseInput.buttonDown(button.name)
                    ? Colors.GRAY : Colors.LIGHT_GRAY;
            var fillBrush = ShapeBrush.createSolidBrush(fillColor)
                    .setZIndex(button.zIndex);
            getScene().getDebugDraw().fillShape(buttonRect, fillBrush);

            var outlineBrush = ShapeBrush.createOutlineBrush(Colors.BLACK)
                    .setZIndex(button.zIndex);
            getScene().getDebugDraw().drawShape(buttonRect, outlineBrush);
        }

        var color = Colors.ORANGE;

        // Draw circle at click position
        getScene().getDebugDraw().drawShape(new Circle(pos, 1), color);

        // Draw last double clicks
        if (MouseInput.isDoubleClick()) {
            doubleClicks.offer(new DoubleClick(pos));
        }

        for (var click : doubleClicks) {
            var percentLeft = click.timer / DOUBLE_CLICK_TIME;
            var radius = DOUBLE_CLICK_RADIUS * percentLeft * percentLeft;
            getScene().getDebugDraw().drawShape(new Circle(click.position, radius), color);
            click.timer -= dt;

            if (click.timer < 0f) doubleClicks.remove(click);
        }

        // Draw move displacements
        var disp = MouseInput.getDisplacement().mul(-1);
        mouseMotions.offer(new MouseMotion(pos, disp));

        for (var motion : mouseMotions) {
            getScene().getDebugDraw().drawVector(motion.position, motion.displacement.mul(0.8f), Colors.ORANGE);
            motion.timer -= dt;

            if (motion.timer < 0f) mouseMotions.remove(motion);
        }

        // Draw drag displacement
        if (dragged) {
            getScene().getDebugDraw().drawLine(dragStart, pos, color);
        }

        // Draw scroll displacement
        var scroll = MouseInput.getScroll();
        getScene().getDebugDraw().drawVector(
                new Vec2(30, 15), scroll.unit().mul(8), Colors.GREEN);
        getScene().getDebugDraw().drawShape(
                new Circle(new Vec2(30, 15), 9f), Colors.BLACK);
    }

    // Helper Classes

    private record ButtonSprite(
            String name, Vec2 position, Vec2 size, int zIndex
    ) {
    }

    private static class DoubleClick {
        final Vec2 position;
        float timer;

        DoubleClick(Vec2 position) {
            this.position = position;
            timer = DOUBLE_CLICK_TIME;
        }
    }

    private static class MouseMotion {
        final Vec2 position, displacement;
        float timer;

        MouseMotion(Vec2 position, Vec2 displacement) {
            this.position = position;
            this.displacement = displacement;
            timer = MOUSE_DISPLACEMENT_TIME;
        }
    }

}
