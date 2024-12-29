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

        getCollider().addCollisionCallback(event -> {
            // On collision
            if (!event.trigger
                    && event.other.hasLayer(MarioScene.GROUND_LAYER)) {
                if (event.type == CollisionEventType.ENTER
                        && event.direction.dot(new Vec2(0, -1)) > 0) {
                    // Direction is downward
                    onTouchGround();
                } else if (event.type == CollisionEventType.EXIT) {
                    onLeaveGround();
                }
            }
        });
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

        // Ground pound
        if (KeyInput.keyDown("s") && !onGround) {
            rb.applyImpulse(new Vec2(0, -SLAM_SPEED));
        }
    }

    private void onTouchGround() {
        if (!onGround) {
            onGround = true;
            rb.getVelocity().y = 0;
        }
    }

    private void onLeaveGround() {
        if (onGround) onGround = false;
    }

}
