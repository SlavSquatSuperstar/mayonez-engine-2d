package slavsquatsuperstar.mayonez.components.scripts;

import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.MouseInput;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Vector2;

public class DragAndDrop extends MouseMovement {

    public DragAndDrop(String button, boolean inverted) {
        super(MoveMode.POSITION, 0);
        this.inverted = inverted;
        this.button = button;
    }

    @Override
    public void onMouseMove() {
        if (isMouseHeld())
            transform.move(getRawInput().div(Preferences.TILE_SIZE));
    }

    @Override
    protected Vector2 getRawInput() {
        MouseInput mouse = Game.mouse();
        return new Vector2(mouse.getX() - lastMx + mouse.getDx(), mouse.getY() - lastMy + mouse.getDy()).mul(inverted ? -1 : 1);
    }
}
