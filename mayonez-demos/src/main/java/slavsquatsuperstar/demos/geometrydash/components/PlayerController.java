package slavsquatsuperstar.demos.geometrydash.components;

import mayonez.Script;
import mayonez.graphics.CameraMode;
import mayonez.input.KeyInput;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import slavsquatsuperstar.demos.geometrydash.LevelEditor;
import slavsquatsuperstar.demos.geometrydash.LevelScene;

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
        if (getScene() instanceof LevelEditor) {
            cam.setKeepInScene(false).setMode(CameraMode.FREE);
        } else if (getScene() instanceof LevelScene) {
            cam.setMode(CameraMode.FOLLOW).setSubject(gameObject);
        }
        rb = getRigidbody();
        rb.setDrag(drag);
    }

    @Override
    public void update(float dt) {
        if (getScene() instanceof LevelScene) {
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
