package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
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
        super(name, new Transform(position, 0f, properties.scale()),
                SpaceGameZIndex.SPACESHIP);
        this.properties = properties;
    }

    @Override
    protected void init() {
        setLayer(getScene().getLayer(SpaceGameLayer.SHIPS));

        // Collision
        addComponent(new BoxCollider(properties.colliderSize()));
        addComponent(new KeepInScene(SpaceGameScene.SCENE_HALF_SIZE.mul(-1f),
                SpaceGameScene.SCENE_HALF_SIZE, KeepInScene.Mode.WRAP));

        // Movement
        addComponent(new ThrustController(properties.thrusters()));

        // Combat
        addComponent(new SpaceshipDestruction());
        addComponent(getDamageable(properties.maxHull(), properties.maxShield(), properties.shieldRegen()));
        addComponent(new CollisionDamage());

        // Visuals
        addComponent(Sprites.createSprite(properties.texture()));
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
