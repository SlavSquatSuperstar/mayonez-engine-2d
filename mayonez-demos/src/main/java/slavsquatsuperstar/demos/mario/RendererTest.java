package slavsquatsuperstar.demos.mario;

import mayonez.Mayonez;
import mayonez.Scene;
import mayonez.SceneManager;
import mayonez.graphics.Color;
import mayonez.graphics.Colors;
import mayonez.graphics.sprite.SpriteSheet;
import mayonez.input.KeyInput;
import mayonez.io.Assets;
import mayonez.io.image.Texture;
import mayonez.math.Vec2;

/**
 * For testing renderer, camera, and world to screen coordinates.
 *
 * @author SlavSquatSupertar
 */
public class RendererTest extends Scene {

    private final SpriteSheet sprites;
    private final Texture background;

    public RendererTest() {
        super("Renderer Test Scene", 1920, 1080, 40);
        sprites = SpriteSheet.create("assets/textures/mario/spritesheet.png", 16, 16, 26, 0);
        background = Assets.getTexture("assets/textures/mario/background.png"); // 1920x1080 (16:9)
    }

    @Override
    public void init() {
        setBackground(Colors.LIGHT_GRAY);
        setBackground(background);
        setGravity(new Vec2());

        addObject(new Mario(sprites.getSprite(0)));

        // Add squares
        float halfWidth = getWidth() * 0.5f - 2;
        float halfHeight = getHeight() * 0.5f - 2;

        addObject(new ColoredSquare("Red Square", new Vec2(-halfWidth, halfHeight),
                new Color(169, 0, 0, 153), 0));
        addObject(new ColoredSquare("Green Square", new Vec2(halfWidth, halfHeight),
                new Color(67, 169, 0, 153), 2));
        addObject(new ColoredSquare("Blue Square", new Vec2(-halfWidth, -halfHeight),
                new Color(0, 67, 169, 153), 0));
        addObject(new ColoredSquare("Gray Square", new Vec2(halfWidth, -halfHeight),
                new Color(31, 31, 31, 153), 2));

        // Add enemies
        for (int i = 0; i < 16; i++) {
            switch (i % 4) {
                case 0 -> addObject(new Goomba("Goomba", sprites.getSprite(14), getRandomPosition()));
                case 1 -> addObject(new Goomba("Cool Goomba", sprites.getSprite(17), getRandomPosition()));
                case 2 -> addObject(new Goomba("Vintage Goomba", sprites.getSprite(20), getRandomPosition()));
                case 3 -> addObject(new Goomba("Vintage Cool Goomba", sprites.getSprite(23), getRandomPosition()));
            }
        }
    }

    @Override
    protected void onUserUpdate(float dt) {
        if (KeyInput.keyPressed("r")) SceneManager.reloadScene();

//        DebugDraw.drawLine(new Vec2(-4, -4), new Vec2(-4, 4), Colors.RED);
//        DebugDraw.drawLine(new Vec2(-4, 4), new Vec2(4, 4), Colors.GREEN);
//        DebugDraw.drawLine(new Vec2(4, 4), new Vec2(4, -4), Colors.BLUE);
//        DebugDraw.drawLine(new Vec2(4, -4), new Vec2(-4, -4), Colors.YELLOW);
//
//        Rectangle sceneBounds = new Rectangle(new Vec2(0, 0), new Vec2(this.getWidth(), this.getHeight()));
//        DebugDraw.drawShape(sceneBounds.scale(new Vec2(1.0f), null), Colors.BLACK);
//        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.8f), null), Colors.BLACK);
//        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.6f), null), Colors.BLACK);
//        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.4f), null), Colors.BLACK);
//        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.2f), null), Colors.BLACK);
//
//        Triangle tri = new Triangle(new Vec2(-2, -5), new Vec2(0, -1), new Vec2(2, -5));
//        DebugDraw.fillShape(tri, Colors.ORANGE);
//
//        Circle circle = new Circle(new Vec2(0, 5), 5);
//        DebugDraw.drawShape(circle, Colors.PURPLE);
//
//        Ellipse ellipse = new Ellipse(new Vec2(0, 5), new Vec2(9.5f, 8));
//        DebugDraw.drawShape(ellipse, Colors.BLUE);

        // camera controls
        getCamera().rotate(-KeyInput.getAxis("arrows horizontal"));
        getCamera().zoom(1 + 0.01f * KeyInput.getAxis("arrows vertical"));

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

    public static void main(String[] args) {
        String arg0 = (args.length > 0) ? args[0] : "false";
        Mayonez.setUseGL(Boolean.valueOf(arg0)); // Automatically choose AWT/GL from CL args
        Mayonez.start(new RendererTest());
    }

}
