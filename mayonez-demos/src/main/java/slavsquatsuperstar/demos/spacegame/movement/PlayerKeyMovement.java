package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.input.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameConfig;

/**
 * Controls the player spaceship's movement using the keyboard.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerKeyMovement extends SpaceshipMovement {

    // Config
    private static final InputAxis HORIZONTAL_MOVE_AXIS = SpaceGameConfig.getHorizontalMoveAxis();
    private static final InputAxis VERTICAL_MOVE_AXIS = SpaceGameConfig.getVerticalMoveAxis();
    private static final InputAxis TURN_AXIS = SpaceGameConfig.getTurnAxis();
    private static final Key BRAKE_KEY = SpaceGameConfig.getBreakKey();

    // Movement Fields
    private final float moveSpeed, turnSpeed;

    public PlayerKeyMovement(float moveSpeed, float turnSpeed) {
        this.moveSpeed = moveSpeed;
        this.turnSpeed = turnSpeed;
    }

    @Override
    protected void update(float dt) {
        super.update(dt);

        // Move player
        var moveDirection = getUserInput().mul(moveSpeed)
                .rotate(transform.getRotation()); // Align with object direction
        moveObject(moveDirection, dt);
        rotateObject(getUserInputValue() * turnSpeed, dt);
    }

    // Movement Script Overrides

    @Override
    public void moveObject(Vec2 amount, float dt) {
        rb.applyForce(amount);
    }

    @Override
    public void rotateObject(float amount, float dt) {
        rb.applyTorque(amount);
    }

    @Override
    protected boolean isBraking() {
        return KeyInput.keyDown(BRAKE_KEY);
    }

    @Override
    protected void brake(Vec2 brakeDir, float angBrakeDir) {
        moveObject(brakeDir.mul(moveSpeed), 0f);
        rotateObject(angBrakeDir * turnSpeed, 0f);

        // Stop moving if too slow
        if (Math.abs(rb.getVelocity().x) < BRAKE_THRESHOLD_SPEED) {
            rb.getVelocity().x = 0f;
        }
        if (Math.abs(rb.getVelocity().y) < BRAKE_THRESHOLD_SPEED) {
            rb.getVelocity().y = 0f;
        }
        if (rb.getAngSpeed() < TURN_BRAKE_THRESHOLD_SPEED) {
            rb.setAngVelocity(0f);
        }
    }

    // Input Overrides

    @Override
    public Vec2 getUserInput() {
        return new Vec2(KeyInput.getAxis(HORIZONTAL_MOVE_AXIS), KeyInput.getAxis(VERTICAL_MOVE_AXIS));
    }

    @Override
    public float getUserInputValue() {
        return -KeyInput.getAxis(TURN_AXIS);
    }

}
