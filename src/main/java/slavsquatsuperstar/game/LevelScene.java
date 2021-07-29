package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.Collider2D;
import slavsquatsuperstar.mayonez.renderer.DebugDraw;

import java.awt.*;

public class LevelScene extends Scene {

    public LevelScene(String name) {
        super(name, (int) (Preferences.SCREEN_WIDTH * 1.5), (int) (Preferences.SCREEN_HEIGHT * 1.0), 42);
        setGravity(new Vector2(0, 2));
    }

    @Override
    protected void init() {
        addObject(new GameObject("Ground", new Vector2(getWidth() * 0.5f, getHeight())) {
            @Override
            protected void init() {
                addComponent(new Rigidbody2D(0f));
                addComponent(new AlignedBoxCollider2D(new Vector2(getWidth() + 2f, 2f)).setBounce(0f));
                System.out.println(transform.position);
            }

            @Override
            public void render(Graphics2D g2) {
                super.render(g2);
                DebugDraw.fillShape(getComponent(Collider2D.class), Colors.BLACK);
            }
        });

        addObject(new Player("Player", new Vector2(5, 5)));
    }

}
