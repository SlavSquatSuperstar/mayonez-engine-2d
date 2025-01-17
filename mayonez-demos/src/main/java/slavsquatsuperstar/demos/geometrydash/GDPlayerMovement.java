package slavsquatsuperstar.demos.geometrydash;

import mayonez.input.*;
import mayonez.math.*;
import mayonez.scripts.movement.*;

/**
 * Allows the GD player to be controlled with the WASD keys
 *
 * @author SlavSquatSuperstar
 */
class GDPlayerMovement extends MovementScript {

    private final float speed;
    private final InputAxis xAxis, yAxis;

    GDPlayerMovement(float speed, InputAxis xAxis, InputAxis yAxis) {
        this.speed = speed;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    @Override
    protected void update(float dt) {
        moveObject(getUserInput().mul(speed), MoveMode.POSITION, dt);
    }

    @Override
    public Vec2 getUserInput() {
        // Normalize so don't move faster diagonally
        return new Vec2(KeyInput.getAxis(xAxis), KeyInput.getAxis(yAxis)).unit();
    }

}
