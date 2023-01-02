package mayonez.physics;

import mayonez.GameObject;
import mayonez.Preferences;
import mayonez.Scene;
import mayonez.math.Vec2;
import mayonez.physics.colliders.Collider;
import mayonez.physics.resolution.CollisionSolver;
import mayonez.physics.resolution.Manifold;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simulation containing bodies that approximate real-world physics.
 * <br>
 * Thanks to GamesWithGabe's <a href="https://youtube.com/playlist?list=PLtrSb4XxIVbpZpV65kk73OoUcIrBzoSiO"> Coding a 2D
 * Physics Engine playlist</a>for explaining the math and logic.
 *
 * @author SlavSquatSuperstar
 */
public class PhysicsWorld {

    public final static float GRAVITY_CONSTANT = 9.8f;
    public final static int IMPULSE_ITERATIONS = Preferences.getImpulseIterations();
    private Vec2 gravity; // acceleration due to gravity

    // Bodies and Collisions
    private final List<Rigidbody> bodies; // physical objects in the world
    private final List<Collider> colliders; // shapes in the world
    private final Set<CollisionListener> listeners; // all collision listeners
    private final List<CollisionSolver> collisions; // confirmed narrowphase collisions

    public PhysicsWorld() {
        bodies = new ArrayList<>();
        colliders = new ArrayList<>();
        listeners = new HashSet<>();
        collisions = new ArrayList<>();
        setGravity(new Vec2(0, -PhysicsWorld.GRAVITY_CONSTANT));
    }

    public void start() {
        bodies.clear();
        colliders.clear();
        listeners.clear();
        collisions.clear();
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
     * Send collision events x2
     * Resolve dynamic collisions x2
     *
     * Integrate forces and velocities
     */
    public void step(float dt) {
        collisions.clear();

        // Apply Gravity and Update Bodies
        bodies.forEach(rb -> {
            if (rb.isFollowsGravity()) rb.applyForce(gravity.mul(rb.getMass()));
            rb.integrateForce(dt);
            rb.integrateVelocity(dt);
        });

        // Detect Collisions and Create Collision Events
        detectBroadPhase(); // TODO Pre-collision optimizations and spatial partitioning
        detectNarrowPhase();

        // Resolve Collisions
        collisions.forEach(CollisionSolver::solve);
    }

    // Collision Helper Methods

    /**
     * Get the collision listener that matches the with two colliders
     *
     * @param c1 the first collider
     * @param c2 the second collider
     * @return an existing listener or a new listener
     */
    private CollisionListener getListener(Collider c1, Collider c2) {
        for (CollisionListener lis : listeners)
            if (lis.match(c1, c2)) return lis;
        return new CollisionListener(c1, c2);
    }

    /**
     * Detect potential collisions between bounding boxes while avoiding expensive contact calculations.
     */
    private void detectBroadPhase() {
        for (int i = 0; i < colliders.size(); i++) {
            // Avoid duplicate collisions between two objects and checking against self
            for (int j = i + 1; j < colliders.size(); j++) {
                Collider c1 = colliders.get(i);
                Collider c2 = colliders.get(j);
                c1.setCollisionResolved(false); // reset flags
                c2.setCollisionResolved(false);

                if (c1.getGameObject().hasTag("Ignore Collisions") || c2.getGameObject().hasTag("Ignore Collisions")) continue;
                if (c1.isStatic() && c2.isStatic()) continue; // Don't check for collision if both are static

                CollisionListener lis = getListener(c1, c2);
                if (lis.checkBroadphase()) // TODO only add if broadphase checks out
                    listeners.add(lis);
//                if (Collisions.checkCollision(c1.getMinBounds(), c2.getMinBounds()))
//                    listeners.add(new CollisionListener(c1, c2)); // Check for detailed collision later
            }
        }
    }

    /**
     * Check broadphase pairs for collisions and calculate contact points.
     */
    private void detectNarrowPhase() {
        for (CollisionListener lis : listeners) {
            Manifold collision = lis.checkNarrowphase(); // Get contacts
            if (collision == null) continue;
            Collider c1 = lis.c1;
            Collider c2 = lis.c2;

//            if (c1.isTrigger() && c2.isTrigger()) continue; // Ignore if both are triggers
//            // Send collision callbacks
//            c1.onCollision(c2);
//            c2.onCollision(c1);
            // Don't resolve if either object called ignore collision
            if (c1.getIgnoreCurrentCollision() || c2.getIgnoreCurrentCollision()) {
                c1.setIgnoreCurrentCollision(false);
                c2.setIgnoreCurrentCollision(false);
                continue;
            }
            collisions.add(new CollisionSolver(c1, c2, collision)); // Resolve collisions
        }
    }

    // Game Object Methods

    public void addObject(GameObject o) {
        Rigidbody rb = o.getComponent(Rigidbody.class);
        if (rb != null) bodies.add(rb);

        Collider c = o.getComponent(Collider.class);
        if (c != null) colliders.add(c);
    }

    public void removeObject(GameObject o) {
        bodies.remove(o.getComponent(Rigidbody.class));
        Collider c = o.getComponent(Collider.class);
        colliders.remove(c);
        // remove all listeners with collider
        listeners.stream().filter(lis -> lis.match(c)).toList().forEach(listeners::remove);
    }

    public void setScene(Scene newScene) {
        bodies.clear();
        colliders.clear();
        newScene.getObjects().forEach(this::addObject);
    }

    public void stop() {
        bodies.clear();
        colliders.clear();
        listeners.clear();
        collisions.clear();
    }

    // Getters and Setters

    public void setGravity(Vec2 gravity) {
        this.gravity = gravity;
    }
}
