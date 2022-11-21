package mayonez.scripts;

import mayonez.input.MouseInput;
import mayonez.math.Vec2;

/**
 * Moves an object with or towards the mouse pointer, depending on the mode.
 *
 * @author SlavSquatSuperstar
 */
public class FollowMouse extends MouseScript {

    public FollowMouse(MoveMode mode, float speed) {
        super(mode, speed);
    }

    @Override
    public void onMouseMove() {
        Vec2 mouseDirection = getRawInput().clampLength(speed);
        switch (mode) {
            case FOLLOW_MOUSE -> transform.position.set(MouseInput.getPosition());
            case POSITION -> transform.move(mouseDirection);
            case VELOCITY -> rb.addVelocity(mouseDirection);
            case IMPULSE -> rb.applyImpulse(mouseDirection);
            case ACCELERATION -> rb.applyAcceleration(mouseDirection);
            case FORCE -> rb.applyForce(mouseDirection);
        }
    }

    @Override
    protected Vec2 getRawInput() {
        return MouseInput.getPosition().sub(transform.position);
    }
}
