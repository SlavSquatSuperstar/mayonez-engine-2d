package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.components.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.AlignedBoxCollider2D;

import java.awt.event.KeyEvent;

@SuppressWarnings("unused")
public class PlayerController extends Script {

    // Physics Fields
    private final GameObject ground;
    private Rigidbody2D rb;
    private AlignedBoxCollider2D box;

    // Movement Parameters
    private float topSpeed = 10f;
    private float thrustForce = 150f;
    private float brakeForce = 0.6f;
    private float drag = 0f; // [0, 1]
    private float mass = 0.2f;

    public PlayerController(GameObject ground) {
        this.ground = ground;
    }

    @Override
    public void start() {
//        scene().camera().setSubject(parent); // TODO pass camera in player c'tor?
        box = parent.getComponent(AlignedBoxCollider2D.class);
        rb = parent.getComponent(Rigidbody2D.class);
        parent.getComponent(KeyMovement.class).speed = thrustForce;
        rb.mass = this.mass;
        rb.drag = this.drag;
    }

    @Override
    public void update(float dt) {
        // Rotate player
        if (Game.keyboard().keyDown(KeyEvent.VK_Q))
            parent.transform.rotate(-2);
        if (Game.keyboard().keyDown(KeyEvent.VK_E))
            parent.transform.rotate(2);

        Vector2 velocity = rb.velocity();
        // Limit Top Speed
        if (rb.speed() > topSpeed)
            velocity = velocity.mul(topSpeed / rb.speed());

//         Apply Drag Unless Stationary (prevent divide by 0)
        if (velocity.length() != 0) {
            // Increase drag by braking
            if (Game.keyboard().keyDown("space"))
                rb.drag = this.drag + brakeForce;
            else
                rb.drag = this.drag;

            // Just stop if moving really slow and not pressing move keys
//            if (xInput == 0 && Math.abs(velocity.x) < drag)
//                velocity.x = 0;
//            if (yInput == 0 && Math.abs(velocity.y) < drag)
//                velocity.y = 0;
        }

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
