package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.dynamics.*;

/**
 * Provides basic move, jump, and ground pound controls for Mario.
 *
 * @author SlavSquatSuperstar
 */
class MarioController extends Script {

    // Constants
    private static final float MOVE_SPEED = 15;
    private static final float JUMP_SPEED = 35;
    private static final float SLAM_SPEED = 5;

    // Fields
    private boolean onGround;
    private Rigidbody rb;

    MarioController() {
        super(UpdateOrder.INPUT);
    }

    @Override
    protected void start() {
        rb = getRigidbody();
        onGround = false;
    }

    @Override
    protected void fixedUpdate(float dt) {
        // Move
        var xInput = KeyInput.getAxis("horizontal");
        transform.move(new Vec2(xInput * MOVE_SPEED * dt, 0));
        rb.getVelocity().x = 0;

        // Jump
        if (KeyInput.keyDown("w") && onGround) {
            rb.applyImpulse(new Vec2(0, JUMP_SPEED));
        }

        // Ground pound
        if (KeyInput.keyDown("s") && !onGround) {
            rb.applyImpulse(new Vec2(0, -SLAM_SPEED));
        }
    }

    public void onTouchGround() {
        if (!onGround) {
            onGround = true;
            rb.getVelocity().y = 0;
        }
    }

    public void onLeaveGround() {
        if (onGround) onGround = false;
    }

}
