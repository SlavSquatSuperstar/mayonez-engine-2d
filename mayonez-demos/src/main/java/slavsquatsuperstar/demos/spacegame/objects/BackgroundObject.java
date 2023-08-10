package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.shapes.*;

/**
 * A background object in outer space.
 *
 * @author SlavSquatSuperstar
 */
public class BackgroundObject {

    private final Shape shape;
    private final ShapeBrush brush;

    public BackgroundObject(Shape shape, Color color, int zIndex) {
        this.shape = shape;
        brush = ShapeBrush.createSolidBrush(color).setZIndex(zIndex);
    }

    public void debugDraw(DebugDraw debugDraw) {
        debugDraw.fillShape(shape, brush);
    }

}
