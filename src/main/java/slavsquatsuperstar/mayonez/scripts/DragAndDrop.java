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

    private static DragAndDrop activeInstance = null; // only want to move one object

    public DragAndDrop(String button, boolean inverted) {
        super(MoveMode.POSITION, 0);
        this.inverted = inverted;
        this.button = button;
    }

    @Override
    public void onMouseDown() {
        if (activeInstance == null)
            activeInstance = this;
    }

    @Override
    public void onMouseMove() {
        if (activeInstance == this && isMouseHeld())
                transform.move(getRawInput());
    }

    @Override
    public void onMouseUp() {
        if (activeInstance == this)
            activeInstance = null;
    }

    @Override
    protected Vec2 getRawInput() {
        return new Vec2(MouseInput.getX() - lastMx + MouseInput.getDx(), MouseInput.getY() - lastMy + MouseInput.getDy()).mul(inverted ? -1 : 1);
    }
}
