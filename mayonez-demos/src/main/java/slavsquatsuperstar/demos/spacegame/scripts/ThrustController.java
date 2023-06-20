package slavsquatsuperstar.demos.spacegame.scripts;

import mayonez.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.scripts.movement.*;

/**
 * A script that enables/disables the visibility of thruster exhaust plumes based on a spaceship's
 * acceleration.
 *
 * @author SlavSquatSuperstar
 */
public class ThrustController extends Script {

    private Thruster[] thrusters;
    private KeyMovement keyMovement;
    private KeyRotation keyRotation;
    private Rigidbody rb;

    public ThrustController(Thruster... thrusters) {
        this.thrusters = thrusters;
    }

    @Override
    public void start() {
        keyMovement = gameObject.getComponent(KeyMovement.class);
        keyRotation = gameObject.getComponent(KeyRotation.class);
        rb = getRigidbody();
    }

    @Override
    public void update(float dt) {
        if (rb == null) return;

        // Slow movement and turning
        var braking = Input.keyDown("space");
        if (braking) rb.setDrag(2f).setAngDrag(2f);
        else rb.setDrag(0f).setAngDrag(0f);

        // Calculate brake direction
        Vec2 brakeDir;
        float angBrakeDir;
        if (braking) {
            brakeDir = rb.getVelocity().mul(-1f);
            angBrakeDir = rb.getAngVelocity(); // Using right-handed coords here, so choose positive
        } else {
            brakeDir = new Vec2();
            angBrakeDir = 0f;
        }

        // Activate thrusters
        if (keyMovement != null) {
            var input = keyMovement.getUserInput();
            for (var thr : thrusters) {
                var shouldBrake = thr.moveDir.faces(brakeDir) && rb.getSpeed() > 1e-3f; // Don't fire when moving very slow
                thr.setMoveEnabled(thr.moveDir.faces(input) || shouldBrake);
            }
        }
        if (keyRotation != null) {
            var turnInput = keyRotation.getUserInput().x;
            for (var thr : thrusters) {
                var shouldBrake = thr.turnDir.faces(angBrakeDir) && rb.getAngSpeed() > 1e-3f;
                thr.setTurnEnabled(thr.turnDir.faces(turnInput) || shouldBrake);
            }
        }

    }

}
