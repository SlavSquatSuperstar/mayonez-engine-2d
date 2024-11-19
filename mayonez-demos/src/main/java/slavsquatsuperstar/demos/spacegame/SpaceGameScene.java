package slavsquatsuperstar.demos.spacegame;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.math.Random;
import mayonez.math.*;
import mayonez.math.shapes.*;
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
public class SpaceGameScene extends Scene {

    // Constants
    private static final int SCENE_SIZE = 4;
    private static final int NUM_STARS = 3000;
    private static final boolean CAMERA_DEBUG_MODE = false;

    // Objects
    private final List<BackgroundObject> backgroundObjects;

    public SpaceGameScene(String name) {
        super(name, Preferences.getScreenWidth() * SCENE_SIZE,
                Preferences.getScreenHeight() * SCENE_SIZE, 32);
        SpaceGameConfig.readConfig();
        backgroundObjects = new ArrayList<>();
    }

    @Override
    protected void init() {
        setBackground(Color.grayscale(14));
        setGravity(new Vec2());
        getCamera().setKeepInScene(true);
        setLayers();

        // Background
        backgroundObjects.clear();
        addSolarSystem();
        addBackgroundStars();

        // Objects
        addObject(new SpaceObjectSpawner("Object Spawner"));

        // UI
        addObject(new PlayerUI("Player UI"));
    }

    @Override
    protected void onUserUpdate(float dt) {
        if (CAMERA_DEBUG_MODE) {
            getCamera().rotate(KeyInput.getAxis("arrows horizontal"));
            getCamera().zoom(1 + 0.01f * KeyInput.getAxis("arrows vertical"));
        }
    }

    @Override
    protected void onUserRender() {
        backgroundObjects.forEach(obj -> obj.debugDraw(getDebugDraw()));
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

    private void addSolarSystem() {
        addBackgroundObject(new Circle(new Vec2(-10, -8), 16),
                new Color(8, 104, 232)); // Earth
        addBackgroundObject(new Circle(new Vec2(12.5f, 7.5f), 2.5f),
                Color.grayscale(144)); // Moon
        addBackgroundObject(new Circle(new Vec2(-20, 16), 1.5f),
                new Color(255, 232, 80)); // Sun
    }

    private void addBackgroundStars() {
        // TODO parallax
        for (var i = 0; i < NUM_STARS; i++) {
            float starRadius;
            float invCDF = Random.randomFloat(0f, 100f);
            if (invCDF < 60f) {
                starRadius = Random.randomFloat(0.01f, 0.04f); // Dwarf
            } else if (invCDF < 90f) {
                starRadius = Random.randomFloat(0.04f, 0.07f); // Giant
            } else {
                starRadius = Random.randomFloat(0.07f, 0.08f); // Supergiant
            }
            var starTemp = BackgroundStarPrefabs.getRandomColorType().getRandomTemp();
            backgroundObjects.add(BackgroundStarPrefabs.createBackgroundStar(
                    getRandomPosition(), starRadius, starTemp
            ));
        }
    }


    private void addBackgroundObject(Shape shape, Color color) {
        backgroundObjects.add(new BackgroundObject(shape, color, SpaceGameZIndex.BACKGROUND));
    }

}
