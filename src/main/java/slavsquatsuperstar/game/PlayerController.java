package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.KeyInput;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.renderer.Camera;
import slavsquatsuperstar.mayonez.scripts.KeepInScene;

import java.awt.event.KeyEvent;

@SuppressWarnings("unused")
public class PlayerController extends Script {

    // Physics Fields
    private Rigidbody2D rb;

    // Movement Parameters
    private float speed;
    private float brakeForce = 0.6f;
    private float drag = 0.4f;

    public PlayerController(float speed) {
        this.speed = speed;
    }

    @Override
    public void start() {
        Camera cam = getScene().camera();
        if (getScene() instanceof LevelEditorScene) {
            cam.enableFreeMovement(true).enableKeepInScene(false);
            parent.getComponent(KeepInScene.class).setEnabled(false);
        } else if (getScene() instanceof LevelScene) {
            cam.enableKeepInScene(true).setSubject(parent); // TODO pass camera in player c'tor?
        }
        rb = parent.getComponent(Rigidbody2D.class);
        rb.setDrag(drag);
    }

    @Override
    public void update(float dt) {
        // Rotate player
        if (KeyInput.keyDown(KeyEvent.VK_Q))
            transform.rotate(-2);
        if (KeyInput.keyDown(KeyEvent.VK_E))
            transform.rotate(2);

        Vec2 velocity = rb.getVelocity();

        if (getScene() instanceof LevelScene) {
            // Jump if on ground
            if (KeyInput.keyDown("up"))
                // Impulse must be big enough to not get stuck on ground next frame
                rb.addImpulse(new Vec2(0, -speed / 5f));

            // Ground Pound if in air
            if (KeyInput.keyDown("down"))
                rb.addImpulse(new Vec2(0, speed / 5f));
        }

    }

}
