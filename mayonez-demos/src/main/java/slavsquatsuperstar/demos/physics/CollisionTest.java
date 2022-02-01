package slavsquatsuperstar.demos.physics;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Mayonez;

public class CollisionTest extends PhysicsTestScene {

    public CollisionTest(String name) {
        super(name, 0);
    }

    @Override
    protected void init() {
        addObject(createStaticOBB("Ground", new Vec2(getWidth() / 2f, 1), new Vec2(getWidth(), 2), 0));

        addObject(createStaticOBB("Left Ramp", new Vec2(20, 40), new Vec2(20, 4), -40));
        addObject(createStaticOBB("Right Ramp", new Vec2(90, 40), new Vec2(24, 4), 25));

        // Add Other Test Objects
        addObject(createCircle(4, new Vec2(25, 60), PhysicsTestScene.STICKY_MATERIAL));
        addObject(createCircle(5, new Vec2(90, 50), PhysicsTestScene.STICKY_MATERIAL));
        addObject(createCircle(2, new Vec2(10, 30), PhysicsTestScene.STICKY_MATERIAL));
        addObject(createOBB(5, 10, new Vec2(40, 40), -30, PhysicsTestScene.NORMAL_MATERIAL));
        addObject(createOBB(10, 6, new Vec2(70, 30), -45, PhysicsTestScene.NORMAL_MATERIAL));
        addObject(createOBB(3, 15, new Vec2(90, 20), 90, PhysicsTestScene.NORMAL_MATERIAL));
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.setScene(new CollisionTest("Collision Test Scene"));
        Mayonez.start();
    }

}
