package slavsquatsuperstar.demos.physics;

import mayonez.DebugDraw;
import mayonez.Preferences;
import mayonez.Scene;
import mayonez.graphics.Colors;
import mayonez.input.KeyInput;
import mayonez.input.MouseInput;
import mayonez.math.Vec2;
import mayonez.physics.Collisions;
import mayonez.physics.shapes.Circle;
import mayonez.physics.shapes.Polygon;
import mayonez.physics.shapes.Rectangle;
import mayonez.physics.shapes.Triangle;

import java.awt.*;


public class DetectionTestScene extends Scene {

    private final Polygon box = new Rectangle(new Vec2(0, 0), new Vec2(15, 18), 0);
    private final Polygon rect = new Rectangle(new Vec2(0, 0), new Vec2(12, 10), 0);
    private final Triangle tri = new Triangle(new Vec2(-7.5f, -5), new Vec2(0, 8), new Vec2(7.5f, -4));
    private final Circle circ = new Circle(new Vec2(0, 0), 8);
    private float rot = 45f;

    public DetectionTestScene(String name) {
        super(name, Preferences.getScreenWidth(), Preferences.getScreenHeight(), 10);
    }

    @Override
    public void onUserRender(Graphics2D g2) {
        DebugDraw.drawLine(new Vec2(0, -20), new Vec2(0, 20), Colors.DARK_GRAY);
        rot -= KeyInput.getAxis("horizontal");
        DebugDraw.drawLine(new Vec2(-20, 0), new Vec2(20, 0), Colors.DARK_GRAY);
        var userShape = tri.translate(MouseInput.getPosition().add(new Vec2(2))).rotate(rot, null);

        DebugDraw.drawShape(box, Colors.GREEN);
        DebugDraw.drawShape(userShape, Colors.GREEN);
        var manifold = Collisions.getContacts(box, userShape);
        if (manifold != null) {
            for (var contact : manifold.getContacts()) {
                DebugDraw.drawPoint(contact, Colors.LIGHT_GREEN);
                DebugDraw.drawVector(contact, manifold.getNormal(), Colors.LIGHT_GREEN);
            }
        }
    }

}
