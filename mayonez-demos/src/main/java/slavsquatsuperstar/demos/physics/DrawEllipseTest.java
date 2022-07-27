package slavsquatsuperstar.demos.physics;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Colors;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.graphics.DebugDraw;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics.shapes.Ellipse;

public class DrawEllipseTest extends Scene {

    private final Ellipse ellipse = new Ellipse(new Vec2(50, 35), new Vec2(30, 20), 45);


    public DrawEllipseTest(String name) {
        super(name, Preferences.getScreenWidth(), Preferences.getScreenHeight(), 10);
    }

    @Override
    protected void onUserUpdate(float dt) {
        DebugDraw.drawShape(ellipse, Colors.BLUE);
        DebugDraw.drawShape(ellipse.boundingRectangle(), Colors.GRAY);
        Vec2 mouseDir = MouseInput.getWorldPos().sub(ellipse.center());
        DebugDraw.drawPoint(ellipse.supportPoint(mouseDir), Colors.LIGHT_GREEN);
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.setScene(new DrawEllipseTest("Draw Ellipse Scene"));
        Mayonez.start();
    }

}
