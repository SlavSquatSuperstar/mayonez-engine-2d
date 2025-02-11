package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.math.*;

/**
 * Which direction a thruster propels a spaceship.
 *
 * @author SlavSquatSuperstar
 */
public enum ThrustDirection {

    FORWARD(new Vec2(0f, 1f), 0f),
    BACKWARD(new Vec2(0f, -1f), 0f),
    LEFT(new Vec2(-1f, 0f), 0f),
    RIGHT(new Vec2(1f, 0f), 0f),

    TURN_LEFT(new Vec2(), 1f), // counter-clockwise
    TURN_RIGHT(new Vec2(), -1f), // clockwise

    NONE(new Vec2(), 0f); // sentinel

    private final Vec2 moveDir;
    private final float turnDir;

    ThrustDirection(Vec2 moveDir, float turnDir) {
        this.moveDir = moveDir;
        this.turnDir = turnDir;
    }

    public boolean faces(Vec2 moveDir) {
        return this.moveDir.dot(moveDir) > SpaceshipMovement.BRAKE_THRESHOLD_SPEED;
    }

    public boolean faces(float turnDir) {
        // Simulate dot product with z-components
        return this.turnDir * turnDir > SpaceshipMovement.TURN_BRAKE_THRESHOLD_SPEED;
    }

}
