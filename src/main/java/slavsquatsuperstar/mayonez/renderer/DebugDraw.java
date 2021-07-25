package slavsquatsuperstar.mayonez.renderer;

import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.primitives.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.BoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.CircleCollider;
import slavsquatsuperstar.mayonez.physics2d.primitives.Collider2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws colliders and mathematical objects onto the screen as shapes. Note: all methods use position as the center.
 */
public class DebugDraw {

    private static final int STROKE_SIZE = 2;
    private static final Stroke stroke = new BasicStroke(STROKE_SIZE);
    private static final List<Renderable> shapes = new ArrayList<>(); // map to color?

    // Points

    public static void drawPoint(Vector2 position, Color color) {
        // Fill a circle with radius "STROKE_SIZE"
        Vector2 min = position;
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.fillOval(toScreen(min.x) - STROKE_SIZE, toScreen(min.y) - STROKE_SIZE, STROKE_SIZE * 2, STROKE_SIZE * 2);
        });
    }

    // Lines

    public static void drawLine(Vector2 start, Vector2 end, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawLine(toScreen(start.x), toScreen(start.y),
                    toScreen(end.x), toScreen(end.y));
        });
    }

    public static void drawVector(Vector2 direction, Vector2 origin, Color color) {
        drawLine(origin, origin.add(direction), color);
//        drawPoint(origin.add(direction), color);
    }

    // Shapes

    public static void drawShape(Collider2D shape, Color color) {
        if (shape instanceof CircleCollider)
            drawCircle((CircleCollider) shape, color);
        else if (shape instanceof AlignedBoxCollider2D)
            drawAABB((AlignedBoxCollider2D) shape, color);
        else if (shape instanceof BoxCollider2D)
            drawBox((BoxCollider2D) shape, color);
    }

    private static void drawCircle(CircleCollider circle, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawOval(toScreen(circle.min().x), toScreen(circle.min().y),
                    toScreen(circle.radius * 2), toScreen(circle.radius * 2));
        });
    }

    private static void drawAABB(AlignedBoxCollider2D aabb, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawRect(toScreen(aabb.min().x), toScreen(aabb.min().y), toScreen(aabb.width()), toScreen(aabb.height()));
        });
    }

    private static void drawBox(BoxCollider2D box, Color color) {
        Polygon obb = new Polygon();
        for (Vector2 point : box.getVertices())
            obb.addPoint(toScreen(point.x), toScreen(point.y));
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawPolygon(obb);
        });
    }

    /**
     * Converts world coordinate to screen coordinates, rounding to the nearest integer.
     *
     * @param world the location of something in the world
     * @return the corresponding screen pixel
     */
    private static int toScreen(float world) {
        return Math.round(world * Game.currentScene().getCellSize());
    }

    public void render(Graphics2D g2) {
        if (!shapes.isEmpty()) {
            g2.setStroke(stroke);
            shapes.forEach(s -> s.draw(g2));
            shapes.clear();
        }
    }

}
