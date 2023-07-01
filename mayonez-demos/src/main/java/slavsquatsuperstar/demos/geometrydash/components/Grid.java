package slavsquatsuperstar.demos.geometrydash.components;

import mayonez.Component;
import mayonez.graphics.JRenderable;
import mayonez.math.Vec2;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Draws gridlines to indicate where the world coordinates are.
 *
 * @author SlavSquatSuperstar
 */
// TODO doesn't work in GL
public class Grid extends Component implements JRenderable {

    private final Vec2 gridSize;

    public Grid(Vec2 gridSize) {
        this.gridSize = gridSize;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(0.3f, 0.3f, 0.3f, 0.5f));

        var camPos = getScene().getCamera().getPosition();
        var screneHalfSize = getScene().getSize().mul(0.5f);

        // Which world coordinates are we at?
        var start = camPos.sub(screneHalfSize).floor().sub(new Vec2(0.5f)).mul(gridSize);
        var end = camPos.add(screneHalfSize).ceil().add(new Vec2(0.5f)).mul(gridSize);

        // Either bottom of screen or top of ground
        // Vertical Lines
        for (var drawX = start.x; drawX <= end.x; drawX += gridSize.x)
//            getScene().getDebugDraw().drawLine(new Vec2(drawX, start.y), new Vec2(drawX, end.y), gridColor);
            g2.draw(new Line2D.Float(drawX, start.y, drawX, end.y));

        // Horizontal Lines
        for (var drawY = start.y; drawY <= end.y; drawY += gridSize.y)
//            getScene().getDebugDraw().drawLine(new Vec2(start.x, drawY), new Vec2(end.x, drawY), gridColor);
            g2.draw(new Line2D.Float(start.x, drawY, end.x, drawY));
    }

    @Override
    public int getZIndex() {
        return gameObject.getZIndex();
    }
}
