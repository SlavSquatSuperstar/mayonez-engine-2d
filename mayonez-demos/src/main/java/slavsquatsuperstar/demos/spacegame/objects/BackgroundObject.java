package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.shapes.*;

/**
 * A background object in outer space.
 *
 * @author SlavSquatSuperstar
 */
public class BackgroundObject  {

    private final String name;
    private final Shape shape;
    private final Color color;

    public BackgroundObject(String name, Shape shape, Color color) {
        this.name = name;
        this.shape = shape;
        this.color = color;
    }

    public void debugDraw(DebugDraw debugDraw) {
        debugDraw.fillShape(shape, color);
    }

    @Override
    public String toString() {
        return "Backgorund %s".formatted(name);
    }
}
