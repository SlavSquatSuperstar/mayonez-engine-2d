package slavsquatsuperstar.demos.mario;

import mayonez.Scene;
import mayonez.graphics.Colors;
import mayonez.graphics.sprites.SpriteSheet;
import mayonez.io.Assets;
import mayonez.io.image.Texture;
import mayonez.math.Vec2;

/**
 * For testing the renderer, camera, and coordinate conversions.
 *
 * @author SlavSquatSupertar
 */
public class MarioScene extends Scene {

    private final SpriteSheet sprites;
    private final Texture background;

    public MarioScene(String name) {
        super(name, 1920, 1080, 32);
        sprites = SpriteSheet.create("assets/textures/mario/spritesheet.png", 16, 16, 26, 0);
        background = Assets.getTexture("assets/textures/mario/background.png"); // 1920x1080 (16:9)
    }

    @Override
    public void init() {
        setBackground(Colors.LIGHT_GRAY);
        setBackground(background);
        setGravity(new Vec2());
        getCamera().zoom(0.8f);

        addObject(new Mario(new Vec2(-16f, -12f), sprites.getSprite(0)));

        // Add enemies
        for (var i = 0; i < 16; i++) {
            switch (i % 4) {
                case 0 -> addObject(new Goomba("Goomba", sprites.getSprite(14), getRandomPosition()));
                case 1 -> addObject(new Goomba("Cool Goomba", sprites.getSprite(17), getRandomPosition()));
                case 2 -> addObject(new Goomba("Vintage Goomba", sprites.getSprite(20), getRandomPosition()));
                case 3 -> addObject(new Goomba("Vintage Cool Goomba", sprites.getSprite(23), getRandomPosition()));
            }
        }

        // Add squares
//        var halfWidth = getWidth() * 0.5f - 2f;
//        var halfHeight = getHeight() * 0.5f - 2f;

//        addObject(new ColoredSquare("Red Square", new Vec2(-halfWidth, halfHeight),
//                new Color(169, 0, 0, 153), 0));
//        addObject(new ColoredSquare("Green Square", new Vec2(halfWidth, halfHeight),
//                new Color(67, 169, 0, 153), 2));
//        addObject(new ColoredSquare("Blue Square", new Vec2(-halfWidth, -halfHeight),
//                new Color(0, 67, 169, 153), 0));
//        addObject(new ColoredSquare("Gray Square", new Vec2(halfWidth, -halfHeight),
//                new Color(31, 31, 31, 153), 2));
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

    @Override
    protected void onUserRender() {
//        DebugDraw.drawLine(new Vec2(-4, -4), new Vec2(-4, 4), Colors.RED);
//        DebugDraw.drawLine(new Vec2(-4, 4), new Vec2(4, 4), Colors.GREEN);
//        DebugDraw.drawLine(new Vec2(4, 4), new Vec2(4, -4), Colors.BLUE);
//        DebugDraw.drawLine(new Vec2(4, -4), new Vec2(-4, -4), Colors.YELLOW);
//
//        var sceneBounds = new Rectangle(new Vec2(0, 0), new Vec2(this.getWidth(), this.getHeight()));
//        DebugDraw.drawShape(sceneBounds.scale(new Vec2(1.0f), null), Colors.BLACK);
//        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.8f), null), Colors.BLACK);
//        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.6f), null), Colors.BLACK);
//        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.4f), null), Colors.BLACK);
//        DebugDraw.drawShape(sceneBounds.scale(new Vec2(0.2f), null), Colors.BLACK);
//
//        var tri = new Triangle(new Vec2(-2, -5), new Vec2(0, -1), new Vec2(2, -5));
//        DebugDraw.fillShape(tri, Colors.ORANGE);
//
//        var circle = new Circle(new Vec2(0, 5), 5);
//        DebugDraw.drawShape(circle, Colors.PURPLE);
//
//        var ellipse = new Ellipse(new Vec2(0, 5), new Vec2(9.5f, 8));
//        DebugDraw.fillShape(ellipse, Colors.BLUE);
    }
}
