package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.mayonez.math.Vec2;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.input.KeyInput;

/**
 * Allows objects to be moved with the WASD/arrow keys.
 *
 * @author SlavSquatSuperstar
 */
public class KeyMovement extends MovementScript {

    private String xAxis = "horizontal";
    private String yAxis = "vertical";

    public KeyMovement(MoveMode mode, float speed) {
        super(mode, speed);
    }

    @Override
    public void update(float dt) {
        // Don't want to move faster diagonally so normalize
        Vec2 input = getRawInput().unit().mul(speed).rotate(transform.rotation);
        switch (mode) {
            case POSITION -> transform.move(input.mul(Mayonez.TIME_STEP));
            case VELOCITY -> rb.addVelocity(input);
            case ACCELERATION -> rb.applyAcceleration(input);
            case IMPULSE -> rb.applyImpulse(input);
            case FORCE -> rb.applyForce(input);
        }
        // Limit Top Speed
        if (rb != null && topSpeed > -1 && rb.getSpeed() > topSpeed)
            rb.setVelocity(rb.getVelocity().mul(topSpeed / rb.getSpeed()));
    }

    @Override
    protected Vec2 getRawInput() {
        return new Vec2(KeyInput.getAxis(xAxis), KeyInput.getAxis(yAxis));
    }

    public KeyMovement setTopSpeed(float topSpeed) {
        this.topSpeed = topSpeed;
        return this;
    }

}
