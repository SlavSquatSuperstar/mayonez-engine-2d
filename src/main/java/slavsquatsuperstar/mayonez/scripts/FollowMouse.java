package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.mayonez.MouseInput;
import slavsquatsuperstar.mayonez.Vector2;

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
        Vector2 mouseDirection = getRawInput().clampLength(speed);
        switch (mode) {
            case FOLLOW_MOUSE:
                transform.position.set(MouseInput.getPosition());
                break;
            case POSITION:
                transform.move(mouseDirection);
                break;
            case VELOCITY:
                rb.addVelocity(mouseDirection);
                break;
            case IMPULSE:
                rb.addImpulse(mouseDirection);
                break;
            case ACCELERATION:
                rb.addAcceleration(mouseDirection);
                break;
            case FORCE:
                rb.addForce(mouseDirection);
                break;
        }
    }

    @Override
    protected Vector2 getRawInput() {
        return MouseInput.getPosition().sub(transform.position);
    }
}
