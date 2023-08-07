package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.math.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.movement.ThrustController;

/**
 * Plays a destruction sequence after a spaceship's health is depleted.
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
    private Component fireProjectile, thrustController;

    public ShipDestruction() {
        destructionTimer = new Timer(DESTRUCTION_DURATION);
    }

    @Override
    public void init() {
        gameObject.addComponent(destructionTimer.setEnabled(false));
    }

    @Override
    public void start() {
        sequenceStarted = false;
        fireProjectile = gameObject.getComponent(FireProjectile.class);
        thrustController = gameObject.getComponent(ThrustController.class);
        explosion = null;
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
        if (fireProjectile != null) fireProjectile.setEnabled(false);
        if (thrustController != null) thrustController.setEnabled(false);

        getScene().addObject(explosion = Explosion.createPrefab(
                "Ship Explosion",
                new Transform(transform.getPosition(), Random.randomFloat(0f, 360f), new Vec2(2)),
                EXPLOSION_DURATION)
        );
    }

}
