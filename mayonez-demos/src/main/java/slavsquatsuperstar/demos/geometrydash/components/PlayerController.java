package slavsquatsuperstar.demos.geometrydash.components;

import slavsquatsuperstar.demos.geometrydash.LevelEditor;
import slavsquatsuperstar.demos.geometrydash.LevelScene;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.graphics.JCamera;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.physics.Rigidbody;

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
        JCamera cam = (JCamera) getScene().getCamera();
        if (getScene() instanceof LevelEditor) {
            cam.enableDragAndDrop(true).enableKeepInScene(false);
        } else if (getScene() instanceof LevelScene) {
            cam.enableKeepInScene(true).setSubject(parent); // TODO pass camera in player c'tor?
        }
        rb = getRigidbody();
        rb.setDrag(drag);
    }

    @Override
    public void update(float dt) {
        if (getScene() instanceof LevelScene) {
            // Jump if on ground
            if (KeyInput.keyDown("w"))
                // Impulse must be big enough to not get stuck on ground next frame
                rb.addImpulse(new Vec2(0, speed));

            // Ground Pound if in air
            if (KeyInput.keyDown("s"))
                rb.addImpulse(new Vec2(0, -speed));
        }
    }

}
