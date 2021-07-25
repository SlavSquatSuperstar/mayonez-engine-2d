package slavsquatsuperstar.mayonez.renderer;

import slavsquatsuperstar.mayonez.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws all user interface components and overlays onto the screen. Note: all shape methods use position as the top-left.
 */
public class IMGUI {

    private static final int STROKE_SIZE = 4;
    private static final Stroke stroke = new BasicStroke(STROKE_SIZE);
    private static final List<Renderable> shapes = new ArrayList<>(); // map to color?

    public static void drawPoint(Vector2 position, Color color) {
        // Fill a circle with diameter "STROKE_SIZE" centered at "position"
        Vector2 min = position.sub(new Vector2(STROKE_SIZE, STROKE_SIZE).div(2));
        IMGUI.fillCircle(min, STROKE_SIZE * 0.5f, color);
    }

    public static void drawLine(Vector2 start, Vector2 end, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawLine(round(start.x), round(start.y), round(end.x), round(end.y));
        });
    }

    // Shapes

    public static void drawCircle(Vector2 position, float radius, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawOval(round(position.x), round(position.y), round(radius * 2), round(radius * 2));
        });
    }

    public static void fillCircle(Vector2 position, float radius, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.fillOval(round(position.x), round(position.y), round(radius * 2), round(radius * 2));
        });
    }

    public static void drawRect(Vector2 position, float width, float height, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawRect(round(position.x), round(position.y), round(width), round(height));
        });
    }

    public static void fillRect(Vector2 position, float width, float height, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.fillRect(round(position.x), round(position.y), round(width), round(height));
        });
    }

    // Other Methods

    private static int round(float value) {
        return Math.round(value);
    }

    public void render(Graphics2D g2) {
        if (!shapes.isEmpty()) {
            g2.setStroke(stroke);
            shapes.forEach(s -> s.draw(g2));
            shapes.clear();
        }
    }
}
