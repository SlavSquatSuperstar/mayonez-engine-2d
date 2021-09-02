package slavsquatsuperstar.sandbox;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;
import slavsquatsuperstar.mayonez.renderer.DebugDraw;
import slavsquatsuperstar.mayonez.renderer.Grid;

import java.awt.*;

public class LevelEditorScene extends Scene {

    public LevelEditorScene(String name) {
        super(name, Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT, 42);
        setGravity(new Vec2());
    }

    public static void main(String[] args) {
        Game game = Game.instance();
        game.start();
        Game.loadScene(new LevelEditorScene("Level Editor"));
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
            public void render(Graphics2D g2) {
                super.render(g2);
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

}