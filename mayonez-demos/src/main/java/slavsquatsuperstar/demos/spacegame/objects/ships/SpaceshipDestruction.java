package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.*;
import mayonez.math.Random;
import mayonez.scripts.*;
import mayonez.scripts.movement.*;
import slavsquatsuperstar.demos.spacegame.combat.ExplosionPrefabs;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.FireProjectile;
import slavsquatsuperstar.demos.spacegame.movement.ThrustController;

import java.util.*;

/**
 * Plays a destruction sequence and disables all ship systems after a spaceship's health
 * is depleted.
 *
 * @author SlavSquatSuperstar
 */
public class SpaceshipDestruction extends TimerScript {

    // Constants
    private static final float EXPLOSION_DURATION = 0.8f;
    private static final float DESTRUCTION_DURATION = 0.6f;

    // References
    private final List<Component> shipSystems;
    private GameObject explosion;

    public SpaceshipDestruction() {
        super(DESTRUCTION_DURATION);
        shipSystems = new ArrayList<>();
    }

    @Override
    protected void start() {
        shipSystems.clear();
        explosion = null;

        shipSystems.add(gameObject.getComponent(FireProjectile.class));
        shipSystems.add(gameObject.getComponent(ThrustController.class));
        shipSystems.add(gameObject.getComponent(MovementScript.class));
        shipSystems.add(gameObject.getComponent(KeepInScene.class));
    }

    @Override
    protected void update(float dt) {
        super.update(dt);
        if (this.isReady()) gameObject.destroy();
    }

    @Override
    protected void debugRender() {
        if (explosion != null) {
            // Have the explosion follow the ship until it is destroyed
            explosion.transform.setPosition(transform.getPosition());
        }
    }

    /**
     * Begin the ship destruction sequence.
     */
    public void startDestructionSequence() {
        if (this.isStarted()) return;
        this.setStarted(true);

        // Disable ship systems
        for (var system : shipSystems) {
            if (system != null) system.setEnabled(false);
        }
        shipSystems.clear();

        getScene().addObject(explosion = ExplosionPrefabs.createPrefab(
                "Ship Explosion",
                new Transform(transform.getPosition(), Random.randomAngle(), transform.getScale()),
                EXPLOSION_DURATION
        ));
    }

}
