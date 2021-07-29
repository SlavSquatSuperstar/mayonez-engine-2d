package slavsquatsuperstar.mayonez.renderer;

import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.primitives.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws colliders and mathematical objects onto the screen. All methods use world coordinates, and shapes are centered
 * around the collider's position.
 */
public class DebugDraw {

    private static final int STROKE_SIZE = 2;
    private static final Stroke stroke = new BasicStroke(STROKE_SIZE);
    private static final List<Renderable> shapes = new ArrayList<>(); // map to color?

    // Points

    /**
     * Draws a point onto the screen
     *
     * @param position where the point is located, in world coordinates
     * @param color    the color to use
     */
    public static void drawPoint(Vector2 position, Color color) {
        float radiusPx = STROKE_SIZE; // Fill a circle with radius "STROKE_SIZE"
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.fill(new Ellipse2D.Float(toScreen(position.x) - radiusPx, toScreen(position.y) - radiusPx, radiusPx * 2, radiusPx * 2));
        });
    }

    // Lines

    /**
     * Draws a line segment onto the screen.
     *
     * @param start the segment's starting point, in world coordinates
     * @param end   the segment's ending point in, world coordinates
     * @param color the color to use
     */
    public static void drawLine(Vector2 start, Vector2 end, Color color) {
        Vector2 startPx = toScreen(start);
        Vector2 endPx = toScreen(end);
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.draw(new Line2D.Float(startPx.x, startPx.y, endPx.x, endPx.y));
        });
    }

    /**
     * Draws a vector onto the screen.
     *
     * @param origin    the vector's starting point, in world coordinates
     * @param direction how far away the vector's end point is, in world coordinates
     * @param color     the color to use
     */
    public static void drawVector(Vector2 origin, Vector2 direction, Color color) {
        drawLine(origin, origin.add(direction), color);
//        drawPoint(origin.add(direction), color); // draw the "arrowhead"
    }

    // Shapes (Outline)

    /**
     * Draws a shape onto the screen.
     *
     * @param shape a {@link Collider2D} instance
     * @param color the color to use
     */
    public static void drawShape(Collider2D shape, Color color) {
        if (shape instanceof CircleCollider)
            drawCircle((CircleCollider) shape, color);
        else if (shape instanceof AlignedBoxCollider2D)
            drawAABB((AlignedBoxCollider2D) shape, color);
        else if (shape instanceof BoxCollider2D)
            drawBox((BoxCollider2D) shape, color);
        else if (shape instanceof Edge2D)
            drawLine(((Edge2D) shape).start, ((Edge2D) shape).end, color);
    }

    private static void drawCircle(CircleCollider circle, Color color) {
        Vector2 minPx = toScreen(circle.min());
        float diameterPx = toScreen(circle.radius * 2);
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.draw(new Ellipse2D.Float(minPx.x, minPx.y, diameterPx, diameterPx));
        });
    }

    private static void drawAABB(AlignedBoxCollider2D aabb, Color color) {
        Vector2 minPx = toScreen(aabb.min());
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.draw(new Rectangle2D.Float(minPx.x, minPx.y, toScreen(aabb.width()), toScreen(aabb.height())));
        });
    }

    private static void drawBox(BoxCollider2D box, Color color) {
        Polygon obb = new Polygon();
        for (Vector2 point : box.getVertices())
            obb.addPoint(Math.round(toScreen(point.x)), Math.round(toScreen(point.y)));
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawPolygon(obb);
        });
    }

    // Shapes (Filled)

    /**
     * Fills in a shape onto the screen.
     *
     * @param shape a {@link Collider2D} instance
     * @param color the color to use
     */
    public static void fillShape(Collider2D shape, Color color) {
        if (shape instanceof CircleCollider)
            fillCircle((CircleCollider) shape, color);
        else if (shape instanceof AlignedBoxCollider2D)
            fillAABB((AlignedBoxCollider2D) shape, color);
        else if (shape instanceof BoxCollider2D)
            fillBox((BoxCollider2D) shape, color);
        else if (shape instanceof Edge2D)
            drawLine(((Edge2D) shape).start, ((Edge2D) shape).end, color);
    }

    private static void fillCircle(CircleCollider circle, Color color) {
        Vector2 minPx = toScreen(circle.min());
        float diameterPx = toScreen(circle.radius * 2);
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.fill(new Ellipse2D.Float(minPx.x, minPx.y, diameterPx, diameterPx));
        });
    }

    private static void fillAABB(AlignedBoxCollider2D aabb, Color color) {
        Vector2 minPx = toScreen(aabb.min());
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.fill(new Rectangle2D.Float(minPx.x, minPx.y, toScreen(aabb.width()), toScreen(aabb.height())));
        });
    }

    private static void fillBox(BoxCollider2D box, Color color) {
        Polygon obb = new Polygon();
        for (Vector2 point : box.getVertices())
            obb.addPoint(Math.round(toScreen(point.x)), Math.round(toScreen(point.y)));
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.fillPolygon(obb);
        });
    }

    /**
     * Converts a world coordinate to screen coordinates.
     *
     * @param world the location of something in the world along an axis
     * @return the corresponding screen pixel
     */
    private static float toScreen(float world) {
        return world * Game.currentScene().getCellSize();
    }

    /**
     * Converts a world position to screen coordinates.
     *
     * @param world the location of something in the world along both axes
     * @return the corresponding screen pixels
     */
    private static Vector2 toScreen(Vector2 world) {
        return world.mul(Game.currentScene().getCellSize());
    }

    public void render(Graphics2D g2) {
        if (!shapes.isEmpty()) {
            g2.setStroke(stroke);
            shapes.forEach(s -> s.draw(g2));
            shapes.clear();
        }
    }

}
