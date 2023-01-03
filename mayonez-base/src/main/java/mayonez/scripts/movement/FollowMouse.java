package mayonez.scripts.movement;

import mayonez.input.MouseInput;
import mayonez.math.Vec2;

/**
 * Moves an object with or towards the mouse pointer, depending on the mode.
 *
 * @author SlavSquatSuperstar
 */
public class FollowMouse extends MouseScript {

    public boolean rotate;

    public FollowMouse(MoveMode mode, float speed, boolean rotate) {
        super(mode, speed);
        this.rotate = rotate;
    }

    @Override
    public void onMouseMove() {
        Vec2 mouseDirection = getRawInput().clampLength(speed);
//        if (rotate) {
//            float angle = transform.getUp().angle(mouseDirection);
//            transform.rotate(angle);
//        }
        switch (mode) {
            case FOLLOW_MOUSE -> transform.setPosition(MouseInput.getPosition());
            case POSITION -> transform.move(mouseDirection);
            case VELOCITY -> rb.addVelocity(mouseDirection);
            case IMPULSE -> rb.applyImpulse(mouseDirection);
            case ACCELERATION -> rb.applyAcceleration(mouseDirection);
            case FORCE -> rb.applyForce(mouseDirection);
        }
    }

    @Override
    protected Vec2 getRawInput() {
        return MouseInput.getPosition().sub(transform.getPosition());
    }
}
