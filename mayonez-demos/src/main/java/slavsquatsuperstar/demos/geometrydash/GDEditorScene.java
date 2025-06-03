package slavsquatsuperstar.demos.geometrydash;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.camera.*;
import slavsquatsuperstar.demos.DemoScene;
import slavsquatsuperstar.demos.geometrydash.ui.UICanvas;

/**
 * The Geometry Dash level editor scene.
 *
 * @author SlavSquatSuperstar
 */
public class GDEditorScene extends DemoScene {

    final static int TILE_SIZE = 42;
    final static Vec2 SCENE_SIZE = new Vec2(Preferences.getScreenWidth(),
            Preferences.getScreenHeight()).div(TILE_SIZE);

    public GDEditorScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        setGravity(new Vec2());
        getCamera().setCameraScale(TILE_SIZE);
        var size = new Vec2(Preferences.getScreenWidth(), Preferences.getScreenHeight()).div(TILE_SIZE);

        addObject(new GameObject("Camera Controls") {
            @Override
            protected void init() {
                addComponent(new CameraDragAndDrop("right mouse"));
            }
        });

        addObject(new GameObject("Ground", new Vec2(0, size.y * -0.5f)) {
            @Override
            protected void init() {
                setZIndex(ZIndex.BLOCK);
                addComponent(new Rigidbody(0f).setFixedRotation(true));
                addComponent(new BoxCollider(new Vec2(size.x + 2f, 2f)));
                addComponent(new ShapeSprite(Colors.BLACK, true));
            }
        });

        // TODO still getting stuck on corners
        addObject(new GDPlayer("Player", new Vec2(0, 0)));

        addObject(new GameObject("Grid") {
            @Override
            protected void init() {
                setZIndex(ZIndex.GRID);
                addComponent(new DrawGrid());
            }
        });

        addObject(new UICanvas("Block Palette", new Transform(new Vec2(-3f, -5f))));
    }

}
