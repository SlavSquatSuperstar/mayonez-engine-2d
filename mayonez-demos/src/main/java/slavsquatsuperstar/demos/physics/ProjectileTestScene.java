package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.math.*;
import mayonez.physics.dynamics.*;
import slavsquatsuperstar.demos.DemoScene;

import static slavsquatsuperstar.demos.physics.SandboxObjectPrefabs.createStaticBox;

/**
 * A scene for testing projectile collision.
 *
 * @author SlavSquatSupertar
 */
public class ProjectileTestScene extends DemoScene {

    public ProjectileTestScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        getCamera().setCameraScale(10f);
        setGravity(new Vec2());

        // Add target
        var targetBox = createStaticBox("Target Box",
                new Vec2(50f, 0f), new Vec2(5f, 15f), 0f,
                PhysicsMaterial.DEFAULT_MATERIAL);
        addObject(targetBox);

        // Control target
        addObject(new GameObject("Target Controller") {
            @Override
            protected void init() {
                addComponent(new TargetController(targetBox));
            }
        });

        // Launch projectiles
        addObject(new GameObject("Projectile Launcher", new Vec2(-50f, 0f)) {
            @Override
            protected void init() {
                addComponent(new ProjectileLauncher());
            }
        });
    }

}
