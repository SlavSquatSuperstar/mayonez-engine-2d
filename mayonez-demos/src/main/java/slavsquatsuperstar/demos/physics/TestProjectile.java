package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;
import mayonez.physics.CollisionEvent;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;
import mayonez.physics.colliders.BulletBoxCollider;

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
        addComponent(new BulletBoxCollider(ProjectileLauncher.PROJ_SIZE) {
            @Override
            protected void init() {
                setLayer(getScene().getLayer(ProjectileTestScene.PROJECTILE_LAYER));
            }

            @Override
            public void onCollisionEvent(CollisionEvent event) {
                if (event.other.getName().equals("Target Box")) {
                    getGameObject().setDestroyed();
                }
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
                protected void fixedUpdate(float dt) {
                    // Add trail
                    getScene().addObject(new GameObject("Trail", transform.copy()) {
                        @Override
                        protected void init() {
                            var collider = new BoxCollider(ProjectileLauncher.PROJ_SIZE);
                            collider.setLayer(getScene().getLayer(ProjectileTestScene.PROJECTILE_LAYER));
                            addComponent(collider);
                            addComponent(new ShapeSprite(Colors.LIGHT_BLUE, false));
                            addComponent(new DestroyAfterDuration(0.5f));
                        }
                    });
                }
            });
        }
    }

}
