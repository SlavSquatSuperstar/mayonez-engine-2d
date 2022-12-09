package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.GameObject;
import mayonez.Transform;
import mayonez.graphics.Colors;
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

    public Asteroid(String name, SpawnManager obstacleSpawner) {
        super(name, Transform.scaleInstance(Random.randomVector(2f, 5f, 2f, 5f)));
        this.obstacleSpawner = obstacleSpawner;
    }

    @Override
    protected void init() {
        transform.position = getScene().getRandomPosition();
        transform.rotation = Random.randomFloat(0f, 360f);

        addComponent(new BallCollider(new Vec2(1f)).setDebugDraw(Colors.GRAY, true));
        Rigidbody rb;
        addComponent(rb = new Rigidbody(15f, 0.2f, 0.2f));
        rb.setVelocity(transform.getUp().mul(Random.randomFloat(0f, 1f)));
        addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
        addComponent(new Damageable(Random.randomInt(8, 12)) {
            @Override
            public void onDestroy() {
                obstacleSpawner.markObjectDestroyed();
            }
        });
    }
}
