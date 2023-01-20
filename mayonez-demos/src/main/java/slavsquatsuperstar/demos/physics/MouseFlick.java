package slavsquatsuperstar.demos.physics;

import mayonez.math.Vec2;
import mayonez.scripts.movement.MouseScript;
import mayonez.scripts.movement.MoveMode;

/**
 * Allows objects to be given a velocity or impulse using the mouse. Holding the mouse on an object
 * and dragging it sets the direction and strength of the flick.
 *
 * @author SlavSquatSuperstar
 */
public class MouseFlick extends MouseScript {

    private static MouseFlick activeInstance = null; // only want to move one object
    private Vec2 lastMouse = new Vec2();

    public MouseFlick(MoveMode mode, String button, float speed, boolean inverted) {
        this.mode = mode;
        this.button = button;
        this.inverted = inverted;
        this.speed = speed;
    }

    // Overrides

    @Override
    public void onMouseDown() {
        if (activeInstance == null) {
            activeInstance = this;
            lastMouse = getMousePos();
        }
    }

    @Override
    public void onMouseUp() {
        if (activeInstance == this) {
            Vec2 input = getUserInput().clampLength(speed);
            switch (mode) {
                case VELOCITY -> rb.addVelocity(input);
                case IMPULSE -> rb.applyImpulse(input);
            }
            activeInstance = null;
        }
    }

    @Override
    protected Vec2 getUserInput() {
        Vec2 dragDisplacement = getMousePos().sub(lastMouse);
        return dragDisplacement.mul(inverted ? -1 : 1);
    }

}
