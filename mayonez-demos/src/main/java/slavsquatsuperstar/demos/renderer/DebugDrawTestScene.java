package slavsquatsuperstar.demos.renderer;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.graphics.font.*;
import mayonez.graphics.ui.*;
import mayonez.input.KeyInput;
import mayonez.math.*;
import slavsquatsuperstar.demos.DemoScene;
import slavsquatsuperstar.demos.ShapeUtils;

import java.util.List;

/**
 * A scene for testing the performance of debug shape rendering.
 *
 * @author SlavSquatSuperstar
 */
public class DebugDrawTestScene extends DemoScene {

    private static final Vec2 SCENE_HALF_SIZE
            = new Vec2(Mayonez.getScreenWidth(), Mayonez.getScreenHeight())
            .div(2f);
    private static final Color BACKGROUND_COLOR
            = new Color(192, 192, 192, 220);
    private static final int SHAPE_AMOUNT = 500;

    private static final float MIN_LENGTH = 8f;
    private static final float MAX_LENGTH = 32f;
    private static final int MIN_STROKE = 1;
    private static final int MAX_STROKE = 3;

    private final List<DebugShapeType> types = List.of(
            new DebugShapeType("Balls", () -> ShapeUtils
                    .getRandomEllipse(getRandomPosition(), MIN_LENGTH, MAX_LENGTH)),
            new DebugShapeType("Boxes", () -> ShapeUtils
                    .getRandomRectangle(getRandomPosition(), MIN_LENGTH, MAX_LENGTH)),
            new DebugShapeType("Polys", () -> ShapeUtils
                    .getRandomPolygon(getRandomPosition(), MIN_LENGTH / 2f, MAX_LENGTH / 2f)),
            new DebugShapeType("Lines", () -> ShapeUtils
                    .getRandomEdge(getRandomPosition(), MIN_LENGTH, MAX_LENGTH))
    );

    private TextLabel totalText;

    public DebugDrawTestScene(String name) {
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
                        new Vec2(120, Mayonez.getScreenHeight() - 330),
                        new Vec2(220, 220), BACKGROUND_COLOR
                ));

                // Num circles
                addChild(types.get(0).text = new TextLabel(
                        "Balls: _",
                        new Vec2(20, Mayonez.getScreenHeight() - 240)
                ).setAnchor(Anchor.TOP_LEFT)
                        .setFontSize(32));

                // Num rectangles
                addChild(types.get(1).text = new TextLabel(
                        "Boxes: _",
                        new Vec2(20, Mayonez.getScreenHeight() - 280)
                ).setAnchor(Anchor.TOP_LEFT)
                        .setFontSize(32));

                // Num polygons
                addChild(types.get(2).text = new TextLabel(
                        "Polys: _",
                        new Vec2(20, Mayonez.getScreenHeight() - 320)
                ).setAnchor(Anchor.TOP_LEFT)
                        .setFontSize(32));

                // Num polygons
                addChild(types.get(3).text = new TextLabel(
                        "Lines: _",
                        new Vec2(20, Mayonez.getScreenHeight() - 360)
                ).setAnchor(Anchor.TOP_LEFT)
                        .setFontSize(32));

                // Total shapes
                totalText = new TextLabel(
                        "Total: _",
                        new Vec2(20, Mayonez.getScreenHeight() - 400)
                );
                addChild(totalText
                        .setAnchor(Anchor.TOP_LEFT)
                        .setFontSize(32));
            }
        });

        types.forEach(DebugShapeType::clear);
        addShapes();
    }

    @Override
    protected void onUserRender() {
        var debugDraw = getDebugDraw();
        types.forEach(type -> type.draw(debugDraw));
    }

    @Override
    protected void onUserUpdate(float dt) {
        super.onUserUpdate(dt);
        if (KeyInput.keyPressed("=")) {
            addShapes();
        }
        if (KeyInput.keyPressed("-")) {
            // Remove shapes
            types.forEach(type -> type.remove(SHAPE_AMOUNT));
            updateLabels();
        }
    }

    private void addShapes() {
        // Add shapes
        // TODO select which shapes to add
        for (int i = 0; i < SHAPE_AMOUNT; i++) {
            types.forEach(type -> type.addOne(getRandomBrush()));
        }
        updateLabels();
    }

    private void updateLabels() {
        var total = 0;
        for (var type : types) {
            type.updateText();
            total += type.count();
        }
        totalText.setMessage("Total: " + total);
    }

    // Helper Methods

    private static ShapeBrush getRandomBrush() {
        var color = Colors.randomColor();
        var fill = Random.randomPercent(0.25f);
        // TODO limit max stroke based on size
        var stroke = Random.randomInt(MIN_STROKE, MAX_STROKE);
        var zIndex = Random.randomInt(-25, 25);
        return new ShapeBrush(color, fill, zIndex, stroke);
    }

    private static Vec2 getRandomPosition() {
        return Random.randomVector(
                SCENE_HALF_SIZE.mul(-1f), SCENE_HALF_SIZE
        );
    }

}
