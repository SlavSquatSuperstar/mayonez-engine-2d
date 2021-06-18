package com.slavsquatsuperstar.game;

import com.slavsquatsuperstar.mayonez.*;

import java.awt.*;

public class LevelScene extends Scene {

    public LevelScene(String name) {
        super(name, (int) (Preferences.SCREEN_WIDTH * 1.5), (int) (Preferences.SCREEN_HEIGHT * 1.0));
        background = Color.WHITE;
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

        GameObject player = new Player("Player", new Vector2(100, 100), ground);
        addObject(player);
        camera().setSubject(player);

        addObject(new GameObject("Mario", new Vector2(0, 28)) {
            @Override
            protected void init() {
                addComponent(Assets.getSprite("mario.png"));
                transform.scale = transform.scale.mul(2);
            }
        });
        addObject(new GameObject("Goomba", new Vector2(width - 32, height - 52)) {
            @Override
            protected void init() {
                addComponent(Assets.getSprite("goomba.png"));
            }
        });
    }

}
