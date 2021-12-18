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
 * A simulation of the physics world inside the game.
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

        // Integrate force
        bodies.forEach(rb -> rb.integrateForce(dt));

        // Resolve collisions
        collisions.forEach(col -> {
            Rigidbody2D r1 = col.getSelf().getRigidbody();
            Rigidbody2D r2 = col.getOther().getRigidbody();
            resolveDynamicCollision(col, r1, r2); // Simulate an impact
            resolveStaticCollision(col, r1, r2); // Correct positions
        });

        // Integrate velocity
        bodies.forEach(rb -> rb.integrateVelocity(dt));
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
            // Want to avoid duplicate collisions between two objects and checking against self
            for (int j = i + 1; j < colliders.size(); j++) {
                Collider2D c1 = colliders.get(i);
                Collider2D c2 = colliders.get(j);

                if (c1.isStatic() && c2.isStatic()) continue; // Don't check for collision if both are static
                CollisionManifold result = c1.getCollisionInfo(c2); // Get collision info
                if (result == null) continue;

                // TODO still send collision events if triggers
                // Add the collisions if neither is a trigger
                if (!c1.isTrigger() && !c2.isTrigger()) {
                    collisions.add(result);
                }
            }
        }
    }

    /**
     * Correct positions of objects if one or neither collider has an infinite-mass rigidbody.
     */
    private static void resolveStaticCollision(CollisionManifold col, Rigidbody2D r1, Rigidbody2D r2) {
        float mass1 = r1 == null ? 0 : r1.getMass();
        float mass2 = r2 == null ? 0 : r2.getMass();
        if (mass1 + mass2 == 0) return; // Should not both be static

        // Separate objects factoring in mass
        float massPercent = mass1 / (mass1 + mass2);
        float depth1 = col.getDepth() * massPercent;
        float depth2 = col.getDepth() * (1 - massPercent);

        // Displace bodies to correct position
        r1.getTransform().move(col.getNormal().mul(-depth1));
        r2.getTransform().move(col.getNormal().mul(depth2));
    }

    private static void resolveDynamicCollision(CollisionManifold col, Rigidbody2D r1, Rigidbody2D r2) {
        // Collision Information
        Vec2 normal = col.getNormal(); // Collision direction
        Vec2 tangent = normal.getNormal(); // Collision plane, may be positive or negative to actual tangent
        int numContacts = col.countContacts();

//        DebugDraw.drawVector(r1.getPosition(), normal, Colors.BLUE);

        // Mass Checks
        float invMass1 = r1 == null ? 0 : r1.getInvMass();
        float invMass2 = r2 == null ? 0 : r2.getInvMass();
        float sumInvMass = invMass1 + invMass2;
        if (invMass1 + invMass2 == 0) return; // Should not both be static

        float initialKE = PhysicsUtils.getKineticEnergy(r1) + PhysicsUtils.getKineticEnergy(r2);

        // Physics Properties
        PhysicsMaterial mat1 = col.getSelf().getMaterial();
        PhysicsMaterial mat2 = col.getOther().getMaterial();
        float restitution = PhysicsMaterial.combineBounce(mat1, mat2);
        float sFric = PhysicsMaterial.combineStaticFriction(mat1, mat2);
        float kFric = PhysicsMaterial.combineKineticFriction(mat1, mat2);

        for (int j = 0; j < IMPULSE_ITERATIONS; j++) {
            // Sum up total impulse of collision
            Vec2 sumImp = new Vec2(); // impulse at this point
            float sumAngImp1 = 0;
            float sumAngImp2 = 0;

            for (int i = 0; i < numContacts; i++) {
                // Radius from center of mass to contact
                // TODO transferring too much angular momentum, check radii
                Vec2 contact = col.getContact(i);
                Vec2 rad1 = contact.sub(r1.getPosition());
                Vec2 rad2 = contact.sub(r2.getPosition());

//                DebugDraw.drawPoint(contact, Colors.GREEN);
//                DebugDraw.drawVector(r1.getPosition(), rad1, Colors.BLUE);
//                DebugDraw.drawVector(r2.getPosition(), rad2, Colors.RED);

                // Relative velocity at contact point (linear + angular velocity)
                Vec2 vel1 = r1.getPointVelocity(contact);
                Vec2 vel2 = r2.getPointVelocity(contact);
                Vec2 relativeVel = vel1.sub(vel2);

                // Normal (separation) impulse
                float normalVel = relativeVel.dot(normal); // Velocity along collision normal
                if (normalVel < 0f) return; // Stop if moving away or stationary
                // Apply impulse at contact points evenly
                float normalImp = -(1f + restitution) * normalVel / (sumInvMass * numContacts);

                // Transfer angular momentum
                sumAngImp1 -= rad1.cross(normal.mul(relativeVel));
                sumAngImp2 += rad2.cross(normal.mul(relativeVel));

                // Tangential (friction) impulse
                /*
                 * Coulomb's law (static/dynamic friction)
                 * F_f ≤ mu*F_n
                 * Clamp the friction magnitude to the normal magnitude
                 * Use dynamic friction if J_f ≥ mu*J_n
                 */
                float tangentVel = relativeVel.dot(tangent);
                float tangentImp = -tangentVel / (sumInvMass * numContacts);
                if (MathUtils.equals(tangentImp, 0f)) tangentImp = 0; // Ignore tiny friction impulses

                // Coulomb's Law: use kinetic friction if overcome static friction
                if (Math.abs(tangentImp) > Math.abs(normalImp * sFric))
                    tangentImp = -Math.signum(tangentImp) * normalImp * kFric;
                tangentImp = MathUtils.clamp(tangentImp, -normalImp * kFric, normalImp * kFric);

                // Calculate angular impulse from collision
                Vec2 contactImp = normal.mul(normalImp);
                Vec2 frictionImp = tangent.mul(tangentImp);
                sumImp = sumImp.add(contactImp.add(frictionImp));
                sumAngImp1 += rad1.cross(contactImp);
                sumAngImp2 -= rad2.cross(contactImp);
            }

            // Transfer Momentum
            if (r1 != null) {
                r1.addImpulse(sumImp);
                r1.addAngularImpulse(sumAngImp1);
            }
            if (r2 != null) {
                r2.addImpulse(sumImp.mul(-1));
                r2.addAngularImpulse(sumAngImp2);
            }

        }

        float finalKE = PhysicsUtils.getKineticEnergy(r1) + PhysicsUtils.getKineticEnergy(r2);
//        Logger.log("Initial KE: %.3f, Final KE: %.3f", initialKE, finalKE);
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
