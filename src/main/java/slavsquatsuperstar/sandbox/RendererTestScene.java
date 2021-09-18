package slavsquatsuperstar.sandbox;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.renderer.IMGUI;
import slavsquatsuperstar.mayonez.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.scripts.MoveMode;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * For testing world to screen coordinates
 */
public class RendererTestScene extends Scene {

    SpriteSheet sprites;

    public RendererTestScene() {
        super("Renderer Test Scene", Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT, 32);
        sprites = new SpriteSheet("assets/textures/spritesheet.png", 16, 16, 26, 0);
    }

    public static void main(String[] args) {
        Game game = Game.instance();
        game.start();
        Game.loadScene(new RendererTestScene());
    }

    @Override
    protected void init() {
        addObject(new GameObject("Mario", new Transform(new Vec2(getWidth() / 2f, getHeight() / 2f), new Vec2(2, 2))) {
            @Override
            protected void init() {
                getScene().camera().setSubject(this);
                getScene().camera().enableKeepInScene(false);
                addComponent(sprites.getSprite(0));
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
                Vec2 circleMin = new Vec2(Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT).div(2f).sub(new Vec2(50, 50));
                IMGUI.drawCircle(circleMin, 50, Colors.LIGHT_BLUE);
            }
        });

        for (int i = 0; i < 5; i++) {
            addObject(createObject("Goomba", 14));
        }
    }

    private GameObject createObject(String name, int spriteIndex) {
        return new GameObject(name, new Vec2(MathUtils.random(0, 15), MathUtils.random(0, 10))) {
            @Override
            protected void init() {
                addComponent(sprites.getSprite(spriteIndex));
            }
        };
    }
}
