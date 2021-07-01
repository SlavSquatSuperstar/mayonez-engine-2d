package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.primitives.Collider2D;
import slavsquatsuperstar.util.MathUtils;

import java.util.ArrayList;

/**
 * A simulation of the world inside the game.
 * <p>
 * Thanks to GamesWithGabe's <a href="https://youtube.com/playlist?list=PLtrSb4XxIVbpZpV65kk73OoUcIrBzoSiO">
 * Coding a 2D Physics Engine playlist</a>for explaining the math and logic.
 * </p>
 */
public class Physics2D {

    private final float deltaTime;
    private final ArrayList<Rigidbody2D> rigidbodies;
    private final ArrayList<Collider2D> colliders;
    private final ArrayList<ForceRegistration> forceRegistry;
    private final ForceGenerator gravityForce;
    private final ForceGenerator dragForce;
    private Vector2 gravity; // acceleration to due gravity

    public Physics2D(float deltaTime, Vector2 gravity) {
        this.deltaTime = deltaTime;
        rigidbodies = new ArrayList<>();
        colliders = new ArrayList<>();
        forceRegistry = new ArrayList<>();
        setGravity(gravity);
        gravityForce = (rb, dt) -> rb.addForce(getGravity().mul(rb.mass));
        dragForce = (rb, dt) -> {
            // Apply drag if moving
            if (!MathUtils.equals(rb.velocity().lengthSquared(), 0f))
                rb.addForce(rb.velocity().mul(-rb.drag));
        };
    }

    public void physicsUpdate(float dt) {
        // TODO Pre-collision optimizations

        // Detect collisions
        colliders.forEach(col -> colliders.forEach(other -> {
            if (col.detectCollision(other))
                Logger.log("%s intersects %s", col, other);
        }));

        // TODO Create collision events and call GameObject.onCollision()
        // TODO Resolve collisions

        // Update force generators
        forceRegistry.forEach(fr -> fr.fg.applyForce(fr.rb, dt));

        // Update object transforms
        rigidbodies.forEach(rb -> rb.physicsUpdate(dt));
    }

    public void add(GameObject o) {
        Rigidbody2D rb = o.getComponent(Rigidbody2D.class);
        // TODO register force method
        // TODO rules for registration (e.g. if object is static, ignore)

        if (rb != null) {
            rigidbodies.add(rb);
            if (rb.followsGravity)
                forceRegistry.add(new ForceRegistration(gravityForce, rb));
            forceRegistry.add(new ForceRegistration(dragForce, rb));
        }

        Collider2D c = o.getComponent(Collider2D.class);
        if (c != null)
            colliders.add(c);
    }

    public void remove(GameObject o) {
        rigidbodies.remove(o.getComponent(Rigidbody2D.class));
        colliders.remove(o.getComponent(Collider2D.class));
        // remove from force registry
    }

    private Vector2 getGravity() {
        return gravity;
    }

    public void setGravity(Vector2 gravity) {
        this.gravity = gravity;
    }
}
