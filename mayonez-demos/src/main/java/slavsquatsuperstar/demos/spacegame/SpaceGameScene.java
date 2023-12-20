package slavsquatsuperstar.demos.spacegame;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.ui.*;
import mayonez.math.Random;
import mayonez.math.*;
import mayonez.math.shapes.*;
import slavsquatsuperstar.demos.spacegame.objects.BackgroundObject;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;
import slavsquatsuperstar.demos.spacegame.objects.SpaceObjectSpawner;
import slavsquatsuperstar.demos.spacegame.ui.HealthBar;

import java.util.*;

import static slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer.*;

/**
 * The main scene for the space game.
 *
 * @author SlavSquatSuperstar
 */
public class SpaceGameScene extends Scene {

    // Constants
    private final static int SCENE_SIZE = 4;
    private final static int NUM_STARS = 2000;

    // Objects
    private final List<BackgroundObject> backgroundObjects;

    public SpaceGameScene(String name) {
        super(name, Preferences.getScreenWidth() * SCENE_SIZE,
                Preferences.getScreenHeight() * SCENE_SIZE, 32f);
        setBackground(new Color(14, 14, 14));
        backgroundObjects = new ArrayList<>();

        SpaceGameConfig.readConfig();
    }

    @Override
    protected void init() {
        setGravity(new Vec2());
        backgroundObjects.clear();

        setLayers();
        addObject(new SpaceObjectSpawner());
        addSolarSystem();
        addBackgroundStars();

        // Health Bar
        addObject(new GameObject("Player UI") {
            @Override
            protected void init() {
                // TODO recharge health bar when respawning
                var hpPosition = new Vec2(102, 773);
                var hpSize = new Vec2(180, 30);
                addComponent(new HealthBar(hpPosition, hpSize));
            }
        });

        // Weapon Select
        addObject(new GameObject("Weapon Select UI") {
            @Override
            protected void init() {
                // TODO show currently active
                // TODO show fire cooldown
                var wsPosition = new Vec2(30, 30);
                var wsSize = new Vec2(30, 30);

                setZIndex(SpaceGameZIndex.UI);
                var box1 = new UIBox(wsPosition, wsSize, Colors.RED);
                addComponent(box1);

                var box2 = new UIBox(wsPosition.add(new Vec2(50, 0)), wsSize, Colors.SKY_BLUE);
                addComponent(box2);
            }
        });
    }

//    @Override
//    protected void onUserUpdate(float dt) {
//        // camera controls
//        getCamera().rotate(KeyInput.getAxis("arrows horizontal"));
//        getCamera().zoom(1 + 0.01f * KeyInput.getAxis("arrows vertical"));
//    }

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
        addBackgroundObject(new Circle(new Vec2(-10, -8), 12), Colors.DARK_BLUE, SpaceGameZIndex.BACKGROUND); // Earth
        addBackgroundObject(new Circle(new Vec2(12.5f, 7.5f), 2.5f), Colors.DARK_GRAY, SpaceGameZIndex.BACKGROUND); // Moon
        addBackgroundObject(new Circle(new Vec2(-20, 16), 2), Colors.YELLOW, SpaceGameZIndex.BACKGROUND); // Sun
    }

    private void addBackgroundStars() {
        // TODO parallax
        for (var i = 0; i < NUM_STARS; i++) {
            var starPos = this.getRandomPosition().mul(2);

            float starSize;
            boolean isDwarfStar = Random.randomPercent(2f / 3f);
            if (isDwarfStar) starSize = Random.randomGaussian(2.5f, 0.5f); // 1-4
            else starSize = Random.randomGaussian(7, 1); // 4-10
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
