package slavsquatsuperstar.demos;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.graphics.JCamera;
import slavsquatsuperstar.mayonez.graphics.JSpriteSheet;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.scripts.MoveMode;

import java.awt.event.KeyEvent;

/**
 * For testing world to screen coordinates
 */
public class JRendererTest extends Scene {

    private final JSpriteSheet sprites;

    public JRendererTest() {
        super("Renderer Test Scene", Preferences.getScreenWidth(), Preferences.getScreenHeight(), 32);
        sprites = new JSpriteSheet("assets/textures/spritesheet.png", 16, 16, 26, 0);
        setGravity(new Vec2());
    }

    @Override
    protected void init() {
        addObject(new GameObject("Mario", new Transform(new Vec2(getWidth() / 2f, getHeight() / 2f), new Vec2(2, 2))) {
            @Override
            protected void init() {
                JCamera cam = (JCamera) getScene().getCamera();
                cam.setSubject(this);
                cam.enableKeepInScene(false);
                addComponent(sprites.getSprite(0));
                addComponent(new BoxCollider(new Vec2(0.8f, 1)));
                addComponent(new Rigidbody(1f).setFixedRotation(true));
                addComponent(new KeyMovement(MoveMode.POSITION, 5));
                addComponent(new Script() {
                    @Override
                    public void update(float dt) {
                        if (KeyInput.keyDown(KeyEvent.VK_Q))
                            transform.rotate(1);
                        if (KeyInput.keyDown(KeyEvent.VK_E))
                            transform.rotate(-1);
                    }
                });
            }
        });

        for (int i = 0; i < 8; i++) addObject(createObject("Goomba", 14));
    }

    private GameObject createObject(String name, Vec2 position, int spriteIndex) {
        return new GameObject(name, position) {
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
//                if (collision.getOther().getParent().name.equals("Mario")) destroy();
//                else collision.ignore();
            }
        };
    }

    private GameObject createObject(String name, int spriteIndex) {
        return createObject(name, new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight())), spriteIndex);
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.setScene(new JRendererTest());
        Mayonez.start();
    }
}
