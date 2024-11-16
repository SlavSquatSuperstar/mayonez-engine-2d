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
public class RenderTestScene extends Scene {

    private static final boolean CAMERA_DEBUG_MODE = true;

    public RenderTestScene(String name) {
        super(name, 10);
    }

    @Override
    protected void init() {
        setBackground(Color.grayscale(96));

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

        addTextureObject("ship1-1", new Vec2(-10, 10), 0, tex1);
        addTextureObject("ship1-2", new Vec2(0, 10), 2, tex1);
        addTextureObject("ship1-3", new Vec2(10, 10), 4, tex1);

        addTextureObject("ship2-1", new Vec2(-10, 0), 0, tex2);
        addTextureObject("ship2-2", new Vec2(0, 0), 2, tex2);
        addTextureObject("ship2-3", new Vec2(10, 0), 4, tex2);

        addTextureObject("sat-1", new Vec2(-10, -10), 0, tex3);
        addTextureObject("sat-2", new Vec2(0, -10), 2, tex3);
        addTextureObject("sat-3", new Vec2(10, -10), 4, tex3);

        addTextureObject("proj-1", new Vec2(-10, -30), 0, sheet2.getTexture(0));
        addTextureObject("proj-2", new Vec2(0, -30), 2, sheet2.getTexture(1));
        addTextureObject("proj-3", new Vec2(10, -30), 4, sheet2.getTexture(2));

        addShapeObject("square-1", new Vec2(5, 5), 3, Colors.RED);
        addShapeObject("square-2", new Vec2(-5, 5), 1, Colors.GREEN);
        addShapeObject("square-3", new Vec2(-5, -5), 1, Colors.BLUE);
        addShapeObject("square-3", new Vec2(5, -5), 3, Colors.ORANGE);

        addAnimatedObject("anim-1", new Vec2(0, 15), 3, sheet1);
        addAnimatedObject("anim-2", new Vec2(0, -15), 1, sheet1);

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

    private void addTextureObject(String name, Vec2 pos, int zIndex, Texture tex) {
        addObject(new GameObject(name, new Transform(pos, 0f, new Vec2(10f))) {
            @Override
            protected void init() {
                setZIndex(zIndex);
                addComponent(Sprites.createSprite(tex));
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

    private void addShapeObject(String name, Vec2 pos, int zIndex, Color color) {
        addObject(new GameObject(name, pos) {
            @Override
            protected void init() {
                setZIndex(zIndex);
                addComponent(new Script() {
                    @Override
                    protected void debugRender() {
                        getDebugDraw().fillShape(
                                new Rectangle(pos, new Vec2(10, 10)),
                                new ShapeBrush(color, true, zIndex, 1)
                        );
                    }
                });
            }
        });
    }

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
                    .div(getCamera().getZoom() * getScale() / 10f);

            getCamera().getTransform().move(translation);
            getCamera().rotate(KeyInput.getAxis("arrows horizontal"));
            getCamera().zoom(1 + 0.01f * KeyInput.getAxis("arrows vertical"));
        }
    }

}
