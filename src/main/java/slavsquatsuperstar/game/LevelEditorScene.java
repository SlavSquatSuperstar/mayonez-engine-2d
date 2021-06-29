package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.components.Sprite;

import java.awt.*;

public class LevelEditorScene extends Scene {

    public LevelEditorScene(String name) {
        super(name, (int) (Preferences.SCREEN_WIDTH * 1.0), (int) (Preferences.SCREEN_HEIGHT * 1.0));
        setGravity(new Vector2());
    }

    @Override
    protected void init() {
        GameObject ground = new GameObject("Ground", new Vector2(0, Preferences.GROUND_HEIGHT)) {
            @Override
            public void update(float dt) {
                super.update(dt);
                setX(scene.camera().getX());
            }

            @Override
            public void render(Graphics2D g2) {
                super.render(g2);
                g2.setColor(Color.BLACK);
                g2.fillRect((int) getX() - 20, (int) getY(), Preferences.SCREEN_WIDTH + 40, height);
            }
        };
        addObject(ground);

        GameObject player = new Player("Player", new Vector2(100, 100), ground);
        addObject(player);

        addObject(new GameObject("Mario", new Vector2(0, 28)) {
            @Override
            protected void init() {
                addComponent(new Sprite("mario.png"));
                transform.scale = transform.scale.mul(2);
            }
        });

        addObject(new GameObject("Goomba", new Vector2(width - 32, Preferences.GROUND_HEIGHT - 32)) {
            @Override
            protected void init() {
                addComponent(new Sprite("goomba.png"));
            }
        });

        addObject(new Grid(new Vector2(), ground));
    }

}
