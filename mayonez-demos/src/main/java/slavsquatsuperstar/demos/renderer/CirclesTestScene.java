package slavsquatsuperstar.demos.renderer;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.graphics.font.*;
import mayonez.graphics.ui.*;
import mayonez.input.KeyInput;
import mayonez.math.*;
import mayonez.math.shapes.*;
import slavsquatsuperstar.demos.DemoScene;

import java.util.ArrayList;
import java.util.List;

/**
 * A scene for testing the efficiency of circle rendering.
 *
 * @author SlavSquatSuperstar
 */
public class CirclesTestScene extends DemoScene {

    private static final Vec2 SCENE_HALF_SIZE
            = new Vec2(Mayonez.getScreenWidth(), Mayonez.getScreenHeight())
            .div(2f);
    private static final Color BACKGROUND_COLOR
            = new Color(192, 192, 192, 220);

    private static final int SHAPES_PER_ACTION = 1000;

    private static final float MIN_RADIUS = 4f;
    private static final float MAX_RADIUS = 16f;
    private static final int MIN_STROKE = 1;
    private static final int MAX_STROKE = 3;

    private final List<TestShape> circles = new ArrayList<>();
    private final List<TestShape> rectangles = new ArrayList<>();
    private final List<TestShape> polygons = new ArrayList<>();

    TextLabel circlesText, rectanglesText, polygonsText, totalText;

    public CirclesTestScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        addNode(new Node("Debug Text") {
            @Override
            protected void init() {
                // Background 1
                addChild(new UISprite(
                        new Vec2(110, Mayonez.getScreenHeight() - 110),
                        new Vec2(200, 180), BACKGROUND_COLOR
                ));

                // Frames per second
                var fpsText = new TextLabel(
                        "FPS: _",
                        new Vec2(20, Mayonez.getScreenHeight() - 40)
                ) {
                    @Override
                    protected void update(float dt) {
                        setMessage("FPS: " + Mayonez.getRenderFPS());
                    }
                };
                addChild(fpsText
                        .setAnchor(Anchor.TOP_LEFT)
                        .setFontSize(32));

                // Ticks per second
                var tpsText = new TextLabel(
                        "TPS: _",
                        new Vec2(20, Mayonez.getScreenHeight() - 80)
                ) {
                    @Override
                    protected void debugRender() {
                        setMessage("TPS: " + Mayonez.getFixedTPS());
                    }
                };
                addChild(tpsText
                        .setAnchor(Anchor.TOP_LEFT)
                        .setFontSize(32));

                // V-Sync
                var vsyncText = new TextLabel(
                        "VSync: " + (Preferences.useVSync() ? "On" : "Off"),
                        new Vec2(20, Mayonez.getScreenHeight() - 120)
                );
                addChild(vsyncText
                        .setAnchor(Anchor.TOP_LEFT)
                        .setFontSize(32));

                // Max Ticks per Frame
                var ticksPerFrameText = new TextLabel(
                        "Max T/F: " + Preferences.getMaxTicksPerFrame(),
                        new Vec2(20, Mayonez.getScreenHeight() - 160)
                );
                addChild(ticksPerFrameText
                        .setAnchor(Anchor.TOP_LEFT)
                        .setFontSize(32));

                // Background 2
                addChild(new UISprite(
                        new Vec2(120, Mayonez.getScreenHeight() - 310),
                        new Vec2(220, 180), BACKGROUND_COLOR
                ));

                // Num circles
                circlesText = new TextLabel(
                        "Balls: _",
                        new Vec2(20, Mayonez.getScreenHeight() - 240)
                );
                addChild(circlesText
                        .setAnchor(Anchor.TOP_LEFT)
                        .setFontSize(32));

                // Num rectangles
                rectanglesText = new TextLabel(
                        "Boxes: _",
                        new Vec2(20, Mayonez.getScreenHeight() - 280)
                );
                addChild(rectanglesText
                        .setAnchor(Anchor.TOP_LEFT)
                        .setFontSize(32));

                // Num rectangles
                polygonsText = new TextLabel(
                        "Polys: _",
                        new Vec2(20, Mayonez.getScreenHeight() - 320)
                );
                addChild(polygonsText
                        .setAnchor(Anchor.TOP_LEFT)
                        .setFontSize(32));

                // Total shapes
                totalText = new TextLabel(
                        "Total: _",
                        new Vec2(20, Mayonez.getScreenHeight() - 360)
                );
                addChild(totalText
                        .setAnchor(Anchor.TOP_LEFT)
                        .setFontSize(32));
            }
        });

