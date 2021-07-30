package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.mayonez.MouseInput;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;

/**
 * Allows objects to be pushed around using the mouse.
 *
 * @author SlavSquatSuperstar
 */
public class MouseFlick extends MouseScript {

    private Vector2 dragDisplacement = new Vector2();

    public MouseFlick(String button, float speed, boolean inverted) {
        this.button = button;
        this.inverted = inverted;
        this.speed = speed;
        mode = MoveMode.IMPULSE;
    }

    @Override
    public void start() {
        super.start();
        collider = parent.getComponent(Collider2D.class);
    }

    // Overrides


    @Override
    public void onMouseMove() {
        dragDisplacement = dragDisplacement.add(MouseInput.getDisplacement());
    }

    @Override
    public void onMouseUp() {
        rb.addVelocity(getRawInput().clampLength(speed));
        dragDisplacement.set(0, 0);
    }

    @Override
    protected Vector2 getRawInput() {
        return dragDisplacement.mul(inverted ? -1 : 1);
    }

}
