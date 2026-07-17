package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.DemoScene;

/**
 * A scene for testing projectile collision.
 *
 * @author SlavSquatSupertar
 */
public class ProjectileTestScene extends DemoScene {

    public static int PROJECTILE_LAYER = 0;
    public static int TARGET_LAYER = 1;

    public ProjectileTestScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        // Don't make projectiles collide with each other
        var projectileLayer = getLayer(PROJECTILE_LAYER);
        projectileLayer.setName("Projectiles");
        projectileLayer.setLayerInteract(PROJECTILE_LAYER, false);

        var targetLayer = getLayer(TARGET_LAYER);
        targetLayer.setName("Targets");

        getCamera().setCameraScale(10f);
        setGravity(new Vec2());

        // Add controllable target
        addObject(new TargetBox());

        // Launch projectiles
        addObject(new GameObject("Projectile Launcher", new Vec2(-50f, 0f)) {
            @Override
            protected void init() {
                addComponent(new ProjectileLauncher());
            }
        });
    }

}
