package mayonez.scripts;

import mayonez.math.Vec2;
import mayonez.input.MouseInput;

/**
 * Allows objects to be pushed around using the mouse.
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
            lastMouse = MouseInput.getPosition();
        }
    }

    @Override
    public void onMouseUp() {
        if (activeInstance == this) {
            rb.addVelocity(getRawInput().clampLength(speed));
            activeInstance = null;
        }
    }

    @Override
    protected Vec2 getRawInput() {
        Vec2 dragDisplacement = MouseInput.getPosition().sub(lastMouse);
        return dragDisplacement.mul(inverted ? -1 : 1);
    }

}
