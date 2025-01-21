package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;

/**
 * A scene for testing collision detection and resolution.
 *
 * @author SlavSquatSupertar
 */
public class CollisionTestScene extends Scene {

    public CollisionTestScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        getCamera().setCameraScale(10f);

        // Test ball bouncing at different angles
        addObject(createStaticBox("Balls Box", new Vec2(0f, -30f), new Vec2(100f, 5f)));

        var ballMat = new PhysicsMaterial(0.1f, 0.1f, 0.9f);
        addObject(createBall("Ball 1", new Vec2(-30f, 0f), new Vec2(-5f, -25f), ballMat));
        addObject(createBall("Ball 2", new Vec2(-15f, 1f), new Vec2(-1f, -25f), ballMat));
        addObject(createBall("Ball 3", new Vec2(0f, 2f), new Vec2(0f, -25f), ballMat));
        addObject(createBall("Ball 4", new Vec2(15f, 3f), new Vec2(1f, -25f), ballMat));
        addObject(createBall("Ball 5", new Vec2(30f, 4f), new Vec2(5f, -25f), ballMat));

    }

    private static GameObject createStaticBox(String name, Vec2 pos, Vec2 size) {
        return new GameObject(name, pos) {
            @Override
            protected void init() {
                addComponent(new BoxCollider(size));
                addComponent(new ShapeSprite(Colors.DARK_GRAY, true));
                addComponent(new Rigidbody(0f));
            }
        };
    }

    private static GameObject createBall(String name, Vec2 pos, Vec2 vel, PhysicsMaterial mat) {
        return new GameObject(name, pos) {
            @Override
            protected void init() {
                addComponent(new BallCollider(new Vec2(5f)));
                addComponent(new ShapeSprite(Colors.BLUE, false));
                addComponent(new DrawPhysicsInformation());
                var rb = new Rigidbody(1f).setMaterial(mat);
                addComponent(rb);
                rb.setVelocity(vel);
            }
        };
    }

}
