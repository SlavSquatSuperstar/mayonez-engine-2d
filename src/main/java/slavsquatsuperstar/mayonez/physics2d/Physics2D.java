package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;

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
    private final static int IMPULSE_ITERATIONS = Preferences.IMPULSE_ITERATIONS;

    // Collisions
    private final List<Rigidbody2D> bodies;
    private final List<Collider2D> colliders;
    private final List<CollisionManifold> collisions;

    // Forces
    private final List<ForceRegistration> forceRegistry;
    private final ForceGenerator gravityForce;
    private Vec2 gravity; // acceleration due to gravity

    public Physics2D() {
        bodies = new ArrayList<>();
        colliders = new ArrayList<>();
        collisions = new ArrayList<>();

        forceRegistry = new ArrayList<>();
        setGravity(new Vec2(0, Physics2D.GRAVITY_CONSTANT));
        gravityForce = (rb, dt) -> rb.addForce(getGravity().mul(rb.getMass()));
    }

    /**
     * Updates all  objects in the physics simulation.
     *
     * @param dt seconds since the last frame
     */
    /*
     * Pre-collision optimizations
     * Detect collisions x1
     * Resolve static collisions x1
     * Send collisions events x2
     * Resolve dynamic collisions x2
     *
     * Integrate forces and velocities
     */
    public void physicsUpdate(float dt) {
        collisions.clear();

        // TODO Pre-collision optimizations and spatial partitioning
        // TODO Create collision events and call GameObject.onCollision()
        // Detect Collisions and Create Collision Events
        detectCollisions();

        // Update environmental forces
        forceRegistry.forEach(fr -> fr.fg.applyForce(fr.rb, dt));

        // Resolve collisions
        collisions.forEach(col -> {
//            if (col.getSelf().isStatic() && col.getOther().isStatic())
//                return; // Return if both null rb or infinite mass

            Rigidbody2D r1 = col.getSelf().getRigidbody();
            Rigidbody2D r2 = col.getOther().getRigidbody();
            resolveStaticCollision(col, r1, r2); // Correct positions so contact points should line up
            resolveDynamicCollision(col, r1, r2); // Solve for velocity
        });

        // Integrate velocity and position for bodies
        bodies.forEach(rb -> rb.physicsUpdate(dt));
    }

    // Collision Helper Methods

    /*
     * Collider & Rigidbody (dynamic): Can move and push
     *  - Ex: player, enemy
     * Collider & no Rigidbody (static): Can push but not move
     *  - Ex: fire area
     * Rigidbody & no Collider (sprite): Can move but not push
     *  - Ex: sun
     *
     * // Require RB
     * Static vs Static (ignore)
     * Dynamic vs Static
     * Dynamic vs Dynamic
     */
    private void detectCollisions() {
        for (int i = 0; i < colliders.size(); i++) {
            // Set j to i + 1 to avoid duplicate collisions between two objects and checking against self
            for (int j = i + 1; j < colliders.size(); j++) {
                Collider2D c1 = colliders.get(i);
                Collider2D c2 = colliders.get(j);
                Rigidbody2D r1 = c1.getRigidbody();
                Rigidbody2D r2 = c2.getRigidbody();

                // Ignore collision if no rigidbody
                if (r1 == null || r2 == null)
                    continue;
                    // Ignore collision if both are static
                else if (r1.hasInfiniteMass() && r2.hasInfiniteMass())
                    continue;

                // Get collision info
                CollisionManifold result = c1.getCollisionInfo(c2);
                if (result == null)
                    continue;

                // TODO still send collision events if triggers
                // Add the collisions if neither is a trigger
                if (!c1.isTrigger() && !c2.isTrigger()) {
//                    DebugDraw.drawLine(c1.center(), c2.center(), Colors.RED);
//                    DebugDraw.drawVector(c1.center(), result.getNormal(), Colors.BLACK);
                    collisions.add(result);
                }
            }
        }
    }

    private void resolveStaticCollision(CollisionManifold col, Rigidbody2D r1, Rigidbody2D r2) {
        // Separate objects factoring in mass
//        float massRatio = r2.getMass() / (r1.getMass() + r2.getMass()); // makes massless objects get displaced
        float massRatio = r1.getMass() / (r1.getMass() + r2.getMass()); // allows smaller objects to push around heavy objects
        float depth1 = col.getDepth() * massRatio;
        float depth2 = col.getDepth() * (1 - massRatio);
        r1.getTransform().move(col.getNormal().mul(-depth1));
        r2.getTransform().move(col.getNormal().mul(depth2));
    }

    private void resolveDynamicCollision(CollisionManifold col, Rigidbody2D r1, Rigidbody2D r2) {
        // Precalculated / Known Values
        float sumInvMass = r1.getInvMass() + r2.getInvMass();
        float elasticity = MathUtils.avg(col.getSelf().getBounce(), col.getOther().getBounce()); // Coefficient of restitution
        float friction = MathUtils.avg(col.getSelf().getFriction(), col.getOther().getFriction());

        Vec2 normal = col.getNormal(); // Collision direction
        Vec2 tangent = new Vec2(-normal.y, normal.x); // Collision plane
        int numContacts = col.countContacts();

        for (int j = 0; j < IMPULSE_ITERATIONS; j++) {
            // Sum up total impulse of collision
            Vec2 accumImpulse = new Vec2();
            float accumAngImpulse1 = 0;
            float accumAngImpulse2 = 0;

            for (int i = 0; i < numContacts; i++) {
                // Relative velocity at contact point
                Vec2 contact = col.getContact(i);
                Vec2 vel1 = r1.getPointVelocity(contact);
                Vec2 vel2 = r2.getPointVelocity(contact);
                Vec2 relativeVel = vel1.sub(vel2);

                // Normal (separation) impulse
                float collisionVel = relativeVel.dot(normal); // Velocity along collision normal
                if (collisionVel < 0f) // Stop if moving away or stationary
                    break;
                // Apply impulse at contact points evenly
                float normalImpulse = -(1f + elasticity) * collisionVel / sumInvMass / numContacts;
                if (MathUtils.equals(normalImpulse, 0)) // Don't apply tiny impulses
                    return;

                // Tangent (friction) impulse
                /*
                 * TODO static vs dynamic
                 * Coulomb's law (static/dynamic friction)
                 * F_f ≤ mu*F_n
                 * Clamp the friction magnitude to the normal magnitude
                 * Use dynamic friction if J_f ≥ mu*J_n
                 */
                float frictionVel = relativeVel.dot(tangent);
                float tangentImpulse = -(1f + elasticity) * frictionVel / sumInvMass / numContacts;
                tangentImpulse = MathUtils.clamp(tangentImpulse, -normalImpulse * friction, normalImpulse * friction);

                // Angular impulse
                Vec2 contactImpulse = normal.mul(normalImpulse).add(tangent.mul(tangentImpulse)); // impulse at this point
                accumImpulse = accumImpulse.add(contactImpulse);
                accumAngImpulse1 += contact.sub(r1.getPosition()).cross(contactImpulse); // need to calculate this after impulse
                accumAngImpulse2 += contact.sub(r2.getPosition()).cross(contactImpulse.mul(-1));
            }

            // Transfer momentum
            r1.addImpulse(accumImpulse);
            r2.addImpulse(accumImpulse.mul(-1));
            r1.addAngularImpulse(accumAngImpulse1);
            r2.addAngularImpulse(accumAngImpulse2);
        }
    }

    // Can't push objects after colliding, velocity slows to 0.
    // javidx9 solution
    private void applyImpulse2(Rigidbody2D r1, Rigidbody2D r2, CollisionManifold collision) {

        Vec2 vel1 = r1.getVelocity();
        Vec2 vel2 = r2.getVelocity();
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

        r1.setVelocity(new Vec2(vx1, vy1));
        r2.setVelocity(new Vec2(vx2, vy2));
    }

    // Game Object Methods

    public void addObject(GameObject o) {
        Rigidbody2D rb = o.getComponent(Rigidbody2D.class);
        // TODO register force method
        // TODO rules for registration (e.g. if object is static, ignore)
        // TODO or just apply forces based on tags

        if (rb != null) {
            bodies.add(rb);
            if (rb.isFollowsGravity())
                forceRegistry.add(new ForceRegistration(gravityForce, rb));
        }

        Collider2D c = o.getComponent(Collider2D.class);
        if (c != null)
            colliders.add(c);
    }

    public void removeObject(GameObject o) {
        bodies.remove(o.getComponent(Rigidbody2D.class));
        colliders.remove(o.getComponent(Collider2D.class));
        // remove from force registry
    }

    public void setScene(Scene newScene) {
        bodies.clear();
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
