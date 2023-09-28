package slavsquatsuperstar.demos.geometrydash;

import mayonez.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.util.*;
import slavsquatsuperstar.demos.geometrydash.ui.UICanvas;

/**
 * The Geometry Dash level editor scene.
 *
 * @author SlavSquatSuperstar
 */
public class GDEditorScene extends Scene {

    final static int TILE_SIZE = 42;

    public GDEditorScene(String name) {
        super(name, Preferences.getScreenWidth(), Preferences.getScreenHeight(), TILE_SIZE);
        setGravity(new Vec2());
    }

    @Override
    protected void init() {
        addObject(new GameObject("Ground", new Vec2(0, getHeight() * -0.5f)) {
            @Override
            protected void init() {
                setZIndex(ZIndex.BLOCK);
                addComponent(new Rigidbody(0f).setFixedRotation(true));
                addComponent(new BoxCollider(new Vec2(getWidth() + 2f, 2f)));
                addComponent(new ShapeSprite(Colors.BLACK, true));
            }
        });

        // TODO still getting stuck on corners
        addObject(new GDPlayer("Player", new Vec2(0, 0)));

        addObject(Grid.createGridObject());

        addObject(new UICanvas("Canvas", new Transform(new Vec2(-3f, -5f))));
    }

}
