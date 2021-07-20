package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.components.Sprite;
import slavsquatsuperstar.mayonez.components.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.components.scripts.MoveMode;

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
        addObject(new GameObject("Mario 1", new Vector2(1, 1)) {
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
                            parent.transform.rotate(-2);
                        if (Game.keyboard().keyDown(KeyEvent.VK_E))
                            parent.transform.rotate(2);
                    }
                });
            }

            @Override
            public void update(float dt) {
                super.update(dt);
                Logger.log("Sprite, World: %s", transform.position);
                Logger.log("Camera, World: (%.4f, %.4f)", getScene().camera().getOffsetX(), getScene().camera().getOffsetY());
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
