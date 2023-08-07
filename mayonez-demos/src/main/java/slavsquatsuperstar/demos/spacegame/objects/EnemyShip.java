package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.ZIndex;
import slavsquatsuperstar.demos.spacegame.combat.*;
import slavsquatsuperstar.demos.spacegame.combat.EnemyFireController;
import slavsquatsuperstar.demos.spacegame.movement.EnemyThrustController;
import slavsquatsuperstar.demos.spacegame.movement.ThrusterPrefabs;

/**
 * An enemy spaceship that can be destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class EnemyShip extends GameObject {

    private final String spriteName;
    private final SpawnManager enemySpawner;

    public EnemyShip(String name, String spriteName, SpawnManager enemySpawner) {
        super(name, Transform.scaleInstance(new Vec2(2f)), ZIndex.SPACESHIP);
        this.spriteName = spriteName;
        this.enemySpawner = enemySpawner;
    }

    @Override
    protected void init() {
        transform.setPosition(getScene().getRandomPosition());
        transform.setRotation(Random.randomFloat(0f, 360f));

        addTag("Enemy");
        addComponent(Sprites.createSprite(spriteName));
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
        addComponent(new EnemyFireController(0.5f));

        var thrusters = ThrusterPrefabs.addThrustersToObject(this);
        addComponent(new EnemyThrustController(thrusters));
    }
}
