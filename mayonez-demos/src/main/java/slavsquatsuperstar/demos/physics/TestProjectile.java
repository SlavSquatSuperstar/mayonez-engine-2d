package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;

/**
 * A test projectile fired in the scene.
 *
 * @author SlavSquatSupertar
 */
public class TestProjectile extends GameObject {

    private static final boolean DRAW_TRAILS = false;

    private final Vec2 velocity;

    public TestProjectile(String name, Transform transform, Vec2 velocity) {
        super(name, transform);
        this.velocity = velocity;
    }

    @Override
    protected void init() {
        setLayer(getScene().getLayer(ProjectileTestScene.PROJECTILE_LAYER));

        var col = new BoxCollider(ProjectileLauncher.PROJ_SIZE);
        addComponent(col);
        col.addCollisionCallback(event -> {
            if (event.other.getName().equals("Target Box")) {
                col.getGameObject().destroy();
            }
        });

        var rb = new Rigidbody(1);
        addComponent(rb);
        rb.setVelocity(velocity);

        addComponent(new ShapeSprite(Colors.BLUE, false));
        addComponent(new DestroyAfterDuration(5f));

        if (DRAW_TRAILS) {
            addComponent(new Script() {
                @Override
                protected void debugRender() {
                    // Add trail
                    getScene().addObject(new GameObject("Trail", transform.copy()) {
                        @Override
                        protected void init() {
                            setLayer(getScene().getLayer(ProjectileTestScene.PROJECTILE_LAYER));
                            addComponent(new BoxCollider(ProjectileLauncher.PROJ_SIZE));
                            addComponent(new ShapeSprite(Colors.LIGHT_BLUE, false));
                            addComponent(new DestroyAfterDuration(0.5f));
                        }
                    });
                }
            });
        }
    }

}
