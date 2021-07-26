package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.renderer.Camera;

import java.awt.event.KeyEvent;

@SuppressWarnings("unused")
public class PlayerController extends Script {

    // Physics Fields
    private Rigidbody2D rb;

    // Movement Parameters
    private float brakeForce = 0.6f;

    @Override
    public void start() {
        Camera cam = scene().camera();
        cam.setSubject(parent); // TODO pass camera in player c'tor?
        if (scene() instanceof LevelEditorScene) {
            cam.enableFreeMovement(true);
            cam.enableKeepInScene(true);
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

//         Apply Drag Unless Stationary (prevent divide by 0)
//        if (velocity.length() != 0) {
//            // Increase drag by braking
//            if (Game.keyboard().keyDown("space"))
//                rb.setDrag(this.drag + brakeForce);
//            else
//                rb.setDrag(this.drag);
//
            // Just stop if moving really slow and not pressing move keys
//            if (xInput == 0 && Math.abs(velocity.x) < drag)
//                velocity.x = 0;
//            if (yInput == 0 && Math.abs(velocity.y) < drag)
//                velocity.y = 0;
//        }

        // Collide with walls
//        if (box.min().x < 0 || box.max().x > scene().getWidth())
//            rb.velocity().x = 0;
//        // Collide with floor
//        if (box.min().y < 0) { // TODO detect & resolve collisions elsewhere
//            rb.velocity().y = 0;
//        } else if (box.max().y > ground.getY()) {
//            rb.velocity().y = 0;
//            parent.setY(ground.getY() - box.height() / 2);
//            // Jump if on ground
//            if (Game.keyboard().keyDown("up"))
//                // Impulse must be big enough to not get stuck on ground next frame
//                rb.addImpulse(new Vector2(0, -thrustForce));
//        } else {
//            // Ground Pound if in air
//            if (Game.keyboard().keyDown("down"))
//                rb.addImpulse(new Vector2(0, thrustForce / 5));
//        }

//        // TODO: Make collision detector, move to physics

    }

}
