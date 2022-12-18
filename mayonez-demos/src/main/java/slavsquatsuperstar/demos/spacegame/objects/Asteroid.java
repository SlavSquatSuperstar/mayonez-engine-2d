package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.GameObject;
import mayonez.Transform;
import mayonez.graphics.Colors;
import mayonez.graphics.sprite.ShapeSprite;
import mayonez.math.Random;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BallCollider;
import mayonez.scripts.KeepInScene;
import slavsquatsuperstar.demos.spacegame.scripts.Damageable;
import slavsquatsuperstar.demos.spacegame.scripts.SpawnManager;

/**
 * An asteroid in space that can be destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class Asteroid extends GameObject {

    private final SpawnManager obstacleSpawner;
    private final Vec2 size;
    private final int startingHealth;
    private final boolean isFragment;

    private Asteroid(String name, Vec2 size, int startingHealth, boolean isFragment, SpawnManager obstacleSpawner) {
        super(name, Transform.scaleInstance(size), 1);
        this.size = size;
        this.startingHealth = startingHealth;
        this.isFragment = isFragment;
        this.obstacleSpawner = obstacleSpawner;
    }

    public Asteroid(String name, SpawnManager obstacleSpawner) {
        this(name, Random.randomVector(2f, 5f, 2f, 5f),
                Random.randomInt(8, 12), false, obstacleSpawner);
    }

    public Asteroid(String name, Vec2 size, int startingHealth) {
        this(name, size, startingHealth, true, null);
    }

    @Override
    protected void init() {
        transform.position = getScene().getRandomPosition();
        transform.rotation = Random.randomFloat(0f, 360f);

        addComponent(new BallCollider(new Vec2(1f)));
        addComponent(new ShapeSprite(Colors.GRAY, true));
        Rigidbody rb;
        addComponent(rb = new Rigidbody(15f, 0.2f, 0.2f));
        rb.setVelocity(transform.getUp().mul(Random.randomFloat(0f, 1f)));
        addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
        addComponent(new Damageable(startingHealth) {
            @Override
            public void onDestroy() {
                if (obstacleSpawner != null) obstacleSpawner.markObjectDestroyed();
                // TODO need to set position manually
//                if (!isFragment) {
//                    int fragmentCount = Random.randomInt(0, 4);
//                    if (fragmentCount <= 0) return;
//                    for (int i = 0; i < 2; i++) {
//                        getScene().addObject(new Asteroid("Asteroid Fragment",
//                                size.div(2), startingHealth / 2));
//                    }
//                }
            }
        });
    }
}
