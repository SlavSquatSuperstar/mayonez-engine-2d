package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.mayonez.KeyInput;
import slavsquatsuperstar.mayonez.Vector2;

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
        Vector2 input = getRawInput().unitVector().mul(speed);
        switch (mode) {
            case POSITION:
                transform.move(input);
                break;
            case IMPULSE:
                rb.addImpulse(input);
                break;
            case FORCE:
                rb.addForce(input);
                break;
        }
        // Limit Top Speed
        if (rb != null && topSpeed > -1 && rb.speed() > topSpeed)
            rb.velocity().set(rb.velocity().mul(topSpeed / rb.speed()));
    }

    @Override
    protected Vector2 getRawInput() {
        return new Vector2(KeyInput.getAxis(xAxis), KeyInput.getAxis(yAxis));
    }

    public void setXAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public void setYAxis(String yAxis) {
        this.yAxis = yAxis;
    }

    public KeyMovement setTopSpeed(float topSpeed) {
        this.topSpeed = topSpeed;
        return this;
    }

}
