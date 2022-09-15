package slavsquatsuperstar.demos.physics;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.graphics.DebugDraw;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics.shapes.Ellipse;
import slavsquatsuperstar.util.Colors;

public class DrawEllipseTest extends Scene {

    private final Ellipse ellipse = new Ellipse(new Vec2(50, 35), new Vec2(30, 20), 45);
    private float angle = 0;

    public DrawEllipseTest(String name) {
        super(name, Preferences.getScreenWidth(), Preferences.getScreenHeight(), 10);
    }

    @Override
    protected void onUserUpdate(float dt) {
        if (KeyInput.keyDown("q")) angle += 1;
        else if (KeyInput.keyDown("e")) angle -= 1;
        Ellipse ellipse = this.ellipse.rotate(angle, null);

        Vec2 mouse = MouseInput.getPosition();
        DebugDraw.drawPoint(mouse, Colors.BLUE);
        DebugDraw.drawLine(ellipse.center(), mouse, Colors.BLUE);
        DebugDraw.drawPoint(ellipse.center().add(ellipse.getRadius(mouse.sub(ellipse.center()))), Colors.LIGHT_GREEN);

        if (ellipse.contains(mouse)) DebugDraw.drawShape(ellipse, Colors.RED);
        else DebugDraw.drawShape(ellipse, Colors.BLACK);

        DebugDraw.drawShape(ellipse.boundingRectangle(), Colors.GRAY);
        Vec2 mouseDir = MouseInput.getPosition().sub(ellipse.center());
        DebugDraw.drawPoint(ellipse.supportPoint(mouseDir), Colors.LIGHT_BLUE);
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.setScene(new DrawEllipseTest("Draw Ellipse Scene"));
        Mayonez.start();
    }
}
