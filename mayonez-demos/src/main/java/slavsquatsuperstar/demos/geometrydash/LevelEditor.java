package slavsquatsuperstar.demos.geometrydash;

import slavsquatsuperstar.demos.geometrydash.components.Grid;
import slavsquatsuperstar.demos.geometrydash.ui.UICanvas;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.graphics.sprites.JSpriteSheet;
import slavsquatsuperstar.mayonez.graphics.sprites.SpriteSheet;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.physics.colliders.Collider;
import slavsquatsuperstar.util.Colors;

import java.awt.*;

public class LevelEditor extends Scene {

    public LevelEditor() {
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
            }

            @Override
            public void onUserRender(Graphics2D g2) {
                getComponent(Collider.class).setDebugDraw(Colors.BLACK, true);
            }
        });

        // TODO still getting stuck on corners
        addObject(new Player("Player", new Vec2(0, 0)));

        addObject(new GameObject("Grid") {
            @Override
            protected void init() {
                addComponent(new Grid(new Vec2(getScale())));
            }
        });

        SpriteSheet blocks = SpriteSheet.create("assets/textures/blocks.png", 42, 42, 12, 2);
        addObject(new UICanvas("Canvas", new Transform(new Vec2(-5f, -5f)), (JSpriteSheet) blocks));

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
