package slavsquatsuperstar.demos.spacegame;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.Random;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.physics.colliders.*;
import slavsquatsuperstar.demos.spacegame.objects.*;
import slavsquatsuperstar.demos.spacegame.objects.asteroids.Asteroid;
import slavsquatsuperstar.demos.spacegame.objects.ships.*;

import java.util.*;

import static slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer.*;

public class SpaceGameScene extends Scene {

    // Constants
    private final static int SCENE_SIZE = 4;
    private final static int NUM_PLAYERS = 1;
    private final static int NUM_ENEMIES = 6;
    private final static int NUM_OBSTACLES = 5;
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
        getCamera().setKeepInScene(true);
        backgroundObjects.clear();

        setLayers();
        addSpawners();
        addSolarSystem();
        addBackgroundStars();

        // UI
        addObject(new GameObject("Health Bar") {
            @Override
            protected void init() {
                setZIndex(SpaceGameZIndex.UI);
                transform.setPosition(new Vec2(-16, 11.5f));
                addComponent(new BoxCollider(new Vec2(5, 1)).setEnabled(false));
                addComponent(new ShapeSprite(Colors.GREEN, true));
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

    private void addSpawners() {
        addObject(new GameObject("Object Spawner") {
            @Override
            protected void init() {
                SpawnManager playerSpawner, enemySpawner, obstacleSpawner;
                addComponent(playerSpawner = new SpawnManager(NUM_PLAYERS, 1.5f) {
                    @Override
                    public GameObject createSpawnedObject() {
                        return new PlayerShip("Player Spaceship",
                                "assets/spacegame/textures/spaceship1.png", this);
                    }
                });
                addComponent(enemySpawner = new SpawnManager(NUM_ENEMIES, 5) {
                    @Override
                    public GameObject createSpawnedObject() {
                        return new EnemyShip("Enemy Spaceship",
                                "assets/spacegame/textures/spaceship2.png", this);
                    }
                });
                addComponent(obstacleSpawner = new SpawnManager(NUM_OBSTACLES, 20) {
                    @Override
                    public GameObject createSpawnedObject() {
                        return new Asteroid("Asteroid", this);
                    }
                });

                // Populate world on start
                playerSpawner.populateToMax();
                enemySpawner.populateToMax();
                obstacleSpawner.populateToMax();
            }
        });
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
