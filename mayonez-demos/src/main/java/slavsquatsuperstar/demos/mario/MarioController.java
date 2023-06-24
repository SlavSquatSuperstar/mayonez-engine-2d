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

    private final float speed = 15;
    private final float jumpSpeed = 20;
    private final float jumpGravity = 15;
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
        transform.move(new Vec2(xInput * speed * dt, 0));
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
            rb.applyImpulse(new Vec2(0, -jumpSpeed * 0.25f));
        }
    }

    // TODO only set grounded if touching top
    @Override
    public void onCollisionEnter(GameObject other) {
        if (other.hasTag("Ground")) {
            onGround = true;
            rb.getVelocity().y = 0;
        }
    }

    @Override
    public void onCollisionExit(GameObject other) {
        if (other.hasTag("Ground")) onGround = false;
    }

}
