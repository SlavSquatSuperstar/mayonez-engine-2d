package slavsquatsuperstar.demos.spacegame;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.math.Random;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.scripts.*;
import mayonez.util.*;
import slavsquatsuperstar.demos.spacegame.objects.*;

import java.util.*;

public class SpaceGameScene extends Scene {

    private final static int SCENE_SIZE = 3;
    private final List<BackgroundObject> backgroundObjects;
    private final int numEnemies, numObstacles, numStars;

    public SpaceGameScene(String name) {
        super(name, Preferences.getScreenWidth() * SCENE_SIZE,
                Preferences.getScreenHeight() * SCENE_SIZE, 32f);
        setBackground(new Color(14, 14, 14));
        backgroundObjects = new ArrayList<>();
        numEnemies = 6;
        numObstacles = 5;
        numStars = 1200;
    }

    @Override
    protected void init() {
        setGravity(new Vec2());
        backgroundObjects.clear();

        addObject(new PlayerShip("Player Spaceship", "assets/textures/spacegame/spaceship1.png"));
        addSpawners();

        addSolarSystem();
        addBackgroundStars(numStars);
    }

    private void addSpawners() {
        addObject(new GameObject("Object Spawner") {
            @Override
            protected void init() {
                SpawnManager enemySpawner;
                SpawnManager obstacleSpawner;
                addComponent(enemySpawner = new SpawnManager(numEnemies, 5) {
                    @Override
                    public GameObject createSpawnedObject() {
                        return new EnemyShip("Enemy Spaceship", "assets/textures/spacegame/spaceship2.png", this);
                    }
                });
                addComponent(obstacleSpawner = new SpawnManager(numObstacles, 20) {
                    @Override
                    public GameObject createSpawnedObject() {
                        return new Asteroid("Asteroid", this);
                    }
                });

                // Populate world on start
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

    private void addBackgroundStars(int numStars) {
        // TODO parallax
        for (var i = 0; i < numStars; i++) {
            var starPos = this.getRandomPosition().mul(2);

            float starSize;
            boolean isDwarfStar = Random.randomPercent(2f / 3f);
            if (isDwarfStar) starSize = Random.randomGaussian(2.5f, 0.5f);
            else starSize = Random.randomGaussian(7, 1);
            if (starSize > 1) starSize = 1;

            var starDist = Random.randomFloat(20, 60);
            var starColor = Random.randomColor(192, 255);
            addBackgroundObject(new Circle(starPos, starSize / starDist), starColor, ZIndex.BACKGROUND_STAR);
        }
    }

    @Override
    protected void onUserUpdate(float dt) {
        // camera controls
        getCamera().rotate(KeyInput.getAxis("arrows horizontal"));
        getCamera().zoom(1 + 0.01f * KeyInput.getAxis("arrows vertical"));
    }

    @Override
    protected void onUserRender() {
        backgroundObjects.forEach(obj -> obj.debugDraw(getDebugDraw()));
    }

    private void addBackgroundObject(Shape shape, Color color, int zIndex) {
        backgroundObjects.add(new BackgroundObject(shape, color, zIndex));
    }

}
