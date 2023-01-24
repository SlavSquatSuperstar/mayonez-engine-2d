package slavsquatsuperstar.demos.spacegame;

import kotlin.Pair;
import mayonez.DebugDraw;
import mayonez.GameObject;
import mayonez.Preferences;
import mayonez.Scene;
import mayonez.graphics.Color;
import mayonez.graphics.Colors;
import mayonez.input.KeyInput;
import mayonez.math.Random;
import mayonez.math.Vec2;
import mayonez.physics.shapes.Circle;
import mayonez.physics.shapes.Shape;
import slavsquatsuperstar.demos.spacegame.objects.Asteroid;
import slavsquatsuperstar.demos.spacegame.objects.EnemyShip;
import slavsquatsuperstar.demos.spacegame.objects.PlayerShip;
import mayonez.scripts.SpawnManager;

import java.awt.*;
import java.util.ArrayList;

public class SpaceGameScene extends Scene {

    private final ArrayList<Pair<Shape, Color>> backgroundObjects;
    private final int numEnemies, numObstacles, numStars;

    public SpaceGameScene(String name) {
        super(name, Preferences.getScreenWidth() * 2, Preferences.getScreenHeight() * 2, 32f);
        setBackground(Colors.JET_BLACK);
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

        // Add background stars
        for (var i = 0; i < numStars; i++) {
            var starPos = this.getRandomPosition().mul(2);

            float starSize;
            if (Random.randomPercent(2f / 3f)) starSize = Random.randomFloat(1, 4); // dwarf
            else starSize = Random.randomFloat(4, 10); // giant

            var starDist = Random.randomFloat(5, 20) * 5f;
            var starColor = new Color(Random.randomInt(192, 255), Random.randomInt(192, 255), Random.randomInt(192, 255));
            addBackgroundObject(new Circle(starPos, starSize / starDist), starColor); // Star
        }

        // Add solar system
        addBackgroundObject(new Circle(new Vec2(-10, -8), 10), Colors.DARK_BLUE); // Earth
        addBackgroundObject(new Circle(new Vec2(12.5f, 7.5f), 2f), Colors.DARK_GRAY); // Moon
        addBackgroundObject(new Circle(new Vec2(-12, 11), 1.5f), Colors.YELLOW); // Sun
    }

    @Override
    protected void onUserUpdate(float dt) {
        // camera controls
        getCamera().rotate(KeyInput.getAxis("arrows horizontal"));
        getCamera().zoom(1 + 0.01f * KeyInput.getAxis("arrows vertical"));
    }

    @Override
    protected void onUserRender(Graphics2D g2) {
        for (var obj : backgroundObjects) {
            DebugDraw.fillShape(obj.getFirst(), obj.getSecond());
        }
    }

    private void addBackgroundObject(Shape objShape, Color objColor) {
        backgroundObjects.add(new Pair<>(objShape, objColor));
    }

}
