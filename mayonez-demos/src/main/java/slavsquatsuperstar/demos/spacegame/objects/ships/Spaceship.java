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
// TODO shields
public abstract class Spaceship extends GameObject {

    private final String spriteName;
    private final float maxHealth, shieldHealth;

    public Spaceship(String name, String spriteName, float maxHealth, float shieldHealth) {
        super(name, Transform.scaleInstance(new Vec2(2f)), SpaceGameZIndex.SPACESHIP);
        this.spriteName = spriteName;
        this.maxHealth = maxHealth;
        this.shieldHealth = shieldHealth;
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
        if (shieldHealth > 0f) {
            addComponent(new ShieldedDamageable(maxHealth, maxHealth * 0.5f) {
                @Override
                public void onHealthDepleted() {
                    super.onHealthDepleted();
                    var shipDestruction = getComponent(ShipDestruction.class);
                    if (shipDestruction != null) shipDestruction.startDestructionSequence();
                }
            });
        } else {
            addComponent(new Damageable(maxHealth) {
                @Override
                public void onHealthDepleted() {
                    var shipDestruction = getComponent(ShipDestruction.class);
                    if (shipDestruction != null) shipDestruction.startDestructionSequence();
                }
            });
        }

        // Visuals
        addComponent(Sprites.createSprite(spriteName));
    }

}
