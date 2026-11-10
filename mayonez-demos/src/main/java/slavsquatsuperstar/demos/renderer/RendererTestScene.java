package slavsquatsuperstar.demos.renderer;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.graphics.font.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import slavsquatsuperstar.demos.DemoScene;

/**
 * A scene for testing renderer functions, such as camera transformation, batch
 * rendering, z-indexing, and font rendering.
 *
 * @author SlavSquatSuperstar
 */
public class RendererTestScene extends DemoScene {

    private static final boolean CAMERA_DEBUG_MODE = true;
    private static final int SCENE_SCALE = 10;

    // See https://en.wikipedia.org/wiki/Pangram for more pangrams
    private static final String UI_MESSAGE = """
            The quick brown
            fox jumps over
            the lazy dog.
            Pack my box
            with five dozen
            liquor jugs.
            """;
    private static final String WORLD_MESSAGE = """
            (ABC)[DEF]
            {GHI}<JKL>
            \\MNO/"PQR"
            |STU|'VWX'
            1+2-3*4÷5
            Agpqxy
            ÁÄÅáäå
            """;

    public RendererTestScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        getCamera().setCameraScale(SCENE_SCALE);
        getCamera().setBackgroundColor(Color.grayscale(96));

        var bgTex = Textures.getTexture("assets/mario/textures/background.png");

        var tex1 = Textures.getTexture("assets/spacegame/textures/ships/shuttle.png");
        var tex2 = Textures.getTexture("assets/spacegame/textures/ships/fighter.png");
        var tex3 = Textures.getTexture("assets/spacegame/textures/ships/satellite.png");
        var tex4 = Textures.getTexture("assets/spacegame/textures/ui/gray_background.png");
        var tex5 = Textures.getTexture("assets/spacegame/textures/ui/black_border.png");

        var sheet1 = Sprites.createSpriteSheet("assets/spacegame/textures/combat/explosion.png",
                32, 32, 8, 0);
        var sheet2Size = 4;
        var sheet2 = Sprites.createSpriteSheet("assets/spacegame/textures/combat/projectiles.png",
                16, 16, sheet2Size, 0);

        // Sprites

        addNode(new Node("background-1",
                Transform.scaleInstance(new Vec2(1920, 1024).div(SCENE_SCALE))) {
            @Override
            protected void init() {
                var sprite = Sprites.createSprite(bgTex);
                sprite.setZIndex(-10);
                addChild(sprite);
            }
        });

        addTextureObject("ship1-1", new Vec2(-10, 10), 0, tex1, Colors.YELLOW);
        addTextureObject("ship1-2", new Vec2(0, 10), 2, tex1, Colors.WHITE);
        addTextureObject("ship1-3", new Vec2(10, 10), 4, tex1, Colors.LIGHT_BLUE);

        addTextureObject("ship2-1", new Vec2(-10, 0), 0, tex2, Colors.YELLOW);
        addTextureObject("ship2-2", new Vec2(0, 0), 2, tex2, Colors.WHITE);
        addTextureObject("ship2-3", new Vec2(10, 0), 4, tex2, Colors.LIGHT_BLUE);

        addTextureObject("sat-1", new Vec2(-10, -10), 0, tex3, Colors.YELLOW);
        addTextureObject("sat-2", new Vec2(0, -10), 2, tex3, Colors.WHITE);
        addTextureObject("sat-3", new Vec2(10, -10), 4, tex3, Colors.LIGHT_BLUE);

        addAnimatedObject("anim-1", new Vec2(0, 15), 3, sheet1);
        addAnimatedObject("anim-2", new Vec2(0, -15), 1, sheet1);

        // Shapes

        var center = new Vec2(30, 18);
        addShapeObject("tri-1",
                new Polygon(center, 3, 4f).rotate(90, null),
                1, Colors.DARK_GREEN);
        addShapeObject("poly-1",
                new Polygon(center, 5, 5f).rotate(90, null),
                0, Colors.DARK_BLUE);
        addShapeObject("poly-2",
                new Polygon(center, 6, 6f).rotate(90, null),
                -1, Colors.PURPLE);
        addShapeObject("poly-3",
                new Polygon(center, 8, 7f).rotate(90, null),
                -2, Colors.DARK_RED);

        addSquareObject("square-1", new Vec2(5, 5), 3, Colors.RED);
        addSquareObject("square-2", new Vec2(-5, 5), 1, Colors.GREEN);
        addSquareObject("square-3", new Vec2(-5, -5), 1, Colors.BLUE);
        addSquareObject("square-3", new Vec2(5, -5), 3, Colors.ORANGE);

        addCircleObject("circle-1", new Vec2(30, 7.5f), 2.5f, 3, Colors.BLUE);
        addCircleObject("circle-2", new Vec2(30, 5), 5, 2, Colors.GREEN);
        addCircleObject("circle-3", new Vec2(30, 2.5f), 7.5f, 1, Colors.ORANGE);
        addCircleObject("circle-4", new Vec2(30, 0), 10, 0, Colors.RED);

