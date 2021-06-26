package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Vector2;

import java.util.ArrayList;

/**
 * A simulation of the world inside the game.
 * <p>
 * Thanks to GamesWithGabe's <a href = https://youtube.com/playlist?list=PLtrSb4XxIVbpZpV65kk73OoUcIrBzoSiO>
 * Coding a 2D Physics Engine playlist</a>for explaining the math and logic.
 * </p>
 */
public class Physics2D {

    public Vector2 gravity;
    private ArrayList<RigidBody2D> rigidBodies;

    public Physics2D(Vector2 gravity) {
        this.gravity = gravity;
        rigidBodies = new ArrayList<>();
    }

    public void add(GameObject o) {
        RigidBody2D rb = o.getComponent(RigidBody2D.class);
        if (rb != null)
            rigidBodies.add(rb);
    }

    public void remove(GameObject o) {
        RigidBody2D rb = o.getComponent(RigidBody2D.class);
        if (rb != null)
            rigidBodies.remove(rb);
    }

    public void update(float dt) {
        // apply forces
        rigidBodies.forEach(r -> {
            if (r.followsGravity)
                r.addForce(gravity.mul(r.mass));
        });
    }

    // handle collisions
    // collider.onCollision

}
