package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.CollisionEvent;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;
import slavsquatsuperstar.demos.spacegame.combat.CollisionDamage;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.combat.ShieldedDamageable;
import slavsquatsuperstar.demos.spacegame.movement.ThrustController;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * A spaceship that can move, fire projectiles, and be destroyed.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Spaceship extends GameObject {

    protected final SpaceshipProperties properties;

    public Spaceship(String name, Vec2 position, SpaceshipProperties properties) {
        super(name, new Transform(position, 0f, properties.scale()));
        this.properties = properties;
    }

    @Override
    protected void init() {
        addTag(SpaceGameScene.DAMAGEABLE_TAG);

        // Combat
        addComponent(new SpaceshipDestruction());
        var damageable = getDamageable(properties.maxHull(), properties.maxShield(), properties.shieldRegen());
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

        // Movement
        addComponent(new ThrustController(properties.thrusters()));

        // Visuals
        var sprite = Sprites.createSprite(properties.texture());
        sprite.setZIndex(SpaceGameZIndex.SPACESHIP);
        addComponent(sprite);
    }

    private static Damageable getDamageable(float maxHull, float maxShield, float shieldRegen) {
        if (maxShield > 0f) {
            return new ShieldedDamageable(maxHull, maxShield, shieldRegen) {
                @Override
                public void onHealthDepleted() {
                    var shipDestruction = gameObject.getComponent(SpaceshipDestruction.class);
                    if (shipDestruction != null) shipDestruction.startDestructionSequence();
                }
            };
        } else {
            return new Damageable(maxHull) {
                @Override
                public void onHealthDepleted() {
                    var shipDestruction = gameObject.getComponent(SpaceshipDestruction.class);
                    if (shipDestruction != null) shipDestruction.startDestructionSequence();
                }
            };
        }
    }

}
