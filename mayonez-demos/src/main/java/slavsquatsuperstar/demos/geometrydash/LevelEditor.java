package slavsquatsuperstar.demos.geometrydash;

import slavsquatsuperstar.demos.geometrydash.components.Grid;
import slavsquatsuperstar.demos.geometrydash.ui.UICanvas;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.graphics.JSpriteSheet;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.physics.colliders.Collider;

import java.awt.*;

public class LevelEditor extends Scene {

    public LevelEditor() {
        super("Level Editor", Preferences.getScreenWidth(), Preferences.getScreenHeight(), 42);
        setGravity(new Vec2());
    }

    @Override
    protected void init() {
        JSpriteSheet blocks = new JSpriteSheet("assets/textures/blocks.png", 42, 42, 12, 2);

        addObject(new GameObject("Ground", new Vec2(getWidth() * 0.5f, 0)) {
            @Override
            protected void init() {
                addComponent(new Rigidbody(0f).setFixedRotation(true));
                addComponent(new BoxCollider(new Vec2(getWidth() + 2f, 2f)));
            }

            @Override
            public void onUserRender(Graphics2D g2) {
                getComponent(Collider.class).setDebugDraw(Colors.BLACK, true);
            }
        });

        // TODO getting displaced too much when colliding with multiple boxes
        // TODO check recently moved colliders
        addObject(new Player("Player", new Vec2(5, 5)));

        addObject(new GameObject("Grid") {
            @Override
            protected void init() {
                addComponent(new Grid(new Vec2(getCellSize())));
            }
        });
        addObject(new UICanvas("Canvas", new Transform(new Vec2(9.25f, 3.5f)), blocks));

//        addObject(new GameObject("Button", new Transform(new Vec2(10, 5))) {
//            @Override
//            protected void init() {
//                addComponent(new UIButton(blocks.getTexture(0)));
//            }
//        });
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.setScene(new LevelEditor());
        Mayonez.start();
    }

}
