package slavsquatsuperstar.mayonez.physics2d;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.physics2d.primitives.Collider2D;
import slavsquatsuperstar.mayonez.renderer.DebugDraw;
import slavsquatsuperstar.util.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simulation of the world inside the game.
 * <p>
 * Thanks to GamesWithGabe's <a href="https://youtube.com/playlist?list=PLtrSb4XxIVbpZpV65kk73OoUcIrBzoSiO"> Coding a 2D
 * Physics Engine playlist</a>for explaining the math and logic.
 * </p>
 *
 * @author SlavSquatSuperstar
 */
public class Physics2D {

    // Collisions
    private final List<Rigidbody2D> rigidbodies;
    private final List<Pair<Rigidbody2D, Rigidbody2D>> collidingPairs;
    private final List<CollisionManifold> collisions;
    private final List<Collider2D> colliders;

    // Forces
    private final List<ForceRegistration> forceRegistry;
    private final ForceGenerator gravityForce;
    private final ForceGenerator dragForce;
    private final float deltaTime;
    private Vector2 gravity; // acceleration to due gravity

    public Physics2D(float deltaTime, Vector2 gravity) {
        this.deltaTime = deltaTime;
        rigidbodies = new ArrayList<>();
        collidingPairs = new ArrayList<>();
        collisions = new ArrayList<>();
        colliders = new ArrayList<>();

        forceRegistry = new ArrayList<>();
        setGravity(gravity);
        gravityForce = (rb, dt) -> rb.addForce(getGravity().mul(rb.getMass()));
        dragForce = (rb, dt) -> {
            // Apply drag if moving
            if (!MathUtils.equals(rb.velocity().lengthSquared(), 0))
                rb.addForce(rb.velocity().mul(-rb.getDrag()));
        };
    }

    public void physicsUpdate(float dt) {
        collidingPairs.clear();
        collisions.clear();

        // TODO Pre-collision optimizations and spatial partitioning
        // Detect Collisions and Create Collision Events
        detectCollisions();

        // Update force generators
        forceRegistry.forEach(fr -> fr.fg.applyForce(fr.rb, dt));

        // TODO Create collision events and call GameObject.onCollision()
        resolveCollisions();

        // Update object transforms independent of colliders
        rigidbodies.forEach(rb -> rb.physicsUpdate(dt));
    }

    /*
     * Collider & Rigidbody (dynamic): Can move and push
     *  - Ex: player, enemy
     * Collider & no Rigidbody (static): Can push but not move
     *  - Ex: fire area
     * Rigidbody & no Collider (sprite): Can move but not push
     *  - Ex: sun
     *
     * Static vs Static (ignore)
     * Dynamic vs Static (ignore for now)
     * Dynamic vs Dynamic
     */
    private void detectCollisions() {
        for (int i = 0; i < colliders.size(); i++) {
            // Set j to i + 1 to avoid duplicate collisions and checking against self
            for (int j = i + 1; j < colliders.size(); j++) {
                Collider2D c1 = colliders.get(i);
                Collider2D c2 = colliders.get(j);
                Rigidbody2D r1 = c1.getRigidbody();
                Rigidbody2D r2 = c2.getRigidbody();

                // Ignore collision if either rigidbody is static
                if (r1 == null || r2 == null)
                    continue;
                else if (r1.hasInfiniteMass() || r2.hasInfiniteMass())
                    continue;

                // Get collision info
                CollisionManifold result = c1.getCollisionInfo(c2);

                // May be null b/c not all collisions are implemented
                if (result != null && result.isColliding()) {
                    DebugDraw.drawLine(c1.center(), c2.center(), Colors.RED);
                    collidingPairs.add(new ImmutablePair<>(r1, r2));
                    collisions.add(result);
                    Logger.log("%s intersects %s", c1, c2);
                }
            }
        }
    }