        addLineObject("line-1", new Vec2(20, -23), new Vec2(40, -30), 0, Colors.RED);
        addLineObject("line-2", new Vec2(20, -22), new Vec2(40, -31), 1, Colors.ORANGE);
        addLineObject("line-2", new Vec2(20, -21), new Vec2(40, -32), 2, Colors.GREEN);
        addLineObject("line-2", new Vec2(20, -20), new Vec2(40, -33), 3, Colors.BLUE);

        // UI

        float uiStartPos = 64;
        for (int i = 0; i < sheet2Size; i++) {
            var prefix = "ui-" + (i + 1);
            var xPos = uiStartPos + 72 * i;
            addUIObject(prefix + 'a', new Vec2(xPos, uiStartPos), 10, tex4);
            addUIObject(prefix + 'b', new Vec2(xPos, uiStartPos), 11, tex5);
            addUIObject(prefix + 'c', new Vec2(xPos, uiStartPos), 12, sheet2.getTexture(i));
        }

        addNode(new Node("text-1") {
            private TextAlignment align = TextAlignment.LEFT;
            private TextLabel worldText, uiText;

            @Override
            protected void init() {
                worldText = new TextLabel(
                        WORLD_MESSAGE, new Vec2(-58, 12))
                        .setInUI(false)
                        .setColor(Colors.BLUE)
                        .setFontSize(6)
                        .setAnchor(Anchor.TOP_LEFT);
                addChild(worldText);

                uiText = new TextLabel(
                        UI_MESSAGE, new Vec2(20, Mayonez.getScreenHeight() - 20))
                        .setInUI(true)
                        .setColor(Colors.RED)
                        .setFontSize(40)
                        .setAnchor(Anchor.TOP_LEFT);
                addChild(uiText);
            }

            @Override
            protected void update(float dt) {
                // Toggle font alignment
                if (KeyInput.keyPressed("space")) {
                    switch (align) {
                        case LEFT -> align = TextAlignment.CENTER;
                        case CENTER -> align = TextAlignment.RIGHT;
                        case RIGHT -> align = TextAlignment.LEFT;
                    }

                    // Set alignment
                    worldText.setAlignment(align);
                    uiText.setAlignment(align);
                }
            }
        });
    }

    // Sprites

    private void addTextureObject(String name, Vec2 pos, int zIndex, Texture tex, Color color) {
        addNode(new Node(name,
                new Transform(pos, 0f, new Vec2(10f))) {
            @Override
            protected void init() {
                var sprite = Sprites.createSprite(tex);
                sprite.setColor(color);
                sprite.setZIndex(zIndex);
                addChild(sprite);
            }
        });
    }

    private void addAnimatedObject(String name, Vec2 pos, int zIndex, SpriteSheet sheet) {
        addNode(new Node(name,
                new Transform(pos, 0f, new Vec2(10f))) {
            @Override
            protected void init() {
                var animator = new Animator(sheet, 0.125f);
                animator.setZIndex(zIndex);
                addChild(animator);
            }
        });
    }

    // Shapes

    private void addShapeObject(String name, Shape shape, int zIndex, Color color) {
        var fillBrush = ShapeBrush.createSolidBrush(color).setZIndex(zIndex);
        var drawBrush = ShapeBrush.createOutlineBrush(Colors.BLACK).setZIndex(zIndex).setStrokeSize(2);

        addNode(new Node(name) {
            @Override
            protected void debugRender() {
                getDebugDraw().fillShape(shape, fillBrush);
                getDebugDraw().drawShape(shape, drawBrush);
            }
        });
    }

    private void addSquareObject(String name, Vec2 pos, int zIndex, Color color) {
        var shape = new Rectangle(pos, new Vec2(10, 10));
        addShapeObject(name, shape, zIndex, color);
    }

    private void addCircleObject(String name, Vec2 pos, float radius, int zIndex, Color color) {
        var shape = new Circle(pos, radius);
        addShapeObject(name, shape, zIndex, color);
    }

    private void addLineObject(String name, Vec2 start, Vec2 end, int zIndex, Color color) {
        addNode(new Node(name) {
            @Override
            protected void debugRender() {
                getDebugDraw().drawLine(
                        start, end,
                        new ShapeBrush(color, false, zIndex, 10)
                );
            }
        });
    }

    // UI Objects

    private void addUIObject(String name, Vec2 pos, int zIndex, Texture tex) {
        addNode(new UISprite(pos, new Vec2(64, 64), tex) {
            @Override
            protected void init() {
                setName(name);
                setZIndex(zIndex);
            }
        });
    }

    @Override
    protected void onUserUpdate(float dt) {
        super.onUserUpdate(dt);
        if (CAMERA_DEBUG_MODE) {
            var moveInput = new Vec2(KeyInput.getAxis("horizontal"), KeyInput.getAxis("vertical"));
            var translation = moveInput.unit()
                    .rotate(getCamera().getRotation())
                    .div(getCamera().getZoom());

            getCamera().getTransform().move(translation);
            getCamera().rotate(KeyInput.getAxis("arrows horizontal"));
            getCamera().zoom(1 + 0.01f * KeyInput.getAxis("arrows vertical"));
        }
    }

}
