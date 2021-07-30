package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.mayonez.MouseInput;
import slavsquatsuperstar.math.Vec2;

/**
 * Allows objects to be picked up using the mouse.
 *
 * @author SlavSquatSuperstar
 */
// Issue: activating for multiple objects
// Solution: use static boolean active?
public class DragAndDrop extends MouseScript {

    public DragAndDrop(String button, boolean inverted) {
        super(MoveMode.POSITION, 0);
        this.inverted = inverted;
        this.button = button;
    }

    @Override
    public void onMouseMove() {
        if (isMouseHeld())
            transform.move(getRawInput());
    }

    @Override
    protected Vec2 getRawInput() {
        MouseInput mouse = MouseInput.INSTANCE;
        return new Vec2(mouse.getX() - lastMx + mouse.getDx(), mouse.getY() - lastMy + mouse.getDy()).mul(inverted ? -1 : 1);
    }
}
