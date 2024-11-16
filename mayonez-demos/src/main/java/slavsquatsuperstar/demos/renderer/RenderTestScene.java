package slavsquatsuperstar.demos.renderer;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import mayonez.math.shapes.*;

/**
 * A scene for testing z-indexing and batch rendering.
 *
 * @author SlavSquatSuperstar
 */
public class RenderTestScene extends Scene {

    public RenderTestScene(String name) {
        super(name, 100);
    }

    @Override
    protected void init() {
        setBackground(Colors.DARK_GRAY);

        var tex1 = Textures.getTexture("assets/spacegame/textures/ships/spaceship1.png");
        var tex2 = Textures.getTexture("assets/spacegame/textures/ships/spaceship2.png");
        var tex3 = Textures.getTexture("assets/spacegame/textures/ships/satellite.png");
        var tex4 = Textures.getTexture("assets/spacegame/textures/ui/gray_background.png");
        var tex5 = Textures.getTexture("assets/spacegame/textures/ui/black_border.png");
        var sheet1 = Sprites.createSpriteSheet("assets/spacegame/textures/combat/explosion.png",
                32, 32, 8, 0);
        var sheet2 = Sprites.createSpriteSheet("assets/spacegame/textures/combat/projectiles.png",
                16, 16, 3, 0);

        addTextureObject("ship1-1", new Vec2(-1, 1), 0, tex1);
        addTextureObject("ship1-2", new Vec2(0, 1), 2, tex1);
        addTextureObject("ship1-3", new Vec2(1, 1), 4, tex1);

        addTextureObject("ship2-1", new Vec2(-1, 0), 0, tex2);
        addTextureObject("ship2-2", new Vec2(0, 0), 2, tex2);
        addTextureObject("ship2-3", new Vec2(1, 0), 4, tex2);

        addTextureObject("sat-1", new Vec2(-1, -1), 0, tex3);
        addTextureObject("sat-2", new Vec2(0, -1), 2, tex3);
        addTextureObject("sat-3", new Vec2(1, -1), 4, tex3);

        addTextureObject("proj-1", new Vec2(3, 1), 0, sheet2.getTexture(0));
        addTextureObject("proj-2", new Vec2(3, 0), 2, sheet2.getTexture(1));
        addTextureObject("proj-3", new Vec2(3, -1), 4, sheet2.getTexture(2));

        addShapeObject("square-1", new Vec2(0.5f, 0.5f), 3, Colors.RED);
        addShapeObject("square-2", new Vec2(-0.5f, 0.5f), 1, Colors.GREEN);
        addShapeObject("square-3", new Vec2(-0.5f, -0.5f), 1, Colors.BLUE);
        addShapeObject("square-3", new Vec2(0.5f, -0.5f), 3, Colors.ORANGE);

        addAnimatedObject("anim-1", new Vec2(0, 1.5f), 3, sheet1);
        addAnimatedObject("anim-2", new Vec2(0, -1.5f), 1, sheet1);

        addUIObject("ui-1a", new Vec2(128, 128), 10, tex4);
        addUIObject("ui-1b", new Vec2(128, 128), 11, tex5);
        addUIObject("ui-1c", new Vec2(128, 128), 12, sheet2.getTexture(0));

        addUIObject("ui-2a", new Vec2(192, 128), 10, tex4);
        addUIObject("ui-2b", new Vec2(192, 128), 11, tex5);
        addUIObject("ui-2c", new Vec2(192, 128), 12, sheet2.getTexture(1));

        addUIObject("ui-3a", new Vec2(256, 128), 10, tex4);
        addUIObject("ui-3b", new Vec2(256, 128), 11, tex5);
        addUIObject("ui-3c", new Vec2(256, 128), 12, sheet2.getTexture(2));
    }

    private void addTextureObject(String obj1, Vec2 pos, int zIndex, Texture tex) {
        addObject(new GameObject(obj1, pos) {
            @Override
            protected void init() {
                setZIndex(zIndex);
                addComponent(Sprites.createSprite(tex));
            }
        });
    }

    private void addAnimatedObject(String obj1, Vec2 pos, int zIndex, SpriteSheet sheet) {
        addObject(new GameObject(obj1, pos) {
            @Override
            protected void init() {
                setZIndex(zIndex);
                addComponent(new Animator(sheet, 0.125f));
            }
        });
    }

    private void addShapeObject(String obj1, Vec2 pos, int zIndex, Color color) {
        addObject(new GameObject(obj1, pos) {
            @Override
            protected void init() {
                setZIndex(zIndex);
                addComponent(new Script() {
                    @Override
                    protected void debugRender() {
                        getDebugDraw().fillShape(
                                new Rectangle(pos, new Vec2(1, 1)),
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

}
