package slavsquatsuperstar.sandbox;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;
import slavsquatsuperstar.mayonez.renderer.DebugDraw;
import slavsquatsuperstar.mayonez.renderer.Grid;

import java.awt.*;

public class LevelEditor extends Scene {

    public LevelEditor(String name) {
        super(name, Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT, 42);
        setGravity(new Vec2());
    }

    @Override
    protected void init() {
        addObject(new GameObject("Ground", new Vec2(getWidth() * 0.5f, getHeight())) {
            @Override
            protected void init() {
                addComponent(new Rigidbody2D(0f));
                addComponent(new AlignedBoxCollider2D(new Vec2(getWidth() + 2f, 2f)));
                System.out.println(transform.position);
            }

            @Override
            public void onUserRender(Graphics2D g2) {
                DebugDraw.fillShape(getComponent(Collider2D.class), Colors.BLACK);
            }
        });

        addObject(new Player("Player", new Vec2(5, 5)));

        addObject(new GameObject("Grid", new Vec2()) {
            @Override
            protected void init() {
                addComponent(new Grid(getCellSize(), getCellSize()));
            }
        });
    }

    public static void main(String[] args) {
        Game.loadScene(new LevelEditor("Level Editor"));
        Game.start();
    }

}
