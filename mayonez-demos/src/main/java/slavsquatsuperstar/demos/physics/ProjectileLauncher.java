package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.math.shapes.*;

/**
 * Controls the size, angle, and speed of launched projectiles.
 *
 * @author SlavSquatSupertar
 */
class ProjectileLauncher extends Script {

    static final Vec2 PROJ_SIZE = new Vec2(4f, 2f);
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
                .drawShape(new Rectangle(objPos, PROJ_SIZE.times(size), angle),
                        Colors.GRAY);
    }

    private void launchProjectile() {
        var projXf = new Transform(transform.getPosition(), angle, new Vec2(size));
        var projVel = new Vec2(20f * speed, 0).rotate(angle);
        getScene().addObject(new TestProjectile("Test Projectile", projXf, projVel));
    }

}