        circles.clear();
        rectangles.clear();
        polygons.clear();
        addShapes();
    }

    @Override
    protected void onUserRender() {
        var debugDraw = getDebugDraw();
        circles.forEach(shape -> debugDraw.drawShape(shape.shape, shape.brush));
        rectangles.forEach(shape -> debugDraw.drawShape(shape.shape, shape.brush));
        polygons.forEach(shape -> debugDraw.drawShape(shape.shape, shape.brush));
    }

    @Override
    protected void onUserUpdate(float dt) {
        super.onUserUpdate(dt);
        if (KeyInput.keyPressed("=")) {
            addShapes();
        }
        if (KeyInput.keyPressed("-")) {
            // Remove shapes
            if (!circles.isEmpty()) circles.subList(0, SHAPES_PER_ACTION).clear();
            if (!rectangles.isEmpty()) rectangles.subList(0, SHAPES_PER_ACTION).clear();
            if (!polygons.isEmpty()) polygons.subList(0, SHAPES_PER_ACTION).clear();
            updateLabels();
        }
    }

    private void addShapes() {
        // Add shapes
        for (int i = 0; i < SHAPES_PER_ACTION; i++) {
            circles.add(new TestShape(getRandomCircle(), getRandomBrush()));
            rectangles.add(new TestShape(getRandomRectangle(), getRandomBrush()));
            polygons.add(new TestShape(getRandomPolygon(), getRandomBrush()));
        }
        updateLabels();
    }

    private void updateLabels() {
        circlesText.setMessage("Balls: " + circles.size());
        rectanglesText.setMessage("Boxes: " + rectangles.size());
        polygonsText.setMessage("Polys: " + polygons.size());
        totalText.setMessage("Total: " + (circles.size() + rectangles.size() + polygons.size()));
    }

    // Helper Methods

    private static ShapeBrush getRandomBrush() {
        var color = Colors.randomColor();
        var fill = Random.randomPercent(0.25f);
        // TODO limit max stroke based on size
        var stroke = Random.randomInt(MIN_STROKE, MAX_STROKE);
        var zIndex = Random.randomInt(-10, 10);
        return new ShapeBrush(color, fill, zIndex, stroke);
    }

    private static Shape getRandomRectangle() {
        var isSquare = Random.randomPercent(0.25f);
        var width = Random.randomFloat(MIN_RADIUS, MAX_RADIUS) * 2f;
        var height = isSquare ? width : width * Random.randomFloat(0.5f, 2f);
        var size = new Vec2(width, height);
        var rotation = Random.randomAngle();
        return new Rectangle(getRandomPosition(), size, rotation);
    }

    private static Shape getRandomCircle() {
        var isEllipse = Random.randomPercent(0.75f);
        if (isEllipse) {
            var width = Random.randomFloat(MIN_RADIUS, MAX_RADIUS) * 2f;
            var height = width * Random.randomFloat(0.75f, 1.25f);
            var size = new Vec2(width, height);
            var rotation = Random.randomAngle();
            return new Ellipse(getRandomPosition(), size, rotation);
        } else {
            var radius = Random.randomFloat(MIN_RADIUS, MAX_RADIUS);
            return new Circle(getRandomPosition(), radius);
        }
    }

    private static Shape getRandomPolygon() {
        var isTriangle = Random.randomPercent(0.20f);
        var radius = Random.randomFloat(MIN_RADIUS, MAX_RADIUS);
        var vertices = isTriangle ? 3 : Random.randomInt(5, 10);
        return new Polygon(getRandomPosition(), vertices, radius)
                .rotate(Random.randomAngle(), null);
    }

    private static Vec2 getRandomPosition() {
        return Random.randomVector(
                SCENE_HALF_SIZE.mul(-1f), SCENE_HALF_SIZE
        );
    }

    // Helper Class

    private record TestShape(Shape shape, ShapeBrush brush) {
    }

}
