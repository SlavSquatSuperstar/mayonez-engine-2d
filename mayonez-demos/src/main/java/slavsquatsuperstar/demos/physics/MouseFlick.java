package slavsquatsuperstar.demos.physics;

import mayonez.math.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.mouse.*;

/**
 * Allows objects to be launched using the mouse. Holding the mouse on an object and dragging
 * zit sets the direction and strength of the flick.
 *
 * @author SlavSquatSuperstar
 */
public abstract class MouseFlick extends MouseInputScript {

    // Static Fields
    private static MouseFlick activeInstance = null; // only want to move one object

    // Instance Fields
    private Vec2 mouseStart;
    private final float speed;
    private Rigidbody rb;

    public MouseFlick(String button, float speed) {
        super(button);
        this.speed = speed;
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
            activeInstance = null;
            if (rb == null) return;
            var dragDisplacement = getMousePos().sub(mouseStart);
            var input = dragDisplacement.clampLength(speed);
            flickGameObject(input, rb);
        }
    }

    /**
     * Move this object after the mouse has been flicked.
     * @param input the mouse input vector
     * @param rb the object's rigidbody
     */
    protected abstract void flickGameObject(Vec2 input, Rigidbody rb);

    @Override
    protected void onDestroy() {
        if (activeInstance == this) {
            activeInstance = null;
        }
    }

}
