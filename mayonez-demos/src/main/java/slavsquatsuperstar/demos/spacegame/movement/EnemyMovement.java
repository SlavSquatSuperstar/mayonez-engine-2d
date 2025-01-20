package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.math.*;

/**
 * Controls an enemy spaceship's movement.
 *
 * @author SlavSquatSuperstar
 */
public class EnemyMovement extends SpaceshipMovement {

    // Constants
    private static final ThrusterParameters MOVE_PARAMETERS
            = new ThrusterParameters(5f, 7.5f, 12.5f, 15f);
    private static final ThrusterParameters TURN_PARAMETERS
            = new ThrusterParameters(15f, 30f, 75f, 90f);

    // Movement Fields
    private ThrusterState moveState, turnState;

    @Override
    protected void start() {
        super.start();
        moveState = ThrusterState.COAST;
        turnState = ThrusterState.COAST;
    }

    @Override
    protected void update(float dt) {
        // Update thruster states
        moveState = getNextThrusterState(moveState, rb.getSpeed(), MOVE_PARAMETERS);
        turnState = getNextThrusterState(turnState, rb.getAngSpeed(), TURN_PARAMETERS);

        // Get move and brake direction (relative to ship)
        var moveInput = getUserInput().mul(5f);
        var turnInput = getUserInputValue() * 10f;
        var brakeDir = getBrakeDir(new Vec2()).mul(5f);
        var turnBrakeDir = getTurnBrakeDir(0f) * 10f;

        // Move and brake (relative to world)
        moveObject(moveInput.rotate(transform.getRotation()), dt);
        rotateObject(turnInput, dt);
        brake(brakeDir.rotate(transform.getRotation()), turnBrakeDir);

        // Fire thrusters
        getThrustController().fireMoveThrusters(moveInput, brakeDir);
        getThrustController().fireTurnThrusters(turnInput, turnBrakeDir);
    }

    @Override
    protected void brake(Vec2 brakeDir, float angBrakeDir) {
        moveObject(brakeDir, 0f);
        rotateObject(angBrakeDir, 0f);
    }

    // Movement Script Overrides

    @Override
    public Vec2 getUserInput() {
        if (moveState == ThrusterState.SPEED_UP) {
            return rb.getVelocity().rotate(-transform.getRotation()).unit();
        } else {
            return new Vec2();
        }
    }

    @Override
    public float getUserInputValue() {
        if (moveState == ThrusterState.SPEED_UP) {
            return Math.signum(rb.getAngVelocity());
        } else {
            return 0f;
        }
    }

    @Override
    protected Vec2 getBrakeDir(Vec2 moveInput) {
        if (moveState == ThrusterState.SLOW_DOWN) {
            return rb.getVelocity().rotate(-transform.getRotation())
                    .unit().mul(-1f);
        } else {
            return new Vec2();
        }
    }

    @Override
    protected float getTurnBrakeDir(float turnInput) {
        if (turnState == ThrusterState.SLOW_DOWN) {
            return -Math.signum(rb.getAngVelocity());
        } else {
            return 0f;
        }
    }

    // Helper Classes and Methods

    private ThrusterState getNextThrusterState(
            ThrusterState currentState, float speed, ThrusterParameters parameters
    ) {
        switch (currentState) {
            case COAST -> {
                if (speed < parameters.minSpeed) return ThrusterState.SPEED_UP;
                else if (speed > parameters.maxSpeed) return ThrusterState.SLOW_DOWN;
            }
            case SPEED_UP -> {
                if (speed > parameters.minCoast) return ThrusterState.COAST;
            }
            case SLOW_DOWN -> {
                if (speed < parameters.maxCoast) return ThrusterState.COAST;
            }
        }
        return currentState;
    }

    private enum ThrusterState {
        COAST,
        SPEED_UP,
        SLOW_DOWN
    }

    private record ThrusterParameters(float minSpeed, float minCoast, float maxCoast, float maxSpeed) {
    }

}
