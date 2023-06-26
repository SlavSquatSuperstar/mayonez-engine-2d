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

    private final float moveSpeed = 15;
    private final float jumpSpeed = 30;
    private final float jumpGravity = 30;
    private final float slamSpeed = 5;
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
        transform.move(new Vec2(xInput * moveSpeed * dt, 0));
        rb.getVelocity().x = 0;

        // Jump
        if (KeyInput.keyDown("w") && onGround) {
            rb.applyImpulse(new Vec2(0, jumpSpeed));
        }
        if (!onGround) { // Fall faster while jumping
            rb.addVelocity(new Vec2(0, -jumpGravity * dt));
        }

        // Ground pound
        if (KeyInput.keyDown("s") && !onGround) {
            rb.applyImpulse(new Vec2(0, -slamSpeed));
        }
    }

    @Override
    public void onCollisionEnter(GameObject other, Vec2 direction) {
        if (other.hasTag("Ground") && isDirectionDownward(direction)) {
            onGround = true;
            rb.getVelocity().y = 0;
            Logger.log("touchdown");
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
