package slavsquatsuperstar.mayonez.components.scripts;

import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.MouseInput;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.primitives.Collider2D;

public class DragAndDrop extends Script {

    public boolean inverted;
    private String button;
    private float lastMx, lastMy;
    private Collider2D collider; // Reference to object collider

    public DragAndDrop(String button, boolean inverted) {
        this.button = button;
        this.inverted = inverted;
    }

    public boolean isMouseOnObject() {
        // Using last mouse position is more reliable when mouse is moving fast
        if (collider != null)
            return collider.contains(new Vector2(scene().camera().getX() + lastMx, scene().camera().getY() + lastMy));
        return true;
    }

    @Override
    public void start() {
        collider = parent.getComponent(Collider2D.class);
    }

    @Override
    public void update(float dt) {
        MouseInput mouse = Game.mouse();
        if (isMouseOnObject() && mouse.buttonDown(button)) {
            float dx = (mouse.getX() - lastMx + mouse.getDx()) * (inverted ? -1 : 1);
            float dy = (mouse.getY() - lastMy + mouse.getDy()) * (inverted ? -1 : 1);
            parent.transform.move(new Vector2(dx, dy));
        }
        lastMx = mouse.getX() + mouse.getDx();
        lastMy = mouse.getY() + mouse.getDy();
    }
}
