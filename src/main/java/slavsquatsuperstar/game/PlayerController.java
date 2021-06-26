package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.components.Script;
import slavsquatsuperstar.mayonez.physics2d.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.RigidBody2D;
import slavsquatsuperstar.util.MathUtils;

@SuppressWarnings("unused")
public class PlayerController extends Script {

    // Physics Fields
    private final GameObject ground;
    private RigidBody2D rb;
    private AlignedBoxCollider2D box;

    // Movement Parameters
    private float topSpeed = 10f;
    private float thrustForce = 150f;
    private float brakeForce = 8f;
    private float drag = 0.5f; // [0, 1]
    private float mass = 8f;

    public PlayerController(GameObject ground) {
        this.ground = ground;
    }

    @Override
    public void start() {
//        scene().camera().setSubject(this); // TODO pass camera in player c'tor?
        box = parent.getComponent(AlignedBoxCollider2D.class);
        rb = parent.getComponent(RigidBody2D.class);
        rb.mass = this.mass;
    }

    @Override
    public void update(float dt) {
        // Detect player input
        int xInput = Game.keyboard().getAxis("horizontal");
        int yInput = Game.keyboard().getAxis("vertical");
//        yInput = 0;

        // Don't want to move faster diagonally so normalize
        Vector2 input = new Vector2(xInput, yInput).unit().mul(thrustForce);
        rb.addForce(input);

        Vector2 velocity = rb.velocity();
        // Limit Top Speed
        if (rb.speed() > topSpeed)
            velocity = velocity.mul(topSpeed / rb.speed());

//         Apply Drag Unless Stationary (prevent divide by 0)
        if (velocity.magnitude() != 0) {
            Vector2 dragForce = velocity.div(-drag);
//			dragForce.y = 0; // horizontal only
            // Increase drag by braking
            if (Game.keyboard().keyDown("space"))
                dragForce = dragForce.mul((drag + brakeForce) / drag);
            rb.addForce(dragForce);

            // Just stop if moving really slow and not pressing move keys
            if (xInput == 0 && Math.abs(velocity.x) < drag)
                velocity.x = 0;
            if (yInput == 0 && Math.abs(velocity.y) < drag)
                velocity.y = 0;
        }

        // Collide with walls
        if (box.min().x < 0 || box.max().x > scene().getWidth())
            rb.velocity().x = 0;
        // Collide with floor
        if (box.min().y < 0) { // TODO detect & resolve collisions elsewhere
            rb.velocity().y = 0;
        } else if (box.max().y > ground.getY()) {
            rb.velocity().y = 0;
            parent.setY(ground.getY() - box.height() / 2);
//            // Jump if on ground
//            if (Game.keyboard().keyDown("up"))
//                // Impulse must be big enough to not get stuck on ground next frame
//                rb.addImpulse(new Vector2(0, -thrustForce));
//        } else {
//            // Ground Pound if in air
//            if (Game.keyboard().keyDown("down"))
//                rb.addImpulse(new Vector2(0, thrustForce / 5));
        }

        // Bounds detection
        // TODO: Make collision detector, move to physics
        // TODO make KeepInScene script
        if (scene().isBounded()) {
            parent.setX(MathUtils.clamp(box.center().x, 0 + box.width() / 2f, scene().getWidth() - box.width() / 2f));
            parent.setY(MathUtils.clamp(box.center().y, 0 + box.height() / 2f, scene().getHeight() - box.height() / 2f));
        }

    }

}
