package slavsquatsuperstar.mayonez.physics;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.physics.colliders.Collider;
import slavsquatsuperstar.mayonez.physics.collision.CollisionInfo;
import slavsquatsuperstar.mayonez.physics.collision.CollisionSolver;
import slavsquatsuperstar.mayonez.physics.collision.Collisions;
import slavsquatsuperstar.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * A simulation containing bodies that approximate real-world physics.
 * <br>
 * Thanks to GamesWithGabe's <a href="https://youtube.com/playlist?list=PLtrSb4XxIVbpZpV65kk73OoUcIrBzoSiO"> Coding a 2D
 * Physics Engine playlist</a>for explaining the math and logic.
 *
 * @author SlavSquatSuperstar
 */
public final class PhysicsWorld {

    public final static float GRAVITY_CONSTANT = 9.8f;
    public final static int IMPULSE_ITERATIONS = Preferences.getImpulseIterations();

    // Collisions
    private final List<Rigidbody> bodies; // physical objects in the world
    private final List<Collider> colliders; // shapes in the world
    private final List<Pair<Collider, Collider>> broadphase; // possible broad phase collisions
    private final List<CollisionSolver> collisions; // narrow phase collisions

    // Forces
    private final List<ForceRegistration> forceRegistry;
    private final ForceGenerator gravityForce;
    private Vec2 gravity; // acceleration due to gravity

    public PhysicsWorld() {
        bodies = new ArrayList<>();
        colliders = new ArrayList<>();
        broadphase = new ArrayList<>();
        collisions = new ArrayList<>();

        forceRegistry = new ArrayList<>();
        setGravity(new Vec2(0, -PhysicsWorld.GRAVITY_CONSTANT));
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
        broadphase.clear();
        collisions.clear();

        // TODO Pre-collision optimizations and spatial partitioning
        // TODO Create collision events
        // Detect Collisions and Create Collision Events
        detectBroadPhase();
        detectNarrowPhase();

        // Update environmental forces
        forceRegistry.forEach(fr -> fr.fg.applyForce(fr.rb, dt));

        // Integrate force
        bodies.forEach(rb -> rb.integrateForce(dt));

        // Resolve collisions
        collisions.forEach(CollisionSolver::solve);

        // Integrate velocity
        bodies.forEach(rb -> rb.integrateVelocity(dt));
    }

    // Collision Helper Methods

    private void detectBroadPhase() {
        for (int i = 0; i < colliders.size(); i++) {
            Collider c1 = colliders.get(i);
            c1.setCollisionResolved(false); // reset flag

            // Avoid duplicate collisions between two objects and checking against self
            for (int j = i + 1; j < colliders.size(); j++) {
                Collider c2 = colliders.get(j);
                c2.setCollisionResolved(false); // reset flag

                if (c1.getParent().hasTag("Ignore Collisions") || c2.getParent().hasTag("Ignore Collisions"))
                    continue;
                if (c1.isStatic() && c2.isStatic()) continue; // Don't check for collision if both are static

                if (Collisions.detectCollision(c1.getMinBounds(), c2.getMinBounds()))
                    broadphase.add(new Pair<>(c1, c2)); // Check for detailed collision later
            }
        }
    }

    private void detectNarrowPhase() {
        for (Pair<Collider, Collider> pair : broadphase) {
            Collider c1 = pair.getLeft();
            Collider c2 = pair.getRight();

            CollisionInfo collision = c1.getCollisionInfo(c2); // Get collision info
            if (collision == null) continue;

            // Send collision callbacks if both are not triggers
            if (!c1.isTrigger() && !c2.isTrigger()) {
                c1.onCollision(c2.getParent());
                c2.onCollision(c1.getParent());
                if (c1.getIgnoreCurrentCollision() || c2.getIgnoreCurrentCollision()) {
                    c1.setIgnoreCurrentCollision(false);
                    c2.setIgnoreCurrentCollision(false);
                    continue; // Stop if either object has called ignore collision
                }
                collisions.add(new CollisionSolver(collision)); // Only solve if neither are triggers
            } else if (c1.isTrigger()) {
                c2.onTrigger(c1);
            } else if (c2.isTrigger()) {
                c1.onTrigger(c2);
            }
        }
    }

    // Game Object Methods

    public void addObject(GameObject o) {
        Rigidbody rb = o.getComponent(Rigidbody.class);
        // TODO register force method
        // TODO rules for registration (e.g. if object is static, ignore)
        // TODO or just apply forces based on tags

        if (rb != null) {
            bodies.add(rb);
            if (rb.isFollowsGravity())
                forceRegistry.add(new ForceRegistration(gravityForce, rb));
        }

        Collider c = o.getComponent(Collider.class);
        if (c != null) colliders.add(c);
    }

    public void removeObject(GameObject o) {
        bodies.remove(o.getComponent(Rigidbody.class));
        colliders.remove(o.getComponent(Collider.class));
        // TODO remove from force registry
    }

    public void setScene(Scene newScene) {
        bodies.clear();
        colliders.clear();
        newScene.getObjects().forEach(this::addObject);
    }

    public void stop() {
        bodies.clear();
        colliders.clear();
        collisions.clear();
    }

    // Getters and Setters

    private Vec2 getGravity() {
        return gravity;
    }

    public void setGravity(Vec2 gravity) {
        this.gravity = gravity;
    }
}
