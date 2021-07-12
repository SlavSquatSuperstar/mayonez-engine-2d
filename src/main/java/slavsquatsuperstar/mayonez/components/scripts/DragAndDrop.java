package slavsquatsuperstar.mayonez.components.scripts;

import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.MouseInput;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.primitives.Collider2D;

public class DragAndDrop extends Script {

    public boolean inverted;
    private Collider2D collider; // Reference to object collider
    private float lastMx, lastMy;
    private String button;
    private boolean mouseHeld;

    public DragAndDrop(String button, boolean inverted) {
        this.button = button;
        this.inverted = inverted;
    }

    @Override
    public void start() {
        collider = parent.getComponent(Collider2D.class);
    }

    @Override
    public void update(float dt) {
        MouseInput mouse = Game.mouse();
        if (!mouseHeld) {
            // If the mouse is pressed while on this object, start moving, even if the pointer comes off.
            if (isMouseOnObject() && mouse.buttonDown(button))
                mouseHeld = true;
        } else {
            // Once the mouse is released, stop moving
            if (!mouse.buttonDown(button))
                mouseHeld = false;
            float dx = (mouse.getX() - lastMx + mouse.getDx()) * (inverted ? -1 : 1);
            float dy = (mouse.getY() - lastMy + mouse.getDy()) * (inverted ? -1 : 1);
            transform.move(new Vector2(dx, dy));
        }

        lastMx = mouse.getX() + mouse.getDx();
        lastMy = mouse.getY() + mouse.getDy();
    }

    // Getters and Setters
    // TODO Use MouseInput detection instead
    public boolean isMouseOnObject() {
        // Using last mouse position is more reliable when mouse is moving fast
        if (collider != null)
            return collider.contains(new Vector2(scene().camera().getX() + lastMx, scene().camera().getY() + lastMy));
        return true;
    }

}
