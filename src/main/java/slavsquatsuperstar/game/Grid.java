package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.Vector2;

import java.awt.*;
import java.awt.geom.Line2D;

public class Grid extends GameObject {

    private GameObject ground;
    private int gridWidth, gridHeight;

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

        //Which grid coords are we at?
        float startX = (float) Math.floor(camX / gridWidth) * gridWidth;
        float startY = (float) Math.floor(camY / gridHeight) * gridHeight;
        float endX = startX + Preferences.SCREEN_WIDTH + gridWidth;
        float endY = Math.min(startY + Preferences.SCREEN_HEIGHT + gridHeight, ground.getY());
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
