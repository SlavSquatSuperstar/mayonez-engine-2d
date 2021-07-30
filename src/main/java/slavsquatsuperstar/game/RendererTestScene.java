package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.renderer.IMGUI;
import slavsquatsuperstar.mayonez.renderer.Sprite;
import slavsquatsuperstar.mayonez.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.scripts.MoveMode;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * For testing world to screen coordinates
 */
public class RendererTestScene extends Scene {

    public RendererTestScene(String name) {
        super(name, Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT, 32);
    }

    @Override
    protected void init() {
        addObject(new GameObject("Mario 1", new Vector2(0.5f, 1.5f)) {
            @Override
            protected void init() {
                getScene().camera().setSubject(this);
                getScene().camera().enableKeepInScene(false);
                transform.stretch(new Vector2(2, 2));
                addComponent(new Sprite("mario.png"));
                addComponent(new KeyMovement(MoveMode.POSITION, 0.5f));
                addComponent(new Script() {
                    @Override
                    public void update(float dt) {
                        if (KeyInput.keyDown(KeyEvent.VK_Q))
                            transform.rotate(-2);
                        if (KeyInput.keyDown(KeyEvent.VK_E))
                            transform.rotate(2);
                    }
                });
            }

            @Override
            public void render(Graphics2D g2) {
                super.render(g2);
                IMGUI.fillCircle(new Vector2(Preferences.SCREEN_WIDTH / 2f - 50, Preferences.SCREEN_HEIGHT / 2f + 32), 50, Colors.BLUE);
                IMGUI.drawCircle(new Vector2(Preferences.SCREEN_WIDTH / 2f - 50, Preferences.SCREEN_HEIGHT / 2f + 32), 50, Colors.BLACK);
                IMGUI.drawPoint(new Vector2(Preferences.SCREEN_WIDTH / 2f, Preferences.SCREEN_HEIGHT / 2f + 82), Colors.LIGHT_BLUE);
            }
        });
        addObject(new GameObject("Mario 2", new Vector2(2, 1)) {
            @Override
            protected void init() {
                addComponent(new Sprite("mario.png"));
            }
        });
        addObject(new GameObject("Mario 3", new Vector2(1, 2)) {
            @Override
            protected void init() {
                addComponent(new Sprite("mario.png"));
            }
        });
    }
}
