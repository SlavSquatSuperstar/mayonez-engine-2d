package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.math.Random;
import mayonez.math.*;
import mayonez.scripts.KeepInScene;
import mayonez.scripts.Timer;
import mayonez.scripts.movement.*;
import slavsquatsuperstar.demos.spacegame.movement.ThrustController;

import java.util.*;

/**
 * Plays a destruction sequence and disables all ship systems after a spaceship's health
 * is depleted.
 *
 * @author SlavSquatSuperstar
 */
public class ShipDestruction extends Script {

    // Constants
    private static final float EXPLOSION_DURATION = 1.2f;
    private static final float DESTRUCTION_DURATION = 0.8f;

    // Timer Components
    private final Timer destructionTimer;
    private boolean sequenceStarted;

    // References
    private GameObject explosion;
    private final List<Component> shipSystems;

    public ShipDestruction() {
        destructionTimer = new Timer(DESTRUCTION_DURATION);
        shipSystems = new LinkedList<>();
    }

    @Override
    public void init() {
        shipSystems.clear();
        gameObject.addComponent(destructionTimer.setEnabled(false));
    }

    @Override
    public void start() {
        sequenceStarted = false;
        explosion = null;

        shipSystems.add(gameObject.getComponent(FireProjectile.class));
        shipSystems.add(gameObject.getComponent(ThrustController.class));
        shipSystems.add(gameObject.getComponent(KeyMovement.class));
        shipSystems.add(gameObject.getComponent(KeyRotation.class));
        shipSystems.add(gameObject.getComponent(KeepInScene.class));
    }

    @Override
    public void update(float dt) {
        if (destructionTimer.isReady()) {
            gameObject.destroy();
        }
    }

    @Override
    public void debugRender() {
        if (explosion != null) {
            // Have the explosion follow the ship until it is destroyed
            explosion.transform.setPosition(transform.getPosition());
        }
    }

    public void startDestructionSequence() {
        if (sequenceStarted) return;

        sequenceStarted = true;
        destructionTimer.setEnabled(true);

        // Disable ship systems
        for (var system : shipSystems) {
            if (system != null) system.setEnabled(false);
        }
        shipSystems.clear();

        getScene().addObject(explosion = Explosion.createPrefab(
                "Ship Explosion",
                new Transform(transform.getPosition(), Random.randomFloat(0f, 360f), new Vec2(2)),
                EXPLOSION_DURATION)
        );
    }

}
