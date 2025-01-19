package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.math.*;
import mayonez.scripts.movement.*;

/**
 * Controls a spaceship's movement and activates its thrusters.
 *
 * @author SlavSquatSuperstar
 */
public abstract class SpaceshipMovement extends MovementScript {

    // Constants
    protected final static float BRAKE_THRESHOLD_SPEED = 0.1f;
    protected final static float TURN_BRAKE_THRESHOLD_SPEED = 2f;

    // Script References
    private ThrustController thrustController;

    @Override
    protected void start() {
        super.start();
        if (rb == null) setEnabled(false);
        thrustController = gameObject.getComponent(ThrustController.class);
    }

    // Brake Methods

    protected void brake(Vec2 brakeDir, float angBrakeDir) {
    }

    protected Vec2 getBrakeDir(Vec2 moveInput) {
        return new Vec2();
    }

    protected float getTurnBrakeDir(float turnInput) {
        return 0f;
    }

    // Component Getters

    protected ThrustController getThrustController() {
        return thrustController;
    }

}
