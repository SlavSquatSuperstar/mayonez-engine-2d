package com.slavsquatsuperstar.game;

import com.slavsquatsuperstar.mayonez.GameObject;
import com.slavsquatsuperstar.mayonez.Preferences;
import com.slavsquatsuperstar.mayonez.Scene;
import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.mayonez.components.Sprite;

import java.awt.*;

public class LevelEditorScene extends Scene {

    public LevelEditorScene(String name) {
        super(name, (int) (Preferences.SCREEN_WIDTH * 1.0), (int) (Preferences.SCREEN_HEIGHT * 1.0));
        background = Color.WHITE;
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
                g2.getTransform().getTranslateX();
                g2.fillRect((int) getX() - 20, (int) getY(), Preferences.SCREEN_WIDTH + 40, height);
            }
        };
        addObject(ground);

        addObject(new Grid(new Vector2(), ground));

        GameObject player = new Player("Player", new Vector2(100, 100), ground);
        addObject(player);

        addObject(new GameObject("Mario", new Vector2(0, 28)) {
            @Override
            protected void init() {
                addComponent(new Sprite("mario.png"));
                transform.scale = transform.scale.mul(2);
            }
        });
        addObject(new GameObject("Goomba", new Vector2(width - 32, height - 52)) {
            @Override
            protected void init() {
                addComponent(new Sprite("goomba.png"));
            }
        });
    }

}
