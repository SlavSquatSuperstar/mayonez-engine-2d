package slavsquatsuperstar.demos.geometrydash.components;

import mayonez.*;
import mayonez.graphics.camera.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.*;
import slavsquatsuperstar.demos.geometrydash.GDEditorScene;
import slavsquatsuperstar.demos.geometrydash.GDLevelScene;

/**
 * A player controller script for the Geometry Dash scenes.
 *
 * @author SlavSquatSuperstar
 */
@SuppressWarnings("unused")
public class GDPlayerController extends Script {

    // Physics Fields
    private Rigidbody rb;

    // Movement Parameters
    private float speed;
    private float brakeForce = 0.6f;
    private float drag = 0.4f;

    public GDPlayerController(float speed) {
        this.speed = speed;
    }

    @Override
    public void start() {
        var cam = getScene().getCamera();
        if (getScene() instanceof GDEditorScene) {
            cam.setKeepInScene(false).setMode(CameraMode.FREE);
        } else if (getScene() instanceof GDLevelScene) {
            cam.setMode(CameraMode.FOLLOW).setSubject(gameObject);
        }
        rb = getRigidbody();
        rb.setDrag(drag);
    }

    @Override
    public void update(float dt) {
        if (getScene() instanceof GDLevelScene) {
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
