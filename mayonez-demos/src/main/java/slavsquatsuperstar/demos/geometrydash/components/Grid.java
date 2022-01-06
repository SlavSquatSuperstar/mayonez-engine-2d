package slavsquatsuperstar.demos.geometrydash.components;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Component;
import slavsquatsuperstar.mayonez.Preferences;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Draws gridlines to indicate where the world coordinates are.
 *
 * @author SlavSquatSuperstar
 */
public class Grid extends Component {

    private Vec2 gridSize;

    public Grid(Vec2 gridSize) {
        this.gridSize = gridSize;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(0.3f, 0.3f, 0.3f, 0.5f));

        Vec2 cam = getScene().getCamera().getOffset().floor();

        // Which world coordinates are we at?
        Vec2 start = cam.mul(gridSize);
        Vec2 end = start.add(new Vec2(Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT)).add(gridSize);
        // Either bottom of screen or top of ground

        // Vertical Lines
        for (float drawX = start.x; drawX <= end.x; drawX += gridSize.x)
            g2.draw(new Line2D.Float(drawX, start.y, drawX, end.y));

        // Horizontal Lines
        for (float drawY = start.y; drawY <= end.y; drawY += gridSize.y)
            g2.draw(new Line2D.Float(start.x, drawY, end.x, drawY));
    }
}
