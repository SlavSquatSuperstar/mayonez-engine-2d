package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;

/**
 * Controls the size, angle, and speed of launched projectiles.
 *
 * @author SlavSquatSupertar
 */
class ProjectileLauncher extends Script {

    private final Vec2 projSize = new Vec2(4f, 2f);
    private float angle, speed, size;

    @Override
    protected void start() {
        angle = 0;
        speed = 1;
        size = 1;
    }

    @Override
    protected void update(float dt) {
        var yInput = KeyInput.getAxis("vertical");
        speed += 5f * yInput * dt;
        if (speed < 0f) speed = 0f;

        var xInput = KeyInput.getAxis("horizontal");
        angle += -90f * xInput * dt;

        var x2Input = KeyInput.getAxis("horizontal2");
        size *= 1 + 0.5f * x2Input * dt;

        if (KeyInput.keyPressed("space")) {
            launchProjectile();
        }
    }

    @Override
    protected void debugRender() {
        var velocity = new Vec2(speed, 0f).rotate(angle);
        var objPos = transform.getPosition();

        getScene().getDebugDraw()
                .drawPoint(objPos, Colors.BLACK);
        getScene().getDebugDraw()
                .drawVector(objPos, velocity, Colors.BLACK);
        getScene().getDebugDraw()
                .drawShape(new Rectangle(objPos, projSize.times(size), angle),
                        Colors.GRAY);
    }

    private void launchProjectile() {
        var projXf = new Transform(transform.getPosition(), angle, new Vec2(size));
        getScene().addObject(new GameObject("Test Projectile", projXf) {
            @Override
            protected void init() {
                var col = new BoxCollider(projSize);
                addComponent(col);
                col.addCollisionCallback(event -> {
                    if (event.other.getName().equals("Target Box")) {
                        col.getGameObject().destroy();
                    }
                });

                var rb = new Rigidbody(1);
                addComponent(rb);
                rb.setVelocity(new Vec2(20f * speed, 0).rotate(angle));

                addComponent(new ShapeSprite(Colors.BLUE, false));
                addComponent(new DestroyAfterDuration(5f));
            }
        });
    }

}
