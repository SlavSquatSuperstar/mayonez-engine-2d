package slavsquatsuperstar.demos;

import org.joml.Vector4f;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.graphics.GLCamera;
import slavsquatsuperstar.mayonez.graphics.sprites.GLSprite;
import slavsquatsuperstar.mayonez.graphics.sprites.GLSpriteSheet;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.io.Assets;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.scripts.KeepInScene;
import slavsquatsuperstar.mayonez.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.scripts.MoveMode;

public class GLRendererTest extends Scene {

    private GLCamera camera;
    private GLSpriteSheet sprites;
//    private List<GameObject> enemies;

    public GLRendererTest() {
        super("LWJGL Test Scene", 1080, 720, 32);
        camera = new GLCamera(new Vec2(0, 0), new Vec2(Preferences.getScreenWidth(), Preferences.getScreenHeight()), this.getScale());
//        enemies = new ArrayList<>();
        setGravity(new Vec2());
    }

    @Override
    public void init() {
        // Load resources
        sprites = new GLSpriteSheet("assets/textures/spritesheet.png", 16, 16, 26, 0);

        addObject(new GameObject("Mario", new Transform(
                new Vec2(20, 6), new Vec2(2, 2)
        )) {
            @Override
            protected void init() {
                Logger.log(getScene().getCamera());
                getScene().getCamera().setSubject(this);
                addComponent(sprites.getSprite(0));
                addComponent(new BoxCollider(new Vec2(0.8f, 1)));
                addComponent(new Rigidbody(1f).setFixedRotation(true));
                addComponent(new KeyMovement(MoveMode.POSITION, 20));
                addComponent(new KeepInScene(new Vec2(), getSize(), KeepInScene.Mode.STOP));
                addComponent(new Script() {
                    @Override
                    public void update(float dt) {
                        if (KeyInput.keyDown("q"))
                            transform.rotation += 2f;
                        if (KeyInput.keyDown("e"))
                            transform.rotation -= 2f;

                        if (KeyInput.keyDown("+"))
                            transform.scale(new Vec2(1.1f));
                        if (KeyInput.keyDown("-"))
                            transform.scale(new Vec2(0.9f));
                    }
                });
            }
        }.setZIndex(1));

        addObject(new GameObject("Red Square", new Transform(new Vec2(6, 6), new Vec2(4, 4)), -2) {
            @Override
            protected void init() {
                addComponent(new GLSprite(Assets.getGLTexture("assets/textures/blend_red.png")));
//                addComponent(new GLSprite(new Vector4f(169f / 255f, 0, 0, 0.596f)));
            }
        });

        addObject(new GameObject("Green Square", new Transform(new Vec2(9, 6), new Vec2(4, 4)), 2) {
            @Override
            protected void init() {
//                addComponent(new GLSprite(Assets.getGLTexture("assets/textures/blend_green.png")));
                addComponent(new GLSprite(new Vector4f(67f / 255f, 169f / 255f, 0, 0.596f)));
            }
        });

        for (int i = 0; i < 15; i++) addObject(createObject("Goomba", 14));
    }

    @Override
    protected void onUserUpdate(float dt) {
        if (KeyInput.keyPressed("space"))
            System.out.println("pressed");
        else if (KeyInput.keyDown("space"))
            System.out.println("held");

        if (MouseInput.buttonPressed("left mouse"))
            System.out.println("pressed");
        else if (MouseInput.buttonDown("left mouse"))
            System.out.println("held");
    }

    private GameObject createObject(String name, int spriteIndex) {
        return new GameObject(name, new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight()))) {
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

    @Override
    public GLCamera getCamera() {
        return camera;
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(true);
        Mayonez.setScene(new GLRendererTest());
        Mayonez.start();
    }

}
