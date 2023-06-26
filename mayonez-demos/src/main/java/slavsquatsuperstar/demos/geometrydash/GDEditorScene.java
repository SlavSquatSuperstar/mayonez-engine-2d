package slavsquatsuperstar.demos.geometrydash;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.util.*;
import slavsquatsuperstar.demos.geometrydash.components.Grid;
import slavsquatsuperstar.demos.geometrydash.ui.UICanvas;

/**
 * The Geometry Dash level editor scene.
 *
 * @author SlavSquatSuperstar
 */
public class GDEditorScene extends Scene {

    public GDEditorScene(String name) {
        super(name, Preferences.getScreenWidth(), Preferences.getScreenHeight(), 42);
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

}
