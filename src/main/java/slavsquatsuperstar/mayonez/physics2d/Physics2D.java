package slavsquatsuperstar.mayonez.physics2d;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Colors;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;
import slavsquatsuperstar.mayonez.renderer.DebugDraw;

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

    public final static float GRAVITY_CONSTANT = 9.8f;
    private final static int IMPULSE_ITERATIONS = 6;

    // Collisions
    private final List<Rigidbody2D> rigidbodies;
    private final List<Pair<Rigidbody2D, Rigidbody2D>> collidingPairs;
    private final List<CollisionManifold> collisions;
    private final List<Collider2D> colliders;

    // Forces
    private final List<ForceRegistration> forceRegistry;
    private final ForceGenerator gravityForce;
    private final ForceGenerator dragForce;
    private Vec2 gravity; // acceleration due to gravity

    public Physics2D() {
        rigidbodies = new ArrayList<>();
        collidingPairs = new ArrayList<>();
        collisions = new ArrayList<>();
        colliders = new ArrayList<>();

        forceRegistry = new ArrayList<>();
        setGravity(new Vec2(0, Physics2D.GRAVITY_CONSTANT));
        gravityForce = (rb, dt) -> rb.addForce(getGravity().mul(rb.getMass()));
        dragForce = (rb, dt) -> {
            // Apply drag if moving (prevent divide by 0)
            if (!MathUtils.equals(rb.velocity().lenSquared(), 0))
                rb.addForce(rb.velocity().mul(-rb.getDrag()));
        };
    }

    // TODO seems to be some tunneling at high speeds

    /**
     * Updates all  objects in the physics simulation.
     *
     * @param dt seconds since the last frame
     */
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

                // Dynamic vs Dynamic
                // Dynamic vs Static
                // Static vs Static (ignore)

                // TODO figure out which objects can check for collision
                // Ignore collision if no rigidbody
                if (r1 == null || r2 == null)
                    continue;
                    // Ignore collision if both are static
                else if (r1.hasInfiniteMass() && r2.hasInfiniteMass())
                    continue;

                // Get collision info
                CollisionManifold result = c1.getCollisionInfo(c2);

                // May be null b/c not all collisions are implemented
                if (result != null) {
                    // TODO still send collision events if triggers
                    // Add the collisions if neither is a trigger
                    if (!c1.isTrigger() && !c2.isTrigger()) {
                        DebugDraw.drawLine(c1.center(), c2.center(), Colors.RED);
                        DebugDraw.drawVector(c1.center(), result.getNormal(), Colors.BLACK);
                        collidingPairs.add(new ImmutablePair<>(r1, r2));
                        collisions.add(result);
//                        Logger.log("%s intersects %s", c1, c2);
                    }
                }
            }
        }
    }

    // TODO resolve collision for each shape (get manifold and send event to each object)
    private void resolveCollisions() {
        for (int i = 0; i < collisions.size(); i++) {
            CollisionManifold col = collisions.get(i);
            Rigidbody2D r1 = collidingPairs.get(i).getLeft();
            Rigidbody2D r2 = collidingPairs.get(i).getRight();

            // Draw contact point
//            for (Vec2 contact : col.getContactPoints())
//                DebugDraw.drawPoint(contact, Colors.BLACK);

            if (r1.hasInfiniteMass() && r2.hasInfiniteMass())
                return; // Return in case both infinite mass

            // Resolve static collisions and separate objects factoring in mass
            float massRatio = r2.getMass() / (r1.getMass() + r2.getMass());
            float depth1 = col.getDepth() * massRatio;
            float depth2 = col.getDepth() * (1 - massRatio);
            r1.transform.move(col.getNormal().mul(-depth1));
            r2.transform.move(col.getNormal().mul(depth2));

            // Iterative impulse resolution
            for (int k = 0; k < IMPULSE_ITERATIONS; k++)
                applyImpulse(r1, r2, col); // Resolve dynamic collisions and change velocities
        }
    }

    // Collision Helper Methods

    private void applyImpulse(Rigidbody2D r1, Rigidbody2D r2, CollisionManifold collision) {
        // Solve for linear velocity.
        float invMass1 = r1.getInverseMass();
        float invMass2 = r2.getInverseMass();
        float sumInvMass = invMass1 + invMass2;
        Vec2 vel1 = r1.velocity();
        Vec2 vel2 = r2.velocity();

        Vec2 relativeVel = vel2.sub(vel1);
        Vec2 normal = collision.getNormal(); // Direction of collision
        if (relativeVel.dot(normal) > 0f) // Stop if moving away or stationary
            return;

        float elasticity = r1.getCollider().getBounce() * r2.getCollider().getBounce(); // Coefficient of restitution
        float collisionVel = -(1f + elasticity) * relativeVel.dot(normal);
        float impulse = collisionVel / sumInvMass;

        // TODO Apply force at contact points evenly
        if (!r1.hasInfiniteMass())
            r1.addImpulse(normal.mul(-impulse));
        if (!r2.hasInfiniteMass())
            r2.addImpulse(normal.mul(impulse));
    }

    // Can't push objects after colliding, velocity slows to 0.
    private void applyImpulse2(Rigidbody2D r1, Rigidbody2D r2, CollisionManifold collision) {

        Vec2 vel1 = r1.velocity();
        Vec2 vel2 = r2.velocity();
        Vec2 normal = collision.getNormal(); // Direction of collision
        Vec2 tangent = new Vec2(-normal.y, normal.x); // Collision plane

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

    public void addObject(GameObject o) {
        Rigidbody2D rb = o.getComponent(Rigidbody2D.class);
        // TODO register force method
        // TODO rules for registration (e.g. if object is static, ignore)
        // TODO or just apply forces based on tags

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

    public void removeObject(GameObject o) {
        rigidbodies.remove(o.getComponent(Rigidbody2D.class));
        colliders.remove(o.getComponent(Collider2D.class));
        // remove from force registry
    }

    public void setScene(Scene newScene) {
        rigidbodies.clear();
        colliders.clear();
        newScene.getObjects(null).forEach(this::addObject);
    }

    // Getters and Setters

    private Vec2 getGravity() {
        return gravity;
    }

    public void setGravity(Vec2 gravity) {
        this.gravity = gravity;
    }
}
