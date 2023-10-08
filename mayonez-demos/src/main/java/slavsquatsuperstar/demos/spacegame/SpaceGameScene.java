package slavsquatsuperstar.demos.spacegame;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.math.Random;
import mayonez.math.*;
import mayonez.math.shapes.*;
import slavsquatsuperstar.demos.spacegame.objects.asteroids.Asteroid;
import slavsquatsuperstar.demos.spacegame.objects.BackgroundObject;
import slavsquatsuperstar.demos.spacegame.objects.SpawnManager;
import slavsquatsuperstar.demos.spacegame.objects.ZIndex;
import slavsquatsuperstar.demos.spacegame.objects.ships.*;

import java.util.*;

public class SpaceGameScene extends Scene {

    // Constants
    private final static int SCENE_SIZE = 3;
    private final static int NUM_PLAYERS = 1;
    private final static int NUM_ENEMIES = 6;
    private final static int NUM_OBSTACLES = 5;
    private final static int NUM_STARS = 1200;

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

        addSpawners();
        addSolarSystem();
        addBackgroundStars();
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
        addBackgroundObject(new Circle(new Vec2(-10, -8), 12), Colors.DARK_BLUE, ZIndex.BACKGROUND); // Earth
        addBackgroundObject(new Circle(new Vec2(12.5f, 7.5f), 2.5f), Colors.DARK_GRAY, ZIndex.BACKGROUND); // Moon
        addBackgroundObject(new Circle(new Vec2(-20, 16), 2), Colors.YELLOW, ZIndex.BACKGROUND); // Sun
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
            addBackgroundObject(new Circle(starPos, starSize / starDist), starColor, ZIndex.BACKGROUND_STAR);
        }
    }

    private void addBackgroundObject(Shape shape, Color color, int zIndex) {
        backgroundObjects.add(new BackgroundObject(shape, color, zIndex));
    }

}
