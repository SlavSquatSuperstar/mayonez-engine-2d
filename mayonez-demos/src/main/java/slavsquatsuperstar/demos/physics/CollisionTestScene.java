package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.math.*;
import mayonez.physics.dynamics.*;

import static slavsquatsuperstar.demos.physics.SandboxObjectPrefabs.*;

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
        var boxMat = new PhysicsMaterial(0.1f, 0.1f, 0.25f);
        addObject(createStaticBox("Box 1",
                new Vec2(0f, -30f), new Vec2(100f, 5f), 0f, boxMat));

        var ballMat = new PhysicsMaterial(0.1f, 0.1f, 0.9f);
        for (var i = 0; i < 5; i++) {
            var pos = new Vec2(-30f + 15f * i, 0f);
            var vel = new Vec2(-4f + 2f * i, -25f);
            addObject(createBall(pos, new Vec2(5f), ballMat).addInitialVelocity(vel));
        }

        // Test box balancing at different angles
        addObject(createStaticBox("Box 2",
                new Vec2(0f, 5f), new Vec2(100f, 5f), 0f, boxMat));

        for (var i = 0; i < 5; i++) {
            var pos = new Vec2(-40f + 20f * i, 20f + i);
            var rot = 15f * i;
            addObject(createBox(pos, new Vec2(10, 5), rot, boxMat));
        }
    }

}
