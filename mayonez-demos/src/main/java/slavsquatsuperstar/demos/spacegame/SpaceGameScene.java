package slavsquatsuperstar.demos.spacegame;

import mayonez.*;
import mayonez.graphics.Colors;
import mayonez.graphics.sprite.Sprite;
import mayonez.input.KeyInput;
import mayonez.math.Random;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BallCollider;
import mayonez.physics.colliders.BoxCollider;
import mayonez.physics.shapes.Circle;
import mayonez.scripts.Counter;
import mayonez.scripts.KeepInScene;
import mayonez.scripts.Timer;
import slavsquatsuperstar.demos.spacegame.scripts.Damageable;

import java.awt.*;

public class SpaceGameScene extends Scene {

    private final String shipSprite = "assets/textures/spacegame/spaceship1.png";
    private Counter enemyCounter, obstacleCounter;

    public SpaceGameScene() {
        super("Space Game", Preferences.getScreenWidth(), Preferences.getScreenHeight(), 32f);
        setBackground(Sprite.create(Colors.JET_BLACK));
        setGravity(new Vec2());
    }

    @Override
    protected void init() {
        addObject(new PlayerShip("Player Space Ship", shipSprite));

        addObject(new GameObject("Enemy Spawner") {
            private Timer enemySpawnTimer, obstacleSpawnTimer;

            @Override
            protected void init() {
                enemyCounter = new Counter(0, 3, 0);
                while (!enemyCounter.isAtMax()) {
                    getScene().addObject(createEnemy("Enemy Spaceship", shipSprite));
                    enemyCounter.count(1);
                }
                addComponent(enemySpawnTimer = new Timer(8));

                obstacleCounter = new Counter(0, 2, 0);
                while (!obstacleCounter.isAtMax()) {
                    getScene().addObject(createObstacle("Asteroid"));
                    obstacleCounter.count(1);
                }
                addComponent(obstacleSpawnTimer = new Timer(20));
            }

            @Override
            protected void onUserUpdate(float dt) {
                if (enemySpawnTimer.isReady()) {
                    if (!enemyCounter.isAtMax()) {
                        enemyCounter.count(1);
                        addObject(createEnemy("Enemy Spaceship", shipSprite));
                    }
                    enemySpawnTimer.reset();
                }
                if (obstacleSpawnTimer.isReady()) {
                    if (!obstacleCounter.isAtMax()) {
                        obstacleCounter.count(1);
                        addObject(createEnemy("Enemy Spaceship", shipSprite));
                    }
                    obstacleSpawnTimer.reset();
                }
            }
        });
    }

    @Override
    protected void onUserUpdate(float dt) {
        if (KeyInput.keyPressed("r")) SceneManager.reloadScene();
    }

    @Override
    protected void onUserRender(Graphics2D g2) {
        DebugDraw.fillShape(new Circle(new Vec2(-10, -8), 10), Colors.DARK_BLUE);
        DebugDraw.fillShape(new Circle(new Vec2(12.5f, 7.5f), 2.5f), Colors.DARK_GRAY);
    }

    private GameObject createEnemy(String name, String spriteName) {
        Vec2 randomPos = Random.randomVector(-getWidth(), getWidth(), -getHeight(), getHeight()).mul(0.5f);
        float randomRot = Random.randomFloat(0f, 360f);
        return new GameObject(name, new Transform(randomPos, randomRot, new Vec2(2f))) {
            @Override
            protected void init() {
                addTag("Enemy");
                addComponent(Sprite.create(spriteName));
                addComponent(new BoxCollider(new Vec2(0.85f, 1f)));
                Rigidbody rb;
                addComponent(rb = new Rigidbody(1f, 0.01f, 0.5f));
                rb.setVelocity(transform.getUp().mul(Random.randomFloat(2f, 10f)));
                addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
                addComponent(new Damageable(4) {
                    @Override
                    public void onDestroy() {
                        enemyCounter.count(-1);
                    }
                });
            }
        };
    }

    private GameObject createObstacle(String name) {
        Vec2 randomPos = Random.randomVector(-getWidth(), getWidth(), -getHeight(), getHeight()).mul(0.5f);
        float randomRot = Random.randomFloat(0f, 360f);
        Vec2 randomScl = Random.randomVector(2f, 5f, 2f, 5f);
        return new GameObject(name, new Transform(randomPos, randomRot, randomScl)) {
            @Override
            protected void init() {
                addComponent(new BallCollider(new Vec2(1f)).setDebugDraw(Colors.GRAY, true));
                Rigidbody rb;
                addComponent(rb = new Rigidbody(15f, 0.2f, 0.2f));
                rb.setVelocity(transform.getUp().mul(Random.randomFloat(0f, 1f)));
                addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
                addComponent(new Damageable(Random.randomInt(8, 12)) {
                    @Override
                    public void onDestroy() {
                        obstacleCounter.count(-1);
                    }
                });
            }
        };
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.start(new SpaceGameScene());
    }
}
