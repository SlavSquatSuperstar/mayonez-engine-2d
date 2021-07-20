package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.components.Sprite;
import slavsquatsuperstar.mayonez.components.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.components.scripts.MoveMode;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * For testing world to screen coordinates
 */
public class RendererTestScene extends Scene {

    public RendererTestScene(String name) {
        super(name, Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT);
    }

    @Override
    protected void init() {
        addObject(new GameObject("Mario 1", new Vector2(0.5f, 1.5f)) {
            @Override
            protected void init() {
                getScene().camera().setSubject(this);
                getScene().camera().setKeepInScene(false);
                transform.stretch(new Vector2(2, 2));
                addComponent(new Sprite("mario.png"));
                addComponent(new KeyMovement(MoveMode.POSITION, 0.5f));
                addComponent(new Script() {
                    @Override
                    public void update(float dt) {
                        if (Game.keyboard().keyDown(KeyEvent.VK_Q))
                            transform.rotate(-2);
                        if (Game.keyboard().keyDown(KeyEvent.VK_E))
                            transform.rotate(2);
                    }
                });
            }

            @Override
            public void update(float dt) {
                super.update(dt);
                Logger.log("Object, World: %s", transform.position);
                Logger.log("Camera, World: %s", getScene().camera().getOffset());

            }

            @Override
            public void render(Graphics2D g2) {
                super.render(g2);
                DebugDraw.drawPoint(getScene().camera().getOffset(), Colors.GREEN);
                DebugDraw.drawLine(new Vector2(0, Preferences.SCREEN_HEIGHT / 2f), new Vector2(Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT / 2f), Colors.BLACK);
                DebugDraw.drawLine(new Vector2(Preferences.SCREEN_WIDTH / 2f, 0), new Vector2(Preferences.SCREEN_WIDTH / 2f, Preferences.SCREEN_HEIGHT), Colors.BLACK);
                DebugDraw.drawPoint(new Vector2(540, 360), Colors.RED);
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
