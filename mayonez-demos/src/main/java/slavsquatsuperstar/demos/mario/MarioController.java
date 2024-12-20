package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.dynamics.*;

/**
 * Provides basic move, jump, and ground pound controls for Mario.
 *
 * @author SlavSquatSuperstar
 */
public class MarioController extends Script {

    private static final float MOVE_SPEED = 15;
    private static final float JUMP_SPEED = 30;
    private static final float JUMP_GRAVITY = 30;
    private static final float SLAM_SPEED = 5;
    private boolean onGround;
    private Rigidbody rb;

    @Override
    protected void start() {
        rb = getRigidbody();
        onGround = false;
    }

    @Override
    protected void update(float dt) {
        // Move
        var xInput = KeyInput.getAxis("horizontal");
        transform.move(new Vec2(xInput * MOVE_SPEED * dt, 0));
        rb.getVelocity().x = 0;

        // Jump
        if (KeyInput.keyDown("w") && onGround) {
            rb.applyImpulse(new Vec2(0, JUMP_SPEED));
        }
        if (!onGround) { // Fall faster while jumping
            rb.addVelocity(new Vec2(0, -JUMP_GRAVITY * dt));
        }

        // Ground pound
        if (KeyInput.keyDown("s") && !onGround) {
            rb.applyImpulse(new Vec2(0, -SLAM_SPEED));
        }
    }

    @Override
    protected void onCollisionEnter(GameObject other, Vec2 direction, Vec2 velocity) {
        if (other.hasLayer(MarioScene.GROUND_LAYER) && isDirectionDownward(direction)) {
            onGround = true;
            rb.getVelocity().y = 0;
        }
    }

    private static boolean isDirectionDownward(Vec2 direction) {
        return direction.dot(new Vec2(0, -1)) > 0;
    }

    @Override
    protected void onCollisionExit(GameObject other) {
        if (other.hasLayer(MarioScene.GROUND_LAYER)) {
            onGround = false;
        }
    }

}
