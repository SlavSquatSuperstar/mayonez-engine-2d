package slavsquatsuperstar.mayonez.components.scripts;

import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.Vector2;

public class KeyMovement extends MovementScript {

    public KeyMovement(Mode mode, float speed) {
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
    }

    @Override
    protected Vector2 getRawInput() {
        return new Vector2(Game.keyboard().getAxis("horizontal"), Game.keyboard().getAxis("vertical"));
    }

}
