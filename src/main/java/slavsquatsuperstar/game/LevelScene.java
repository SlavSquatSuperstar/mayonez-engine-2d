package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.Vector2;

import java.awt.*;

public class LevelScene extends Scene {

    public LevelScene(String name) {
        super(name, (int) (Preferences.SCREEN_WIDTH * 1.5), (int) (Preferences.SCREEN_HEIGHT * 1.0));
    }

    @Override
    protected void init() {
        // Give ground a collider
        GameObject ground = new GameObject("Ground", new Vector2(0, Preferences.GROUND_HEIGHT)) {
            @Override
            public void update(float dt) {
                super.update(dt);
                setX(getScene().camera().getOffsetX());
            }

            @Override
            public void render(Graphics2D g2) {
                super.render(g2);
                g2.setColor(Color.BLACK);
                g2.fillRect((int) getX() - 20, (int) getY(), Preferences.SCREEN_WIDTH + 40, height);
            }
        };
//        addObject(ground);

        GameObject player = new Player("Player", new Vector2(100, 100), ground);
        addObject(player);
        camera().setSubject(player);
    }

}
