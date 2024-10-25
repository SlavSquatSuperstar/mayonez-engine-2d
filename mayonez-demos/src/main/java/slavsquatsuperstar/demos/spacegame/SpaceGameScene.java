package slavsquatsuperstar.demos.spacegame;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.math.Random;
import mayonez.math.*;
import mayonez.math.shapes.*;
import slavsquatsuperstar.demos.spacegame.objects.BackgroundObject;
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
    private static final int NUM_STARS = 2000;
    private static final boolean CAMERA_DEBUG_MODE = false;

    // Objects
    private final List<BackgroundObject> backgroundObjects;

    public SpaceGameScene(String name) {
        super(name, Preferences.getScreenWidth() * SCENE_SIZE,
                Preferences.getScreenHeight() * SCENE_SIZE, 32f);
        setBackground(Color.grayscale(14));
        backgroundObjects = new ArrayList<>();

        SpaceGameConfig.readConfig();
    }

    @Override
    protected void init() {
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
        addBackgroundObject(new Circle(new Vec2(-10, -8), 16), Colors.DARK_BLUE, SpaceGameZIndex.BACKGROUND); // Earth
        addBackgroundObject(new Circle(new Vec2(12.5f, 7.5f), 2.5f), Colors.DARK_GRAY, SpaceGameZIndex.BACKGROUND); // Moon
        addBackgroundObject(new Circle(new Vec2(-20, 16), 1.5f), Colors.YELLOW, SpaceGameZIndex.BACKGROUND); // Sun
    }

    private void addBackgroundStars() {
        // TODO parallax
        for (var i = 0; i < NUM_STARS; i++) {
            var starPos = this.getRandomPosition().mul(2);

            float starSize;
            boolean isDwarfStar = Random.randomPercent(2f / 3f);
            if (isDwarfStar) starSize = Random.randomGaussianRange(1f, 4f); // 1-4
            else starSize = Random.randomGaussianRange(4f, 10f);
            if (starSize > 1) starSize = 1;

            var starDist = Random.randomFloat(20, 60);
            var starColor = Colors.randomColor(192, 255);
            addBackgroundObject(new Circle(starPos, starSize / starDist), starColor, SpaceGameZIndex.BACKGROUND_STAR);
        }
    }

    private void addBackgroundObject(Shape shape, Color color, int zIndex) {
        backgroundObjects.add(new BackgroundObject(shape, color, zIndex));
    }

}
