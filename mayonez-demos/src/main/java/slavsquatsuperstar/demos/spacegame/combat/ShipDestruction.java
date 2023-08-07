package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.movement.ThrustController;

/**
 * Plays a destruction sequence after a spaceship's health is depleted.
 *
 * @author SlavSquatSuperstar
 */
// TODO shields
public class ShipDestruction extends Damageable {

    // Timer Components
    private final Timer destructionTimer;
    private boolean healthDepleted;

    // Component References
    private Component fireProjectile, thrustController;

    public ShipDestruction(float maxHealth) {
        super(maxHealth);
        destructionTimer = new Timer(2);
    }

    @Override
    public void init() {
        gameObject.addComponent(destructionTimer.setEnabled(false));
    }

    @Override
    public void start() {
        healthDepleted = false;
        fireProjectile = gameObject.getComponent(FireProjectile.class);
        thrustController = gameObject.getComponent(ThrustController.class);
    }

    @Override
    public void update(float dt) {
        if (!healthDepleted) super.update(dt);
        if (destructionTimer.isReady()) gameObject.destroy();
    }

    @Override
    public void onHealthDepleted() {
        healthDepleted = true;
        destructionTimer.setEnabled(true);

        if (fireProjectile != null) fireProjectile.setEnabled(false);
        if (thrustController != null) thrustController.setEnabled(false);
    }

}
