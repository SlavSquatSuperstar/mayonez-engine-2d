package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.*;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Draws gridlines to indicate where the world coordinates are.
 *
 * @author SlavSquatSuperstar
 */
public class Grid extends GameObject {

    private GameObject ground;
    private int gridWidth, gridHeight;

    public Grid(Vector2 position) {
        super("Grid", position);
    }

    public Grid(Vector2 position, GameObject ground) {
        super("Grid", position);
        this.ground = ground;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(0.3f, 0.3f, 0.3f, 0.5f));

        float camX = getScene().camera().getOffset().x;
        float camY = getScene().camera().getOffset().y;
        Logger.log("Cam Offset: %f, %f", camX, camY);

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

    @Override
    public GameObject setScene(Scene scene) {
        super.setScene(scene);
        gridWidth = getScene().getCellSize();
        gridHeight = getScene().getCellSize();
        return this;
    }
}
