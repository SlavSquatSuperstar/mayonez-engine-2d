package slavsquatsuperstar.demos.spacegame;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.math.Random;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.scripts.camera.*;
import slavsquatsuperstar.demos.DemoScene;
import slavsquatsuperstar.demos.spacegame.objects.BackgroundObject;
import slavsquatsuperstar.demos.spacegame.objects.BackgroundStarPrefabs;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;
import slavsquatsuperstar.demos.spacegame.objects.spawners.SpaceObjectSpawner;
import slavsquatsuperstar.demos.spacegame.ui.PlayerUI;

import java.util.*;

import static slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer.*;

/**
 * The main scene for the space game.
 *
 * @author SlavSquatSuperstar
 */
public class SpaceGameScene extends DemoScene {

    // Constants
    private static final int NUM_STARS = 3000;
    private static final boolean CAMERA_DEBUG_MODE = false;
    private static final int SCENE_SCALE = 32;
    public static final Vec2 SCENE_HALF_SIZE
            = new Vec2(Preferences.getScreenWidth(), Preferences.getScreenHeight())
            .mul(4f / (SCENE_SCALE * 2f));

    // Fields
    private final List<BackgroundObject> backgroundObjects;

    public SpaceGameScene(String name) {
        super(name);
        SpaceGameConfig.readConfig();
        backgroundObjects = new ArrayList<>();
    }

    @Override
    protected void init() {
        setGravity(new Vec2());
        getCamera().setCameraScale(SCENE_SCALE);
        getCamera().setBackgroundColor(Color.grayscale(14));
        getCamera().addCameraScript(new CameraKeepInScene(SCENE_HALF_SIZE.mul(-1f), SCENE_HALF_SIZE));
        setLayers();

        // Background Objects
        backgroundObjects.clear();
        addBackgroundObject(new Circle(new Vec2(-10, -8), 16),
                new Color(8, 64, 192)); // Earth
        addBackgroundObject(new Circle(new Vec2(12.5f, 7.5f), 2.5f),
                Color.grayscale(144)); // Moon
        addBackgroundObject(new Circle(new Vec2(-20, 16), 1.5f),
                new Color(255, 232, 80)); // Sun

        // Background Stars
        for (var i = 0; i < NUM_STARS; i++) {
            backgroundObjects.add(BackgroundStarPrefabs.createRandomStar());
        }

        // Scene Objects
        addObject(new SpaceObjectSpawner("Object Spawner"));

        // UI
        addObject(new PlayerUI("Player UI"));
    }

    @Override
    protected void onUserUpdate(float dt) {
        super.onUserUpdate(dt);
        if (CAMERA_DEBUG_MODE) {
            getCamera().rotate(KeyInput.getAxis("arrows horizontal"));
            getCamera().zoom(1 + 0.01f * KeyInput.getAxis("arrows vertical"));
        }
    }

    @Override
    protected void onUserRender() {
        // TODO parallax
        var debugDraw = getDebugDraw();
        backgroundObjects.forEach(obj -> obj.debugDraw(debugDraw));
    }

    // Helper Methods

    private void setLayers() {
        var shipLayer = getLayer(SHIPS);
        shipLayer.setName("Ships");

        var asteroidLayer = getLayer(ASTEROIDS);
        asteroidLayer.setName("Asteroids");

        var projectileLayer = getLayer(PROJECTILES);
        projectileLayer.setName("Projectiles");
        projectileLayer.setLayerInteract(PROJECTILES, false);
    }

    private void addBackgroundObject(Shape shape, Color color) {
        backgroundObjects.add(new BackgroundObject(shape, color, SpaceGameZIndex.BACKGROUND));
    }

    /**
     * Returns a random vector within the scene's bounds.
     *
     * @return a random position
     */
    public static Vec2 getRandomPosition() {
        return Random.randomVector(SCENE_HALF_SIZE.mul(-1f), SCENE_HALF_SIZE);
    }

}
