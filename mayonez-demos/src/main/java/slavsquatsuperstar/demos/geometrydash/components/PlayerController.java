package slavsquatsuperstar.demos.geometrydash.components;

import mayonez.*;
import mayonez.graphics.camera.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.*;
import slavsquatsuperstar.demos.geometrydash.GDEditor;
import slavsquatsuperstar.demos.geometrydash.GDLevel;

@SuppressWarnings("unused")
public class PlayerController extends Script {

    // Physics Fields
    private Rigidbody rb;

    // Movement Parameters
    private float speed;
    private float brakeForce = 0.6f;
    private float drag = 0.4f;

    public PlayerController(float speed) {
        this.speed = speed;
    }

    @Override
    public void start() {
        var cam = getScene().getCamera();
        if (getScene() instanceof GDEditor) {
            cam.setKeepInScene(false).setMode(CameraMode.FREE);
        } else if (getScene() instanceof GDLevel) {
            cam.setMode(CameraMode.FOLLOW).setSubject(gameObject);
        }
        rb = getRigidbody();
        rb.setDrag(drag);
    }

    @Override
    public void update(float dt) {
        if (getScene() instanceof GDLevel) {
            // Jump if on ground
            if (KeyInput.keyDown("w")) {
                // Impulse must be big enough to not get stuck on ground next frame
                rb.applyImpulse(new Vec2(0, speed));
            }
            // Ground Pound if in air
            if (KeyInput.keyDown("s")) {
                rb.applyImpulse(new Vec2(0, -speed));
            }
        }
    }

}
