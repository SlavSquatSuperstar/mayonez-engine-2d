package slavsquatsuperstar.mayonez.graphics;

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

    private final float gridWidth, gridHeight;

    public Grid(float gridWidth, float gridHeight){
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(0.3f, 0.3f, 0.3f, 0.5f));

        float camX = getScene().getCamera().getOffset().x;
        float camY = getScene().getCamera().getOffset().y;
//        Logger.log("Cam Offset: %f, %f", camX, camY);

        // Which world coordinates are we at?
        float startX = (float) Math.floor(camX) * gridWidth;
        float startY = (float) Math.floor(camY) * gridHeight;
        float endX = startX + Preferences.SCREEN_WIDTH + gridWidth;
//        float endY = Math.min(startY + Preferences.SCREEN_HEIGHT + gridHeight, ground.getY());
        float endY = startY + Preferences.SCREEN_HEIGHT + gridHeight;
        // Either bottom of screen or top of ground

        // Vertical Lines
        for (float drawX = startX; drawX <= endX; drawX += gridWidth)
            g2.draw(new Line2D.Float(drawX, startY, drawX, endY));

        // Horizontal Lines
        for (float drawY = startY; drawY <= endY; drawY += gridHeight)
            g2.draw(new Line2D.Float(startX, drawY, endX, drawY));
    }
}
