package slavsquatsuperstar.mayonez.components.scripts;

import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.Vector2;

/**
 * A simple script that moves an object with or towards the mouse pointer, depending on the mode.
 */
public class FollowMouse extends MouseMovement {

    public FollowMouse(MoveMode mode, float speed) {
        super(mode, speed);
    }

    @Override
    public void onMouseMove() {
        Vector2 mouseDirection = getRawInput().clampLength(speed);
        switch (mode) {
            case FOLLOW_MOUSE:
                parent.transform.position.set(Game.mouse().getPosition());
                break;
            case POSITION:
                parent.transform.move(mouseDirection);
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
        return Game.mouse().getPosition().sub(parent.transform.position);
    }
}
