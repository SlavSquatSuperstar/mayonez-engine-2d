package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Logger;
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

    private final float deltaTime;
    private final ArrayList<RigidBody2D> rigidBodies;
    private final ArrayList<Collider2D> colliders;
    private Vector2 gravity;

    public Physics2D(float deltaTime, Vector2 gravity) {
        this.deltaTime = deltaTime;
        setGravity(gravity);
        rigidBodies = new ArrayList<>();
        colliders = new ArrayList<>();
    }

    public void add(GameObject o) {
        RigidBody2D rb = o.getComponent(RigidBody2D.class);
        if (rb != null)
            rigidBodies.add(rb);

        Collider2D c = o.getComponent(Collider2D.class);
        if (c != null)
            colliders.add(c);
    }

    public void remove(GameObject o) {
        rigidBodies.remove(o.getComponent(RigidBody2D.class));
        colliders.remove(o.getComponent(Collider2D.class));
    }

    public void physicsUpdate(float dt) {
        // apply forces
        rigidBodies.forEach(r -> {
            if (r.followsGravity)
                r.addForce(gravity.mul(r.mass));
            r.physicsUpdate(dt);
        });

        // Detect collisions
        colliders.forEach(c -> colliders.forEach(other -> {
            if (c.detectCollision(other))
                Logger.log("%s intersects %s", c, other);
        }));

        // TODO Create collision events and call GameObject.onCollision()
        // TODO Resolve collisions
    }

    public void setGravity(Vector2 gravity) {
        this.gravity = gravity;
    }
}
