package slavsquatsuperstar.demos;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.graphics.sprite.Sprite;
import slavsquatsuperstar.mayonez.graphics.sprite.SpriteSheet;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.math.MathUtils;
import slavsquatsuperstar.mayonez.math.Vec2;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.physics.shapes.Circle;
import slavsquatsuperstar.mayonez.physics.shapes.Ellipse;
import slavsquatsuperstar.mayonez.physics.shapes.Rectangle;
import slavsquatsuperstar.mayonez.physics.shapes.Triangle;
import slavsquatsuperstar.mayonez.scripts.*;
import slavsquatsuperstar.mayonez.util.Color;
import slavsquatsuperstar.mayonez.util.Colors;
import slavsquatsuperstar.mayonez.util.DebugDraw;

/**
 * For testing renderer, camera, and world to screen coordinates.
 *
 * @author SlavSquatSupertar
 */
public class RendererTest extends Scene {

    private final SpriteSheet sprites;

    public RendererTest() {
        super("Renderer Test Scene", Preferences.getScreenWidth() * 2, Preferences.getScreenHeight() * 2, 32);
        sprites = SpriteSheet.create("assets/textures/spritesheet.png", 16, 16, 26, 0);
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
                Sprite.create("assets/textures/blend_red.png")));

        addObject(createSquare("Green Square", new Vec2(halfWidth, halfHeight),
                Sprite.create("assets/textures/blend_red.png")));

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

    private GameObject createSquare(String name, Vec2 position, Sprite sprite) {
        return new GameObject(name, new Transform(position, 0f, new Vec2(8f)), 2) {
            @Override
            protected void init() {
                addComponent(sprite);
            }
        };
    }

    public static void main(String[] args) {
        String arg0 = (args.length > 0) ? args[0] : "";
        Mayonez.setUseGL(Boolean.valueOf(arg0)); // Automatically choose AWT/GL
        Mayonez.setScene(new RendererTest());
        Mayonez.start();
    }

}
