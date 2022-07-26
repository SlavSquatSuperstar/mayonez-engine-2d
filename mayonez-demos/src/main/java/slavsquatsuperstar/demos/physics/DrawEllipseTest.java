package slavsquatsuperstar.demos.physics;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature;
import slavsquatsuperstar.mayonez.physics.shapes.Ellipse;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.graphics.DebugDraw;
import slavsquatsuperstar.mayonez.input.MouseInput;

@ExperimentalFeature
public class DrawEllipseTest extends Scene {

    private final Ellipse ellipse = new Ellipse(new Vec2(50, 35), new Vec2(20, 30), 0);

    public DrawEllipseTest(String name) {
        super(name, Preferences.getScreenWidth(), Preferences.getScreenHeight(), 10);
    }

    @Override
    protected void onUserUpdate(float dt) {
        DebugDraw.drawShape(ellipse, Colors.BLUE);
        Vec2 mouse = MouseInput.getWorldPos();

        if (ellipse.contains(mouse))
            DebugDraw.drawPoint(mouse, Colors.GREEN);
        else
            DebugDraw.drawPoint(mouse, Colors.RED);
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.setScene(new DrawEllipseTest("Draw Ellipse Scene"));
        Mayonez.start();
    }

}
