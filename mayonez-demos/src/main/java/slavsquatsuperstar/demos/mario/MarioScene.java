package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.input.*;
import mayonez.io.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.util.*;

/**
 * For testing the renderer, camera, and coordinate conversions.
 *
 * @author SlavSquatSupertar
 */
public class MarioScene extends Scene {

    private static final int BACKGROUND_WIDTH = 1920;
    private static final int BACKGROUND_HEIGHT = 1024;
    private static final float SCENE_GRAVITY = 20;
    private final SpriteSheet sprites;
    private final Texture background;

    public MarioScene(String name) {
        super(name, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, 32);
        sprites = Sprites.createSpriteSheet(
                "assets/mario/textures/spritesheet.png",
                16, 16, 26, 0);
        background = Assets.getTexture("assets/mario/textures/background.png");
        // Size = 60 x 32
        // Resolution = 1920x1024 (15:8), cropped from 1920x1080 (16:9)
    }

    @Override
    public void init() {
        setBackground(Colors.LIGHT_GRAY);
        setBackground(background);
        setGravity(new Vec2(0, -SCENE_GRAVITY));
        getCamera().zoom(0.8f);

        addObject(new Mario(new Vec2(-21f, -11f), sprites.getSprite(0)));

        addEnemiesToScene();
        addObstaclesToScene();
    }

    @Override
    protected void onUserUpdate(float dt) {
        if (MouseInput.isPressed()) {
            Logger.log(MouseInput.getPosition());
        }
    }

    private void addEnemiesToScene() {
        for (var i = 0; i < 16; i++) {
            switch (i % 4) {
                case 0 -> addObject(new Goomba("Goomba", sprites.getSprite(14), getRandomPosition()));
                case 1 -> addObject(new Goomba("Cool Goomba", sprites.getSprite(17), getRandomPosition()));
                case 2 -> addObject(new Goomba("Vintage Goomba", sprites.getSprite(20), getRandomPosition()));
                case 3 -> addObject(new Goomba("Vintage Cool Goomba", sprites.getSprite(23), getRandomPosition()));
            }
        }
    }

    private void addObstaclesToScene() {
        // Randomly tunneling through ground if scale too small and speed too fast
        // Manually clamp position using KeepInScene
        addObject(createBoxObstacle("Ground", new Vec2(0, -14), new Vec2(60, 4)));

        addObject(createBoxObstacle("Mystery Box", new Vec2(-25, -5), new Vec2(2, 2)));
        addObject(createBoxObstacle("1x1 Platform", new Vec2(-13, -5), new Vec2(2, 2)));
        addObject(createBoxObstacle("2x1 Platform", new Vec2(10, -5), new Vec2(4, 2)));
        addObject(createBoxObstacle("3x1 Platform", new Vec2(-5, 3), new Vec2(6, 2)));
        addObject(createBoxObstacle("4x1 Platform", new Vec2(10, 3), new Vec2(8, 2)));

        addObject(createBoxObstacle("Stairs Step 1", new Vec2(19, -11), new Vec2(2, 2)));
        addObject(createBoxObstacle("Stairs Step 2", new Vec2(21, -10), new Vec2(2, 4)));
        addObject(createBoxObstacle("Stairs Step 3", new Vec2(23, -9), new Vec2(2, 6)));
        addObject(createBoxObstacle("Stairs Step 4", new Vec2(25, -8), new Vec2(2, 8)));
    }

    private GameObject createBoxObstacle(String name, Vec2 position, Vec2 size) {
        return new GameObject(name, position) {
            @Override
            protected void init() {
                addTag("Ground");
                addComponent(new Rigidbody(0f));
                addComponent(new BoxCollider(size));
//                addComponent(new ShapeSprite(Colors.WHITE, false));
            }
        };
    }

    @Override
    public Vec2 getRandomPosition() {
        var halfWidth = getWidth() * 0.5f;
        var halfHeight = getHeight() * 0.5f;
        return Random.randomVector(-halfWidth, halfWidth, -halfHeight + 2, halfHeight - 6);
    }

}
