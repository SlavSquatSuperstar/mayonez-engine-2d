package slavsquatsuperstar.demos.physics.scripts;

import mayonez.math.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.mouse.*;
import mayonez.scripts.movement.*;

/**
 * Allows objects to be launched using the mouse. Holding the mouse on an object and dragging
 * zit sets the direction and strength of the flick.
 *
 * @author SlavSquatSuperstar
 */
public class MouseFlick extends MouseInputScript {

    // Static Fields
    private static MouseFlick activeInstance = null; // only want to move one object

    // Mouse Fields
    private final boolean inverted;
    private Vec2 mouseStart;

    // Movement Fields
    private final float speed;
    private final MoveMode mode;
    private Rigidbody rb;

    public MouseFlick(String button, float speed, MoveMode mode, boolean inverted) {
        super(button);
        this.speed = speed;
        this.mode = mode;
        this.inverted = inverted;
    }

    // Overrides

    @Override
    protected void start() {
        super.start();
        rb = getRigidbody();
        mouseStart = new Vec2();
    }

    @Override
    public void onMouseDown() {
        if (activeInstance == null) {
            activeInstance = this;
            mouseStart = getMousePos();
        }
    }

    @Override
    public void onMouseUp() {
        if (activeInstance == this) {
            flickGameObject();
            activeInstance = null;
        }
    }

    private void flickGameObject() {
        if (rb == null) return;
        var input = getDragDisplacement().clampLength(speed);
        switch (mode) {
            case VELOCITY -> rb.addVelocity(input);
            case IMPULSE -> rb.applyImpulse(input);
        }
    }

    private Vec2 getDragDisplacement() {
        var dragDisplacement = getMousePos().sub(mouseStart);
        return dragDisplacement.mul(inverted ? -1 : 1);
    }

    @Override
    protected void onDestroy() {
        if (activeInstance == this) {
            activeInstance = null;
        }
    }

}
