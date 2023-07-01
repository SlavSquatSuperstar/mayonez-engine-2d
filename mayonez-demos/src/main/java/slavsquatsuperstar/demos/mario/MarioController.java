package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.*;

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
    public void start() {
        rb = getRigidbody();
        onGround = false;
    }

    @Override
    public void update(float dt) {
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
    public void onCollisionEnter(GameObject other, Vec2 direction) {
        if (other.hasTag("Ground") && isDirectionDownward(direction)) {
            onGround = true;
            rb.getVelocity().y = 0;
        }
    }

    private static boolean isDirectionDownward(Vec2 direction) {
        return direction.dot(new Vec2(0, -1)) > 0;
    }

    @Override
    public void onCollisionExit(GameObject other) {
        if (other.hasTag("Ground")) onGround = false;
    }

}
