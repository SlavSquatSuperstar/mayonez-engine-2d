package slavsquatsuperstar.demos.physics;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Colors;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.graphics.DebugDraw;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics.collision.Collisions;
import slavsquatsuperstar.mayonez.physics.shapes.Polygon;
import slavsquatsuperstar.mayonez.physics.shapes.Triangle;

import java.awt.*;


public class GJKIntersectionTest extends Scene {

    private Polygon rect = Polygon.rectangle(new Vec2(80, 40), new Vec2(15, 15)).rotate(0, null);
    private Triangle tri = new Triangle(new Vec2(0, 0), new Vec2(10, 15), new Vec2(20, 0));

    public GJKIntersectionTest(String name) {
        super(name, Preferences.getScreenWidth(), Preferences.getScreenHeight(), 10);
    }

    @Override
    protected void onUserRender(Graphics2D g2) {
        Triangle newTri = tri.translate(MouseInput.getPosition());
        boolean colliding = Collisions.detectCollision(rect, newTri);
        DebugDraw.drawShape(rect, colliding ? Colors.RED : Colors.BLUE);
        DebugDraw.drawShape(newTri, colliding ? Colors.RED : Colors.LIGHT_GREEN);
        Collisions.getCollisionInfo(rect, newTri);
        DebugDraw.drawLine(new Vec2(50, 20), new Vec2(50, 60), Colors.BLACK);
        DebugDraw.drawLine(new Vec2(30, 40), new Vec2(70, 40), Colors.BLACK);
//        DebugDraw.drawShape(rect, Colors.BLUE);
//        DebugDraw.drawShape(newTri, Colors.LIGHT_GREEN);
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.setScene(new GJKIntersectionTest("GJK Test Scene"));
        Mayonez.start();
    }
}
