package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.physics2d.primitives.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.BoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.CircleCollider;
import slavsquatsuperstar.mayonez.physics2d.primitives.Collider2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// TODO create IMGUI implementation
public class DebugDraw {

    private static final int STROKE_SIZE = 2;
    private static final Stroke stroke = new BasicStroke(STROKE_SIZE);
    private static final List<ShapeDrawer> shapes = new ArrayList<>(); // map to color?

    // Points

    public static void drawPoint(Vector2 position, Color color) {
        Logger.log("Point: %s", position);
        // Fill a circle with radius "STROKE_SIZE"
        Vector2 min = position;
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.fillOval(toScreen(min.x) - STROKE_SIZE, toScreen(min.y) - STROKE_SIZE, STROKE_SIZE * 2, STROKE_SIZE * 2);
        });
    }

    public static void drawPointScreen(Vector2 position, Color color) {
        Logger.log("Point: %s", position);
        // Fill a circle with radius "STROKE_SIZE"
        Vector2 min = position.sub(new Vector2(STROKE_SIZE, STROKE_SIZE));
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.fillOval((int) min.x, (int) min.y, STROKE_SIZE * 2, STROKE_SIZE * 2);
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

    public static void drawLineScreen(Vector2 start, Vector2 end, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
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

    public static void drawShapeScreen(Collider2D shape, Color color) {
        if (shape instanceof CircleCollider)
            drawCircleScreen((CircleCollider) shape, color);
        else if (shape instanceof AlignedBoxCollider2D)
            drawAABBScreen((AlignedBoxCollider2D) shape, color);
        else if (shape instanceof BoxCollider2D)
            drawBoxScreen((BoxCollider2D) shape, color);
    }

    private static void drawCircle(CircleCollider circle, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawOval(toScreen(circle.min().x), toScreen(circle.min().y),
                    toScreen(circle.radius * 2), toScreen(circle.radius * 2));
        });
    }

    private static void drawCircleScreen(CircleCollider circle, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawOval((int) circle.min().x, (int) circle.min().y, (int) (circle.radius * 2), (int) (circle.radius * 2));
        });
    }

    private static void drawAABB(AlignedBoxCollider2D aabb, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawRect(toScreen(aabb.min().x), toScreen(aabb.min().y), toScreen(aabb.width()), toScreen(aabb.height()));
        });
    }

    private static void drawAABBScreen(AlignedBoxCollider2D aabb, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawRect((int) aabb.min().x, (int) aabb.min().y, (int) aabb.width(), (int) aabb.height());
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

    private static void drawBoxScreen(BoxCollider2D box, Color color) {
        Polygon obb = new Polygon();
        for (Vector2 point : box.getVertices()) obb.addPoint((int) point.x, (int) point.y);
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawPolygon(obb);
        });
    }

    private static int toScreen(float world) {
        return Math.round(world * Game.currentScene().getCellSize());
    }

    // Helper Members

    public void render(Graphics2D g2) {
        if (!shapes.isEmpty()) {
            g2.setStroke(stroke);
            shapes.forEach(s -> s.draw(g2));
            shapes.clear();
        }
    }

    @FunctionalInterface
    private interface ShapeDrawer { // Maps a shape to a draw function
        void draw(Graphics2D g2);
    }

}
