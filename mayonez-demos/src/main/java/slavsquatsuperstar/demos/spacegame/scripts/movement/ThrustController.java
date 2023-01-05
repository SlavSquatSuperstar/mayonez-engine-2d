package slavsquatsuperstar.demos.spacegame.scripts.movement;

import mayonez.Script;
import mayonez.input.KeyInput;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.scripts.movement.KeyMovement;
import mayonez.scripts.movement.KeyRotation;

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
        boolean brake = KeyInput.keyDown("space");
        if (brake) rb.setDrag(2f).setAngDrag(2f);
        else rb.setDrag(0f).setAngDrag(0f);

        // Calculate brake direction
        Vec2 brakeDir;
        float angBrakeDir;
        if (brake) {
            brakeDir = rb.getVelocity().mul(-1f);
            angBrakeDir = rb.getAngVelocity(); // Using right-handed coords here, so choose positive
        } else {
            brakeDir = new Vec2();
            angBrakeDir = 0f;
        }

        // Activate thrusters
        if (keyMovement != null) {
            Vec2 input = keyMovement.getRawInput();
            for (Thruster t : thrusters) {
                boolean shouldBrake = t.moveDir.faces(brakeDir) && rb.getSpeed() > 1e-3f; // Don't fire when moving very slow
                t.setMoveEnabled(t.moveDir.faces(input) || shouldBrake);
            }
        }
        if (keyRotation != null) {
            float input = keyRotation.getRawInput().x;
            for (Thruster t : thrusters) {
                boolean shouldBrake = t.moveDir.faces(brakeDir) && rb.getAngSpeed() > 1e-3f;
                t.setTurnEnabled(t.turnDir.faces(input) || shouldBrake);
            }
        }

    }

}
