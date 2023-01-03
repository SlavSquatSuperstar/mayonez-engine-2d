package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.GameObject;
import mayonez.Transform;
import mayonez.graphics.sprites.Sprite;
import mayonez.math.Random;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BoxCollider;
import mayonez.scripts.KeepInScene;
import slavsquatsuperstar.demos.spacegame.scripts.Damageable;
import slavsquatsuperstar.demos.spacegame.scripts.FireProjectile;
import slavsquatsuperstar.demos.spacegame.scripts.SpawnManager;

/**
 * An enemy spaceship that can be destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class EnemyShip extends GameObject {

    private final String spriteName;
    private final SpawnManager enemySpawner;
    private boolean isFiring;
    private int shotsLeft;

    public EnemyShip(String name, String spriteName, SpawnManager enemySpawner) {
        super(name, Transform.scaleInstance(new Vec2(2f)), 1);
        this.spriteName = spriteName;
        this.enemySpawner = enemySpawner;
        isFiring = false;
    }

    @Override
    protected void init() {
        transform.setPosition(getScene().getRandomPosition());
        transform.setRotation(Random.randomFloat(0f, 360f));

        addTag("Enemy");
        addComponent(Sprite.create(spriteName));
        addComponent(new BoxCollider(new Vec2(0.85f, 1f)));
        Rigidbody rb;
        addComponent(rb = new Rigidbody(1f, 0.01f, 0.8f));
        rb.setVelocity(transform.getUp().mul(Random.randomFloat(2f, 10f)));
        addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
        addComponent(new Damageable(4) {
            @Override
            public void onDestroy() {
                enemySpawner.markObjectDestroyed();
            }
        });
        addComponent(new FireProjectile(0.5f) {
            private int weaponChoice;

            @Override
            public void start() {
                weaponChoice = Random.randomBoolean() ? 2 : 1;
            }

            @Override
            protected boolean readyToFire() {
                if (isReloaded() && isFiring) {
                    // Decide to stop shooting
                    if (--shotsLeft > 0) isFiring = false;
                } else {
                    // Decide to start shooting
                    isFiring = Random.randomPercent(0.01f);
                    if (isFiring) shotsLeft = Random.randomInt(1, 5);
                }
                return isFiring;
            }

            @Override
            protected GameObject spawnProjectile() {
                if (weaponChoice == 1) {
                    return Projectiles.createLaser(gameObject);
                } else if (weaponChoice == 2) {
                    return Projectiles.createPlasma(gameObject);
                } else return null;
            }
        });
    }
}
