package slavsquatsuperstar.demos;

import mayonez.*;
import mayonez.graphics.Color;
import mayonez.graphics.Colors;
import mayonez.graphics.sprite.Sprite;
import mayonez.graphics.sprite.SpriteSheet;
import mayonez.input.KeyInput;
import mayonez.input.MouseInput;
import mayonez.math.Random;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BoxCollider;
import mayonez.physics.shapes.Circle;
import mayonez.physics.shapes.Ellipse;
import mayonez.physics.shapes.Rectangle;
import mayonez.physics.shapes.Triangle;
import mayonez.scripts.*;
import mayonez.scripts.movement.DragAndDrop;
import mayonez.scripts.movement.KeyMovement;
import mayonez.scripts.movement.MouseScript;
import mayonez.scripts.movement.MoveMode;

/**
 * For testing renderer, camera, and world to screen coordinates.
 *
 * @author SlavSquatSupertar
 */
public class RendererTest extends Scene {

    private final SpriteSheet sprites;

    public RendererTest() {
        super("Renderer Test Scene", Preferences.getScreenWidth() * 2, Preferences.getScreenHeight() * 2, 32);
        sprites = SpriteSheet.create("assets/textures/mario/spritesheet.png", 16, 16, 26, 0);
        setGravity(new Vec2());
    }

    @Override
    public void init() {
//        Logger.log(Assets.scanFiles("mayonez-demos/src/main/resources/assets/textures"));
//        Logger.log(Assets.scanResources("assets"));

        addObject(new GameObject("Mario",
                Transform.scaleInstance(new Vec2(2))) {
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
                addComponent(new KeyMovement(MoveMode.POSITION, 15));
                addComponent(new Script() {
                    @Override
                    public void update(float dt) {
                        if (KeyInput.keyDown("q"))
                            transform.rotation += 3f;
                        if (KeyInput.keyDown("e"))
                            transform.rotation -= 3f;

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
                Sprite.create("assets/textures/mario/blend_red.png")));

        addObject(createSquare("Green Square", new Vec2(halfWidth, halfHeight),
                Sprite.create("assets/textures/mario/blend_green.png")));

        addObject(createSquare("Blue Square", new Vec2(-halfWidth, -halfHeight),
                Sprite.create(new Color(0, 67, 169, 153))));

        addObject(createSquare("Gray Square", new Vec2(halfWidth, -halfHeight),
                Sprite.create(new Color(31, 31, 31, 153))));

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
        if (KeyInput.keyPressed("r")) SceneManager.reloadScene(); // reload scene

        DebugDraw.drawLine(new Vec2(-4, -4), new Vec2(-4, 4), Colors.RED);
        DebugDraw.drawLine(new Vec2(-4, 4), new Vec2(4, 4), Colors.GREEN);
        DebugDraw.drawLine(new Vec2(4, 4), new Vec2(4, -4), Colors.BLUE);
        DebugDraw.drawLine(new Vec2(4, -4), new Vec2(-4, -4), Colors.YELLOW);

        Rectangle sceneBounds = new Rectangle(new Vec2(0, 0), new Vec2(this.getWidth(), this.getHeight()));
        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.8f), null), Colors.BLACK);
        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.6f), null), Colors.BLACK);
        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.4f), null), Colors.BLACK);
        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.2f), null), Colors.BLACK);

        Triangle tri = new Triangle(new Vec2(-2, -5), new Vec2(0, -1), new Vec2(2, -5));
        DebugDraw.fillShape(tri, Colors.ORANGE);

        Circle circle = new Circle(new Vec2(0, 5), 5);
        DebugDraw.drawShape(circle, Colors.PURPLE);

        Ellipse ellipse = new Ellipse(new Vec2(0, 5), new Vec2(9.5f, 8));
        DebugDraw.drawShape(ellipse, Colors.BLUE);

        getCamera().getTransform().rotate(-KeyInput.getAxis("arrows horizontal"));
        getCamera().getTransform().scale(new Vec2(1 + 0.1f * KeyInput.getAxis("arrows vertical")));

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
        Vec2 randomPos = Random.randomVector(-getWidth(), getWidth(), -getHeight(), getHeight()).mul(0.5f);
        return new GameObject(name, randomPos) {
            @Override
            protected void init() {
                addComponent(sprites.getSprite(spriteIndex));
                addComponent(new BoxCollider(new Vec2(0.8f, 1)));
                addComponent(new Rigidbody(1f).setFixedRotation(true));
                addComponent(new DragAndDrop("left mouse"));
                addComponent(new Script() {
                    @Override
                    public void onCollisionStay(GameObject other) {
                        if (other.name.equals("Mario")) setDestroyed();
                    }
                });
            }
        };
    }

    private GameObject createSquare(String name, Vec2 position, Sprite sprite) {
        return new GameObject(name, new Transform(position, 0f, new Vec2(8f)), 2) {
            @Override
            protected void init() {
                addComponent(sprite);
            }
        };
    }

    public static void main(String[] args) {
        String arg0 = (args.length > 0) ? args[0] : "false";
        Mayonez.setUseGL(Boolean.valueOf(arg0)); // Automatically choose AWT/GL from CL args
        Mayonez.start(new RendererTest());
    }

}
