package slavsquatsuperstar.sandbox;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.renderer.JCamera;
import slavsquatsuperstar.mayonez.renderer.JSpriteSheet;
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
        super("Renderer Test Scene", Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT, 32);
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
                addComponent(new AlignedBoxCollider2D(new Vec2(0.8f, 1)));
                addComponent(new Rigidbody2D(1f));
                addComponent(new KeyMovement(MoveMode.POSITION, 20));
                addComponent(new Script() {
                    @Override
                    public void update(float dt) {
                        if (KeyInput.keyDown(KeyEvent.VK_Q))
                            transform.rotate(2);
                        if (KeyInput.keyDown(KeyEvent.VK_E))
                            transform.rotate(-2);
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
                addComponent(new AlignedBoxCollider2D(new Vec2(0.8f, 1)));
                addComponent(new Rigidbody2D(1f));
                addComponent(new DragAndDrop("left mouse", false));
            }

            @Override
            public void onCollision(CollisionManifold collision) {
                destroy();
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
