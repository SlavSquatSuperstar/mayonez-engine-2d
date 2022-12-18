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
import slavsquatsuperstar.demos.spacegame.objects.PlayerShip;
import slavsquatsuperstar.demos.spacegame.scripts.SpawnManager;

import java.awt.*;
import java.util.ArrayList;

public class SpaceGameScene extends Scene {

    private final ArrayList<Pair<Shape, Color>> backgroundObjects;
    private final int numStars;

    public SpaceGameScene(String name) {
        super(name, Preferences.getScreenWidth() * 2, Preferences.getScreenHeight() * 2, 32f);
        setBackground(Colors.JET_BLACK);
        backgroundObjects = new ArrayList<>();
        numStars = 100;
    }

    @Override
    protected void init() {
        setGravity(new Vec2());
        backgroundObjects.clear();

        String shipSprite = "assets/textures/spacegame/spaceship1.png";
        addObject(new PlayerShip("Player Space Ship", shipSprite));
        // TODO jittering in GL with velocity
        // TODO jittering in AWT with high velocity

        // Spawn Stuff
        addObject(new GameObject("Object Spawner") {

            @Override
            protected void init() {
                SpawnManager enemySpawner;
                SpawnManager obstacleSpawner;
//                addComponent(enemySpawner = new SpawnManager(6, 5) {
//                    @Override
//                    public GameObject createSpawnedObject() {
//                        return new EnemyShip("Enemy Spaceship", "assets/textures/spacegame/spaceship1.png", this);
//                    }
//                });
//                addComponent(obstacleSpawner = new SpawnManager(3, 20) {
//                    @Override
//                    public GameObject createSpawnedObject() {
//                        return new Asteroid("Asteroid", this);
//                    }
//                });

                // Populate world on start
//                enemySpawner.populateToMax();
//                obstacleSpawner.populateToMax();
            }
        });

        // Add background stars
        for (int i = 0; i < numStars; i++) {
            Vec2 starPos = this.getRandomPosition().mul(2);
            float starSize = Random.randomFloat(1, 10);
            float starDist = Random.randomFloat(5, 20) * 5f;
            Color starColor = new Color(Random.randomInt(192, 255), Random.randomInt(192, 255), Random.randomInt(192, 255));
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
        for (Pair<Shape, Color> obj : backgroundObjects) {
            DebugDraw.fillShape(obj.getFirst(), obj.getSecond());
        }
    }

    private void addBackgroundObject(Shape objShape, Color objColor) {
        backgroundObjects.add(new Pair<>(objShape, objColor));
    }

}
