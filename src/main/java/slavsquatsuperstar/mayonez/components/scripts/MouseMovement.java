package slavsquatsuperstar.mayonez.components.scripts;

import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.Vector2;

// TODO make superclass MovementScript
public class MouseMovement extends MovementScript {

    public MouseMovement() {
        mode = Mode.FOLLOW_MOUSE;
    }

    public MouseMovement(float speed, Mode mode) {
        super(speed, mode);
    }

    @Override
    public void update(float dt) {
        Vector2 mouseDirection = getRawInput().clampLength(speed);
        switch (mode) {
            case FOLLOW_MOUSE:
                parent.transform.position.set(Game.mouse().getPosition());
                break;
            case POSITION:
                parent.transform.move(mouseDirection);
                break;
            case IMPULSE:
                rb.addImpulse(mouseDirection);
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
