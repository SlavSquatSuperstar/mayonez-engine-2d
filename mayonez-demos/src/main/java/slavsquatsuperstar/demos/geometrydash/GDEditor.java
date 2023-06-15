package slavsquatsuperstar.demos.geometrydash;

import mayonez.*;
import mayonez.util.Colors;
import mayonez.graphics.sprites.JSpriteSheet;
import mayonez.graphics.sprites.ShapeSprite;
import mayonez.graphics.sprites.SpriteSheet;
import mayonez.init.*;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.demos.geometrydash.components.Grid;
import slavsquatsuperstar.demos.geometrydash.ui.UICanvas;

public class GDEditor extends Scene {

    public GDEditor() {
        super("Level Editor", Preferences.getScreenWidth(), Preferences.getScreenHeight(), 42);
        setGravity(new Vec2());
    }

    @Override
    protected void init() {
        addObject(new GameObject("Ground", new Vec2(0, getHeight() * -0.5f)) {
            @Override
            protected void init() {
                addComponent(new Rigidbody(0f).setFixedRotation(true));
                addComponent(new BoxCollider(new Vec2(getWidth() + 2f, 2f)));
                addComponent(new ShapeSprite(Colors.BLACK, true));
            }
        });

        // TODO still getting stuck on corners
        addObject(new GDPlayer("Player", new Vec2(0, 0)));

        addObject(new GameObject("Grid") {
            @Override
            protected void init() {
                addComponent(new Grid(new Vec2(getScale())));
            }
        });

        var blocks = SpriteSheet.create("assets/textures/geometrydash/blocks.png", 42, 42, 12, 2);
        addObject(new UICanvas("Canvas", new Transform(new Vec2(-5f, -5f)), (JSpriteSheet) blocks));

    }

    public static void main(String[] args) {
        Mayonez.setConfig(new RunConfig(false, RunConfig.DEFAULT_SAVE_LOGS));
        Mayonez.start(new GDEditor());
    }

}
