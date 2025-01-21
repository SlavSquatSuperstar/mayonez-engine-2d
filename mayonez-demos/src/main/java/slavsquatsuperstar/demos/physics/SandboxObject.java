package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;
import mayonez.scripts.mouse.*;

/**
 * A prefab shape with a collider and rigid body that can be moved with the mouse.
 *
 * @author SlavSquatSuperstar
 */
class SandboxObject extends GameObject {

    private static final float DENSITY = 2f;

    SandboxObject(String name, Vec2 position, float rotation) {
        super(name, new Transform(position, rotation));
    }

    @Override
    protected void init() {
        addComponent(new DrawPhysicsInformation());
    }

    private SandboxObject addCollider(Collider collider, Color color, boolean fill) {
        addComponent(collider);
        addComponent(new ShapeSprite(color, fill));
        return this;
    }

    private SandboxObject addRigidbody(float mass, PhysicsMaterial material) {
        addComponent(new Rigidbody(mass).setMaterial(material));
        return this;
    }

    SandboxObject addPhysics(Collider collider, Color color, PhysicsMaterial material) {
        return addCollider(collider, color, false)
                .addRigidbody(collider.getMass(DENSITY), material);
    }

    SandboxObject addStaticPhysics(Vec2 size, PhysicsMaterial material) {
        return addCollider(new BoxCollider(size), Colors.DARK_GRAY, true)
                .addRigidbody(0f, material);
    }

    SandboxObject addMouseMovement() {
        addComponent(new DragAndDrop("left mouse"));
        addComponent(new MouseFlick("right mouse", 25f) {
            @Override
            protected void flickGameObject(Vec2 input, Rigidbody rb) {
                rb.addVelocity(input);
            }
        });
        return this;
    }

    SandboxObject addInitialVelocity(Vec2 velocity) {
        addComponent(new Script() {
            @Override
            protected void start() {
                var rb = getRigidbody();
                if (rb != null) rb.setVelocity(velocity);
            }
        });
        return this;
    }

    SandboxObject setLifetime(float lifeTime) {
        addComponent(new DestroyAfterDuration(lifeTime));
        return this;
    }

}
