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

    private final List<BackgroundObject> backgroundObjects;
    private final int numEnemies, numObstacles, numStars;

    public SpaceGameScene(String name) {
        super(name, Preferences.getScreenWidth() * 2, Preferences.getScreenHeight() * 2, 32f);
        setBackground(new Color(14, 14, 14));
        backgroundObjects = new ArrayList<>();
        numEnemies = 6;
        numObstacles = 3;
        numStars = 750;
    }

    @Override
    protected void init() {
        setGravity(new Vec2());
        backgroundObjects.clear();

        addObject(new PlayerShip("Player Spaceship", "assets/textures/spacegame/spaceship1.png"));

        // Spawn Stuff
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

        addBackgroundStars();
        addSolarSystem();
    }

    private void addSolarSystem() {
        addBackgroundObject("Earth", new Circle(new Vec2(-10, -8), 10), Colors.DARK_BLUE);
        addBackgroundObject("Moon", new Circle(new Vec2(12.5f, 7.5f), 2f), Colors.DARK_GRAY);
        addBackgroundObject("Sun", new Circle(new Vec2(-12, 11), 1.5f), Colors.YELLOW);
    }

    private void addBackgroundStars() {
        for (var i = 0; i < numStars; i++) {
            var starPos = this.getRandomPosition().mul(2);

            float starSize;
            if (Random.randomPercent(2f / 3f)) starSize = Random.randomFloat(1, 4); // dwarf
            else starSize = Random.randomFloat(4, 10); // giant

            var starDist = Random.randomFloat(5, 20) * 5f;
            var starColor = new Color(Random.randomInt(192, 255), Random.randomInt(192, 255), Random.randomInt(192, 255));
            addBackgroundObject("Star", new Circle(starPos, starSize / starDist), starColor);
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

    private void addBackgroundObject(String name, Shape shape, Color color) {
        backgroundObjects.add(new BackgroundObject(name, shape, color));
    }

}
