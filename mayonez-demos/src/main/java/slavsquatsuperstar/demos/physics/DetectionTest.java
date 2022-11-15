package slavsquatsuperstar.demos.physics;

import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.math.Vec2;
import slavsquatsuperstar.mayonez.physics.collision.Collisions;
import slavsquatsuperstar.mayonez.physics.collision.Manifold;
import slavsquatsuperstar.mayonez.physics.shapes.Circle;
import slavsquatsuperstar.mayonez.physics.shapes.Polygon;
import slavsquatsuperstar.mayonez.physics.shapes.Rectangle;
import slavsquatsuperstar.mayonez.physics.shapes.Shape;
import slavsquatsuperstar.mayonez.physics.shapes.Triangle;
import slavsquatsuperstar.mayonez.util.Colors;
import slavsquatsuperstar.mayonez.util.DebugDraw;

import java.awt.*;


public class DetectionTest extends Scene {

    private final Polygon box = new Rectangle(new Vec2(0, 0), new Vec2(15, 18), 0);
    private final Polygon rect = new Rectangle(new Vec2(0, 0), new Vec2(12, 10), 0);
    private final Triangle tri = new Triangle(new Vec2(-7.5f, -5), new Vec2(0, 8), new Vec2(7.5f, -4));
    private final Circle circ = new Circle(new Vec2(0, 0), 8);
    private float rot = 45f;

    public DetectionTest(String name) {
        super(name, Preferences.getScreenWidth(), Preferences.getScreenHeight(), 10);
    }

    @Override
    protected void onUserRender(Graphics2D g2) {
        DebugDraw.drawLine(new Vec2(0, -20), new Vec2(0, 20), Colors.DARK_GRAY);
        rot -= KeyInput.getAxis("horizontal");
        DebugDraw.drawLine(new Vec2(-20, 0), new Vec2(20, 0), Colors.DARK_GRAY);
        Shape userShape = tri.translate(MouseInput.getPosition().add(new Vec2(2))).rotate(rot, null);

        DebugDraw.drawShape(box, Colors.GREEN);
        DebugDraw.drawShape(userShape, Colors.GREEN);
        Manifold manifold = Collisions.getContacts(box, userShape);
        if (manifold != null) {
            for (Vec2 contact : manifold.getContacts()) {
                DebugDraw.drawPoint(contact, Colors.LIGHT_GREEN);
                DebugDraw.drawVector(contact, manifold.getNormal(), Colors.LIGHT_GREEN);
            }
        }
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.setScene(new DetectionTest("Detection Test Scene"));
        Mayonez.start();
    }
}
