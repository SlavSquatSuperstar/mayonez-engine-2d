package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.MouseInput;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;

/**
 * Allows objects to be pushed around using the mouse.
 *
 * @author SlavSquatSuperstar
 */
public class MouseFlick extends MouseScript {

    private static MouseFlick activeInstance = null; // only want to move one object
    private Vec2 lastMouse = new Vec2();

    public MouseFlick(String button, float speed, boolean inverted) {
        this.button = button;
        this.inverted = inverted;
        this.speed = speed;
        mode = MoveMode.VELOCITY;
    }

    @Override
    public void start() {
        super.start();
        collider = parent.getComponent(Collider2D.class);
    }

    // Overrides

    @Override
    public void onMouseDown() {
        if (activeInstance == null) {
            activeInstance = this;
            lastMouse = MouseInput.getPosition();
            if (rb != null) {
                rb.setVelocity(new Vec2(0, 0)); // Stop the object
                rb.setAngVelocity(0);
            }
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
