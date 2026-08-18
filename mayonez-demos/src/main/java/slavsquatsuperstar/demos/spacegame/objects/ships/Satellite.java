package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.CollisionEvent;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;
import slavsquatsuperstar.demos.spacegame.combat.CollisionDamage;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * A free-floating satellite that can be destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class Satellite extends GameObject {

    private final SatelliteProperties properties;

    public Satellite(String name, Vec2 position, SatelliteProperties properties) {
        super(name, new Transform(position, Random.randomAngle(), properties.scale()));
        this.properties = properties;
    }

    @Override
    protected void init() {
        addTag(SpaceGameScene.DAMAGEABLE_TAG);

        // Combat
        addComponent(new SpaceshipDestruction());
        var damageable = new Damageable(properties.maxHull()) {
            @Override
            public void onHealthDepleted() {
                var shipDestruction = gameObject.getComponent(SpaceshipDestruction.class);
                if (shipDestruction != null) shipDestruction.startDestructionSequence();
            }
        };
        addComponent(damageable);

        // Collision
        var cd = new CollisionDamage();
        addComponent(cd);
        var collider = new BoxCollider(properties.colliderSize()) {
            @Override
            public void onCollisionEvent(CollisionEvent event) {
                cd.onObjectCollision(event); // On collision
                damageable.onImpactProjectile(event); // On trigger
            }
        };
        collider.setLayer(getScene().getLayer(SpaceGameLayer.SPACECRAFT));
        addComponent(collider);
        addComponent(new KeepInScene(SpaceGameScene.SCENE_HALF_SIZE.mul(-1f),
                SpaceGameScene.SCENE_HALF_SIZE, KeepInScene.Mode.WRAP));

        // Initial Velocity
        Rigidbody rb;
        addComponent(rb = new Rigidbody(1f, 0.01f, 0.01f));
        rb.setVelocity(transform.getUp().mul(Random.randomFloat(0f, 4f)));

        // Visuals
        var sprite = Sprites.createSprite(properties.texture());
        sprite.setZIndex(SpaceGameZIndex.SPACESHIP);
        addComponent(sprite);
    }

}
