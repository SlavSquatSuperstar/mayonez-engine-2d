package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.renderer.Camera;
import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.scripts.KeepInScene;

import java.awt.event.KeyEvent;

@SuppressWarnings("unused")
public class PlayerController extends Script {

    // Physics Fields
    private Rigidbody2D rb;

    // Movement Parameters
    private float speed;
    private float brakeForce = 0.6f;

    public PlayerController(float speed) {
        this.speed = speed;
    }

    @Override
    public void start() {
        Camera cam = getScene().camera();
        if (getScene() instanceof LevelEditorScene) {
            cam.enableFreeMovement(true);
            cam.enableKeepInScene(false);
            parent.getComponent(KeepInScene.class).setEnabled(false);
        } else if (getScene() instanceof LevelScene) {
            cam.enableKeepInScene(true);
            cam.setSubject(parent); // TODO pass camera in player c'tor?
        }
        rb = parent.getComponent(Rigidbody2D.class);
    }

    @Override
    public void update(float dt) {
        // Rotate player
        if (Game.keyboard().keyDown(KeyEvent.VK_Q))
            transform.rotate(-2);
        if (Game.keyboard().keyDown(KeyEvent.VK_E))
            transform.rotate(2);

        Vector2 velocity = rb.velocity();

        if (getScene() instanceof LevelScene) {
            // Jump if on ground
            if (Game.keyboard().keyDown("up"))
                // Impulse must be big enough to not get stuck on ground next frame
                rb.addImpulse(new Vector2(0, -speed / 5f));

            // Ground Pound if in air
            if (Game.keyboard().keyDown("down"))
                rb.addImpulse(new Vector2(0, speed / 5f));
        }

    }

}
