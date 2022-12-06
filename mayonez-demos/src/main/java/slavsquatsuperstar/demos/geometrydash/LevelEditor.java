package slavsquatsuperstar.demos.geometrydash;

import mayonez.*;
import slavsquatsuperstar.demos.geometrydash.components.Grid;
import slavsquatsuperstar.demos.geometrydash.ui.UICanvas;
import mayonez.math.Vec2;
import mayonez.graphics.sprite.JSpriteSheet;
import mayonez.graphics.sprite.SpriteSheet;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BoxCollider;
import mayonez.physics.colliders.Collider;
import mayonez.graphics.Colors;

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

        SpriteSheet blocks = SpriteSheet.create("assets/textures/geometrydash/blocks.png", 42, 42, 12, 2);
        addObject(new UICanvas("Canvas", new Transform(new Vec2(-5f, -5f)), (JSpriteSheet) blocks));

    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.start(new LevelEditor());
    }

}