    private void resolveCollisions() {
//        for (int k = 0; k < 6; k++) { // Iterative impulse resolution
        for (int i = 0; i < collisions.size(); i++) {
            CollisionManifold col = collisions.get(i);
//            for (Vector2 contact : col.getContactPoints()) {
            Rigidbody2D r1 = collidingPairs.get(i).getLeft();
            Rigidbody2D r2 = collidingPairs.get(i).getRight();

            // Draw contact point
            DebugDraw.drawPoint(col.getContactPoints().get(0), Colors.BLACK);

            if (r1.hasInfiniteMass() && r2.hasInfiniteMass())
                return; // Return in case both infinite mass

            // Resolve static collisions and w objects
            float sumMass = r1.getMass() + r2.getMass();
            float depth1 = col.getDepth() * r1.getMass() / sumMass;
            float depth2 = col.getDepth() * r2.getMass() / sumMass;
            r1.transform.move(col.getNormal().mul(-depth1));
            r2.transform.move(col.getNormal().mul(depth2));

            // Resolve dynamic collisions and change velocities
            applyImpulse1(r1, r2, col);
//            }
        }
//        }
    }

    // Collision Helper Methods

    private void applyImpulse1(Rigidbody2D r1, Rigidbody2D r2, CollisionManifold collision) {
        // Solve for linear velocity.
        float invMass1 = r1.getInverseMass();
        float invMass2 = r2.getInverseMass();
        float sumInvMass = invMass1 + invMass2;
        Vector2 vel1 = r1.velocity();
        Vector2 vel2 = r2.velocity();

        Vector2 relativeVel = vel2.sub(vel1);
        Vector2 normal = collision.getNormal(); // Direction of collision
        if (relativeVel.dot(normal) > 0f) // Stop if moving away or stationary
            return;

        float elasticity = r1.getCollider().getBounce() * r2.getCollider().getBounce(); // Coefficient of restitution
        float collisionVel = -(1f + elasticity) * relativeVel.dot(normal);
        float impulse = collisionVel / sumInvMass;
        if (!collision.getContactPoints().isEmpty())
            impulse /= collision.getContactPoints().size();

        r1.addImpulse(normal.mul(-impulse));
        r2.addImpulse(normal.mul(impulse));
    }

    // Can't push objects after colliding, velocity slows to 0.
    private void applyImpulse2(Rigidbody2D r1, Rigidbody2D r2, CollisionManifold collision) {

        Vector2 vel1 = r1.velocity();
        Vector2 vel2 = r2.velocity();
        Vector2 normal = collision.getNormal(); // Direction of collision
        Vector2 tangent = new Vector2(-normal.y, normal.x); // Collision plane

        float dotTan1 = vel1.dot(tangent);
        float dotTan2 = vel2.dot(tangent);
        float dotNorm1 = vel1.dot(normal);
        float dotNorm2 = vel2.dot(normal);

        // Conservation of momentum
        float sumMass = r1.getMass() + r2.getMass();
        float elasticity = r1.getCollider().getBounce() * r2.getCollider().getBounce(); // Coefficient of restitution
        float momentum1 = (dotNorm1 * (r1.getMass() - r2.getMass()) + 2f * r2.getMass() * dotNorm2) / sumMass * elasticity;
        float momentum2 = (dotNorm2 * (r2.getMass() - r1.getMass()) + 2f * r1.getMass() * dotNorm1) / sumMass * elasticity;

        float vx1 = tangent.x * dotTan1 + normal.x * momentum1;
        float vy1 = tangent.y * dotTan1 + normal.y * momentum1;
        float vx2 = tangent.x * dotTan2 + normal.x * momentum2;
        float vy2 = tangent.y * dotTan2 + normal.y * momentum2;

        r1.velocity().set(vx1, vy1);
        r2.velocity().set(vx2, vy2);
    }

    // Game Object Methods

    public void add(GameObject o) {
        Rigidbody2D rb = o.getComponent(Rigidbody2D.class);
        // TODO register force method
        // TODO rules for registration (e.g. if object is static, ignore)

        if (rb != null) {
            rigidbodies.add(rb);
            if (rb.followGravity())
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
