package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.physics2d.primitives.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.BoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.CircleCollider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DebugDraw {

    private static final int strokeSize = 2;
    private static final Stroke stroke = new BasicStroke(strokeSize);
    private static final List<ShapeDrawer> shapes = new ArrayList<>(); // Use Map

    public static void drawAABB(AlignedBoxCollider2D aabb, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawRect((int) aabb.min().x, (int) aabb.min().y, (int) aabb.width(), (int) aabb.height());
        });
    }

    public static void drawBox2D(BoxCollider2D box, Color color) {
        Polygon obb = new Polygon();
        for (Vector2 point : box.getVertices()) obb.addPoint((int) point.x, (int) point.y);
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawPolygon(obb);
        });
    }

    public static void drawCircle(CircleCollider circle, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawOval((int) circle.min().x, (int) circle.min().y, (int) (circle.radius * 2f), (int) (circle.radius * 2f));
        });
    }

    public static void drawLine(Vector2 start, Vector2 end, Color color) {
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
        });
    }

    public static void drawPoint(Vector2 position, Color color) {
        CircleCollider point = new CircleCollider(strokeSize);
        point.setTransform(new Transform(position));
        shapes.add(g2 -> {
            g2.setColor(color);
            g2.fillOval((int) point.min().x, (int) point.min().y, (int) (point.radius * 2f), (int) (point.radius * 2f));
        });
    }

    public static void drawVector(Vector2 vector, Vector2 origin, Color color) {
        drawLine(origin, origin.add(vector), color);
//        drawPoint(origin.add(vector), color);
    }

    public void render(Graphics2D g2) {
        g2.setStroke(stroke);
        shapes.forEach(s -> s.draw(g2));
        shapes.clear();
    }

    @FunctionalInterface
    private interface ShapeDrawer { // use lambdas to avoid if statements
        void draw(Graphics2D g2);
    }

}
