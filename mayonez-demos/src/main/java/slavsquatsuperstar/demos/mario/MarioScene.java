package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.camera.*;

/**
 * For testing the renderer, camera, and coordinate conversions.
 *
 * @author SlavSquatSupertar
 */
public class MarioScene extends Scene {

    // Layers
    static final int CHARACTER_LAYER = 0;
    static final int GROUND_LAYER = 1;

    // Assets
    static final SpriteSheet SPRITES = Sprites.createSpriteSheet(
            "assets/mario/textures/spritesheet.png",
            16, 16, 26, 0);
    private static final Texture BACKGROUND_TEXTURE =
            Textures.getTexture("assets/mario/textures/background.png");

    // Size = 1920 x 1024 px = 60 x 32 units
    private static final int BACKGROUND_WIDTH = 1920;
    private static final int BACKGROUND_HEIGHT = 1024;
    private static final int SCENE_SCALE = 32;
    private static final float SCENE_GRAVITY = 70f;
    static final Vec2 SCENE_HALF_SIZE = new Vec2(BACKGROUND_WIDTH, BACKGROUND_HEIGHT)
            .div(SCENE_SCALE * 2f);

    public MarioScene(String name) {
        super(name, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, SCENE_SCALE);
    }

    @Override
    protected void init() {
        setBackground(Colors.LIGHT_GRAY);
        setBackground(BACKGROUND_TEXTURE);

        setGravity(new Vec2(0, -SCENE_GRAVITY));

        getCamera().setCameraScale(SCENE_SCALE);
        getCamera().zoom(0.8f);

        var charLayer = getLayer(CHARACTER_LAYER);
        charLayer.setName("Characters");

        var groundLayer = getLayer(GROUND_LAYER);
        groundLayer.setName("Ground");

        getCamera().addCameraScript(new CameraKeepInScene());
        addObject(new Mario(new Vec2(-23f, -11f)));

        // Add Enemies
        var halfWidth = SCENE_HALF_SIZE.x - 12;
        var halfHeight = SCENE_HALF_SIZE.y;
        for (var i = 0; i < 16; i++) {
            var randPos = Random.randomVector(-halfWidth, halfWidth,
                    -halfHeight + 4, halfHeight - 20);
            addObject(Goomba.createRandomGoomba(i % 4, randPos));
        }
        addObstaclesToScene();
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
                setLayer(getScene().getLayer(MarioScene.GROUND_LAYER));
                addComponent(new Rigidbody(0f));
                addComponent(new BoxCollider(size));
            }
        };
    }

}
