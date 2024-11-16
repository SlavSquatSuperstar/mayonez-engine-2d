package slavsquatsuperstar.demos.font;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.math.shapes.*;

/**
 * A scene for testing z-indexing and batch rendering.
 *
 * @author SlavSquatSuperstar
 */
public class RenderTestScene extends Scene {

    public RenderTestScene(String name) {
        super(name, 0, 0, 100);
    }

    @Override
    protected void init() {
        setBackground(Colors.DARK_GRAY);

        var tex1 = Textures.getTexture("assets/spacegame/textures/ships/spaceship1.png");
        var tex2 = Textures.getTexture("assets/spacegame/textures/ships/spaceship2.png");
        var tex3 = Textures.getTexture("assets/spacegame/textures/ships/satellite.png");
        var sheet1 = Sprites.createSpriteSheet("assets/spacegame/textures/combat/explosion.png",
                32, 32, 8, 0);

        addTextureObject("ship1-1", new Vec2(-1, 1), 0, tex1);
        addTextureObject("ship1-2", new Vec2(0, 1), 2, tex1);
        addTextureObject("ship1-3", new Vec2(1, 1), 4, tex1);

        addTextureObject("ship2-1", new Vec2(-1, 0), 0, tex2);
        addTextureObject("ship2-2", new Vec2(0, 0), 2, tex2);
        addTextureObject("ship2-3", new Vec2(1, 0), 4, tex2);

        addTextureObject("sat-1", new Vec2(-1, -1), 0, tex3);
        addTextureObject("sat-2", new Vec2(0, -1), 2, tex3);
        addTextureObject("sat-3", new Vec2(1, -1), 4, tex3);

        addShapeObject("square-1", new Vec2(0.5f, 0.5f), 3, Colors.RED);
        addShapeObject("square-2", new Vec2(-0.5f, 0.5f), 1, Colors.GREEN);
        addShapeObject("square-3", new Vec2(-0.5f, -0.5f), 1, Colors.BLUE);
        addShapeObject("square-3", new Vec2(0.5f, -0.5f), 3, Colors.ORANGE);

        addAnimatedObject("anim-1", new Vec2(0, 1.5f), 3, sheet1);
        addAnimatedObject("anim-2", new Vec2(0, -1.5f), 1, sheet1);
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
                addComponent(new Animator(sheet, 0.25f));
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

}
