package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.combat.CollisionDamage;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.combat.ShieldedDamageable;
import slavsquatsuperstar.demos.spacegame.movement.ThrustController;
import slavsquatsuperstar.demos.spacegame.movement.ThrusterPrefabs;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * A spaceship that can move, fire projectiles, and be destroyed.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Spaceship extends GameObject {

    private final SpaceshipProperties properties;

    public Spaceship(String name, SpaceshipProperties properties) {
        super(name, Transform.scaleInstance(new Vec2(2f)), SpaceGameZIndex.SPACESHIP);
        this.properties = properties;
    }

    @Override
    protected void init() {
        setLayer(getScene().getLayer(SpaceGameLayer.SHIPS));

        // Collision
        addComponent(new BoxCollider(new Vec2(0.85f, 1f)));
        addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
        addComponent(new CollisionDamage());

        // Movement
        var thrusters = ThrusterPrefabs.addThrustersToObject(this);
        addComponent(new ThrustController(thrusters));

        // Combat
        addComponent(new ShipDestruction());
        addComponent(getDamageable(properties.maxHealth(), properties.maxShieldHealth()));

        // Visuals
        addComponent(Sprites.createSprite(properties.spriteName()));
    }

    private static Component getDamageable(float maxHealth, float shieldHealth) {
        if (shieldHealth > 0f) {
            return new ShieldedDamageable(maxHealth, shieldHealth) {
                @Override
                public void onHealthDepleted() {
                    var shipDestruction = gameObject.getComponent(ShipDestruction.class);
                    if (shipDestruction != null) shipDestruction.startDestructionSequence();
                }
            };
        } else {
            return new Damageable(maxHealth) {
                @Override
                public void onHealthDepleted() {
                    var shipDestruction = gameObject.getComponent(ShipDestruction.class);
                    if (shipDestruction != null) shipDestruction.startDestructionSequence();
                }
            };
        }
    }

}
