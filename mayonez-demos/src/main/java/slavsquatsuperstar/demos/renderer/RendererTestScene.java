package slavsquatsuperstar.demos.renderer;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import slavsquatsuperstar.demos.DemosAssets;

/**
 * A scene for testing renderer functions, such as camera transformation, batch
 * rendering, z-indexing, and font rendering.
 *
 * @author SlavSquatSuperstar
 */
public class RendererTestScene extends Scene {

    private static final boolean CAMERA_DEBUG_MODE = true;
    private static final int SCENE_SCALE = 10;

    public RendererTestScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        getCamera().setCameraScale(SCENE_SCALE);
        setBackground(Color.grayscale(96));

        var bgTex = Textures.getTexture("assets/mario/textures/background.png");

        var tex1 = Textures.getTexture("assets/spacegame/textures/ships/spaceship1.png");
        var tex2 = Textures.getTexture("assets/spacegame/textures/ships/spaceship2.png");
        var tex3 = Textures.getTexture("assets/spacegame/textures/ships/satellite.png");
        var tex4 = Textures.getTexture("assets/spacegame/textures/ui/gray_background.png");
        var tex5 = Textures.getTexture("assets/spacegame/textures/ui/black_border.png");

        var sheet1 = Sprites.createSpriteSheet("assets/spacegame/textures/combat/explosion.png",
                32, 32, 8, 0);
        var sheet2 = Sprites.createSpriteSheet("assets/spacegame/textures/combat/projectiles.png",
                16, 16, 3, 0);

        var font = DemosAssets.getFont();

        // Sprites

        addObject(new GameObject("background-1",
                Transform.scaleInstance(new Vec2(1920, 1024).div(SCENE_SCALE))) {
            @Override
            protected void init() {
                setZIndex(-10);
                var sprite = Sprites.createSprite(bgTex);
                addComponent(sprite);
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

        addTextureObject("proj-1", new Vec2(-10, -30), 0, sheet2.getTexture(0), Colors.WHITE);
        addTextureObject("proj-2", new Vec2(0, -30), 2, sheet2.getTexture(1), Colors.WHITE);
        addTextureObject("proj-3", new Vec2(10, -30), 4, sheet2.getTexture(2), Colors.WHITE);

        addAnimatedObject("anim-1", new Vec2(0, 15), 3, sheet1);
        addAnimatedObject("anim-2", new Vec2(0, -15), 1, sheet1);

        // Shapes

        addShapeObject("tri-1",
                new Triangle(new Vec2(27, 15), new Vec2(33, 15), new Vec2(30, 20)),
                1, Colors.DARK_GREEN);

        addShapeObject("poly-1",
                new Polygon(new Vec2(30, 17), 5, 5f).rotate(90, null),
                0, Colors.DARK_BLUE);

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
        addUIObject("ui-1a", new Vec2(uiStartPos, uiStartPos), 10, tex4);
        addUIObject("ui-1b", new Vec2(uiStartPos, uiStartPos), 11, tex5);
        addUIObject("ui-1c", new Vec2(uiStartPos, uiStartPos), 12, sheet2.getTexture(0));

        addUIObject("ui-2a", new Vec2(uiStartPos + 64, uiStartPos), 10, tex4);
        addUIObject("ui-2b", new Vec2(uiStartPos + 64, uiStartPos), 11, tex5);
        addUIObject("ui-2c", new Vec2(uiStartPos + 64, uiStartPos), 12, sheet2.getTexture(1));

        addUIObject("ui-3a", new Vec2(uiStartPos + 128, uiStartPos), 10, tex4);
        addUIObject("ui-3b", new Vec2(uiStartPos + 128, uiStartPos), 11, tex5);
        addUIObject("ui-3c", new Vec2(uiStartPos + 128, uiStartPos), 12, sheet2.getTexture(2));

        addObject(new FontTestObject("text-1", font));
    }

    // Sprites

    private void addTextureObject(String name, Vec2 pos, int zIndex, Texture tex, Color color) {
        addObject(new GameObject(name, new Transform(pos, 0f, new Vec2(10f))) {
            @Override
            protected void init() {
                setZIndex(zIndex);
                var sprite = Sprites.createSprite(tex);
                sprite.setColor(color);
                addComponent(sprite);
            }
        });
    }

    private void addAnimatedObject(String name, Vec2 pos, int zIndex, SpriteSheet sheet) {
        addObject(new GameObject(name, new Transform(pos, 0f, new Vec2(10f))) {
            @Override
            protected void init() {
                setZIndex(zIndex);
                addComponent(new Animator(sheet, 0.125f));
            }
        });
    }

    // Shapes

    private void addShapeObject(String name, Shape shape, int zIndex, Color color) {
        var fillBrush = ShapeBrush.createSolidBrush(color).setZIndex(zIndex);
        var drawBrush = ShapeBrush.createOutlineBrush(Colors.BLACK).setZIndex(zIndex).setStrokeSize(2);

        addObject(new GameObject(name) {
            @Override
            protected void init() {
                addComponent(new Script() {
                    @Override
                    protected void debugRender() {
                        getDebugDraw().fillShape(shape, fillBrush);
                        getDebugDraw().drawShape(shape, drawBrush);
                    }
                });
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
        addObject(new GameObject(name) {
            @Override
            protected void init() {
                addComponent(new Script() {
                    @Override
                    protected void debugRender() {
                        getDebugDraw().drawLine(
                                start, end,
                                new ShapeBrush(color, false, zIndex, 10)
                        );
                    }
                });
            }
        });
    }

    // UI Objects

    private void addUIObject(String obj1, Vec2 pos, int zIndex, Texture tex) {
        addObject(new GameObject(obj1, pos) {
            @Override
            protected void init() {
                setZIndex(zIndex);
                addComponent(new UISprite(pos, new Vec2(64, 64), tex));
            }
        });
    }

    @Override
    protected void onUserUpdate(float dt) {
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
