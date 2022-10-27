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
import slavsquatsuperstar.mayonez.scripts.*;

public class GLRendererTest extends Scene {

    private GLSpriteSheet sprites;

    public GLRendererTest() {
        super("LWJGL Test Scene", Preferences.getScreenWidth() * 2, Preferences.getScreenHeight() * 2, 32);
        camera = new GLCamera(Mayonez.getScreenSize(), this.getScale());
        setGravity(new Vec2());
    }

    @Override
    public void init() {
        // Load resources
        sprites = new GLSpriteSheet("assets/textures/spritesheet.png", 16, 16, 26, 0);

        addObject(new GameObject("Mario", new Transform(
                new Vec2(0, 0), new Vec2(2)
        )) {
            @Override
            protected void init() {
                getScene().getCamera().setSubject(this).setKeepInScene(true);
                addComponent(sprites.getSprite(0));
                addComponent(new BoxCollider(new Vec2(0.8f, 1)));
                addComponent(new Rigidbody(1f).setFixedRotation(true));
                addComponent(new KeyMovement(MoveMode.POSITION, 20));
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

        float halfWidth = getWidth() / 2f;
        float halfHeight = getHeight() / 2f;

        addObject(createSquare("Red Square", new Vec2(-halfWidth, halfHeight),
                new GLSprite(Assets.getGLTexture("assets/textures/blend_red.png"))));

        addObject(createSquare("Green Square", new Vec2(halfWidth, halfHeight),
                new GLSprite(Assets.getGLTexture("assets/textures/blend_green.png"))));

        addObject(createSquare("Blue Square", new Vec2(-halfWidth, -halfHeight),
                new GLSprite(new Vector4f(0, 67f / 255f, 169f / 255f, 0.6f))));

        addObject(createSquare("Gray Square", new Vec2(halfWidth, -halfHeight),
                new GLSprite(new Vector4f(31f / 255f, 31f / 255f, 31f / 255f, 0.6f))));

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
//        if (KeyInput.keyPressed("space"))
//            System.out.println("pressed");
//        else if (KeyInput.keyDown("space"))
//            System.out.println("held");
//
//        if (MouseInput.buttonPressed("left mouse"))
//            System.out.println("pressed");
//        else if (MouseInput.buttonDown("left mouse"))
//            System.out.println("held");
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

    private GameObject createSquare(String name, Vec2 position, GLSprite sprite) {
        return new GameObject(name, new Transform(position, new Vec2(8)), 2) {
            @Override
            protected void init() {
                addComponent(sprite);
            }
        };
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(true);
        Mayonez.setScene(new GLRendererTest());
        Mayonez.start();
    }

}
