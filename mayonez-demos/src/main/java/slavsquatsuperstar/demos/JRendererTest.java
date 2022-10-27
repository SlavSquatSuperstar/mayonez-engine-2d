package slavsquatsuperstar.demos;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.graphics.DebugDraw;
import slavsquatsuperstar.mayonez.graphics.sprites.JSpriteSheet;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.physics.shapes.Rectangle;
import slavsquatsuperstar.mayonez.scripts.*;
import slavsquatsuperstar.util.Colors;

import java.awt.event.KeyEvent;

/**
 * For testing world to screen coordinates and camera.
 */
public class JRendererTest extends Scene {

    private final JSpriteSheet sprites;

    public JRendererTest() {
        super("Renderer Test Scene", Preferences.getScreenWidth() * 2, Preferences.getScreenHeight() * 2, 32);
        sprites = new JSpriteSheet("assets/textures/spritesheet.png", 16, 16, 26, 0);
        setGravity(new Vec2());
    }

    @Override
    protected void init() {
        addObject(new GameObject("Mario", new Transform(new Vec2(0, 0), new Vec2(2, 2))) {
            @Override
            protected void init() {
                getScene().getCamera().setSubject(this).setKeepInScene(true);
                addComponent(sprites.getSprite(0));
                addComponent(new BoxCollider(new Vec2(0.8f, 1)));
                addComponent(new Rigidbody(1f).setFixedRotation(true));
                addComponent(new KeepInScene(KeepInScene.Mode.STOP));
                addComponent(new MouseScript() {
                    @Override
                    protected Vec2 getRawInput() {
                        return MouseInput.getPosition();
                    }

                    @Override
                    public void onMouseDown() {
                        System.out.println("It's a me, Mario!");
                    }
                });
                addComponent(new KeyMovement(MoveMode.POSITION, 7.5f));
                addComponent(new Script() {
                    @Override
                    public void update(float dt) {
                        if (KeyInput.keyDown(KeyEvent.VK_Q))
                            transform.rotate(1);
                        if (KeyInput.keyDown(KeyEvent.VK_E))
                            transform.rotate(-1);

                        if (KeyInput.keyDown("+"))
                            transform.scale(new Vec2(1.1f));
                        if (KeyInput.keyDown("-"))
                            transform.scale(new Vec2(0.9f));
                    }
                });
            }
        });

        for (int i = 0; i < 15; i++) {
            switch (i % 4) {
                case 0 -> addObject(createEnemy("Goomba", 14));
                case 1 -> addObject(createEnemy("Cool Goomba", 17));
                case 2 -> addObject(createEnemy("Vintage Goomba", 20));
                case 3 -> addObject(createEnemy("Vintage Cool Goomba", 23));
            }
        }
    }

    @Override
    protected void onUserUpdate(float dt) {
        Rectangle sceneBounds = new Rectangle(new Vec2(0, 0), new Vec2(this.getWidth(), this.getHeight()));
        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.8f), null), Colors.BLACK);
        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.6f), null), Colors.BLACK);
        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.4f), null), Colors.BLACK);
        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.2f), null), Colors.BLACK);
    }

    private GameObject createEnemy(String name, int spriteIndex) {
        float randomX = MathUtils.random(-getWidth() / 2f, getWidth() / 2f);
        float randomY = MathUtils.random(-getHeight() / 2f, getHeight() / 2f);
        return new GameObject(name, new Vec2(randomX, randomY)) {
            @Override
            protected void init() {
                addComponent(sprites.getSprite(spriteIndex));
                addComponent(new BoxCollider(new Vec2(0.8f, 1)));
                addComponent(new Rigidbody(1f).setFixedRotation(true));
                addComponent(new DragAndDrop("left mouse"));
            }

            @Override
            public void onCollision(GameObject other) {
                setDestroyed();
            }
        };
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.setScene(new JRendererTest());
        Mayonez.start();
    }
}
