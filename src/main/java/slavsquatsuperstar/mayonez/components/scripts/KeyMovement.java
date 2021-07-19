package slavsquatsuperstar.mayonez.components.scripts;

import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.Vector2;

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
                parent.transform.move(input);
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
        return new Vector2(Game.keyboard().getAxis(xAxis), Game.keyboard().getAxis(yAxis));
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
