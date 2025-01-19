package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.*;
import mayonez.assets.*;
import mayonez.assets.text.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;
import slavsquatsuperstar.demos.spacegame.combat.CollisionDamage;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.combat.ShieldedDamageable;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.WeaponHardpoint;
import slavsquatsuperstar.demos.spacegame.movement.ThrustController;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

import java.util.*;

/**
 * A spaceship that can move, fire projectiles, and be destroyed.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Spaceship extends GameObject {

    // Assets
    private static final CSVFile HARDPOINTS_DATA;
    static final List<WeaponHardpoint> HARDPOINTS;

    static {
        HARDPOINTS_DATA = Assets.getAsset(
                "assets/spacegame/data/shuttle_hardpoints.csv", CSVFile.class
        );
        if (HARDPOINTS_DATA == null) HARDPOINTS = Collections.emptyList();
        else HARDPOINTS = HARDPOINTS_DATA.readCSV().stream()
                .map(WeaponHardpoint::new).toList();
    }

    protected final SpaceshipProperties properties;

    public Spaceship(String name, Vec2 position, SpaceshipProperties properties) {
        super(name, new Transform(position, 0f, new Vec2(2f)), SpaceGameZIndex.SPACESHIP);
        this.properties = properties;
    }

    @Override
    protected void init() {
        setLayer(getScene().getLayer(SpaceGameLayer.SHIPS));

        // Collision
        addComponent(new BoxCollider(new Vec2(0.85f, 1f)));
        addComponent(new KeepInScene(SpaceGameScene.SCENE_HALF_SIZE.mul(-1f),
                SpaceGameScene.SCENE_HALF_SIZE, KeepInScene.Mode.WRAP));

        // Movement
        addComponent(new ThrustController(properties.thrusters()));

        // Combat
        addComponent(new SpaceshipDestruction());
        addComponent(getDamageable(properties.maxHealth(), properties.maxShieldHealth()));
        addComponent(new CollisionDamage());

        // Visuals
        addComponent(Sprites.createSprite(properties.spriteName()));
    }

    private static Damageable getDamageable(float maxHealth, float shieldHealth) {
        if (shieldHealth > 0f) {
            return new ShieldedDamageable(maxHealth, shieldHealth) {
                @Override
                public void onHealthDepleted() {
                    var shipDestruction = gameObject.getComponent(SpaceshipDestruction.class);
                    if (shipDestruction != null) shipDestruction.startDestructionSequence();
                }
            };
        } else {
            return new Damageable(maxHealth) {
                @Override
                public void onHealthDepleted() {
                    var shipDestruction = gameObject.getComponent(SpaceshipDestruction.class);
                    if (shipDestruction != null) shipDestruction.startDestructionSequence();
                }
            };
        }
    }

}
