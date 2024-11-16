package slavsquatsuperstar.demos.geometrydash;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;

/**
 * Draws gridlines to indicate where the world coordinates are.
 *
 * @author SlavSquatSuperstar
 */
public class DrawGrid extends Component {

    private static final float GRID_LINE_WIDTH = 1f;
    private static final Color GRID_COLOR = Color.grayscale(77, 127);
    private static final ShapeBrush GRID_BRUSH = ShapeBrush.createLineBrush(GRID_COLOR)
            .setStrokeSize(GRID_LINE_WIDTH);

    private Vec2 sceneHalfSize;

    @Override
    protected void start() {
        sceneHalfSize = getScene().getHalfSize();
    }

    @Override
    protected void debugRender() {
        var debugDraw = getScene().getDebugDraw();
        var camPos = getScene().getCamera().getPosition();

        // Which world coordinates are we at?
        var start = camPos.sub(sceneHalfSize).floor().sub(new Vec2(0.5f));
        var end = camPos.add(sceneHalfSize).ceil().add(new Vec2(0.5f));

        // Either bottom of screen or top of ground
        // Vertical Lines
        for (var drawX = start.x; drawX <= end.x; drawX += 1) {
            debugDraw.drawLine(new Vec2(drawX, start.y), new Vec2(drawX, end.y), GRID_BRUSH);
        }
        // Horizontal Lines
        for (var drawY = start.y; drawY <= end.y; drawY += 1) {
            debugDraw.drawLine(new Vec2(start.x, drawY), new Vec2(end.x, drawY), GRID_BRUSH);
        }
    }

}
