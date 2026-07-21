package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.mouse.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A prefab shape with a collider and rigid body that can be moved with the mouse.
 *
 * @author SlavSquatSuperstar
 */
class SandboxObject extends GameObject {

    private static final float DENSITY = 2f;
    private final List<Component> sandboxComponents;

    SandboxObject(String name, Vec2 position, float rotation) {
        super(name, new Transform(position, rotation));
        sandboxComponents = new ArrayList<>();
    }

    @Override
    protected void init() {
        addComponent(new DrawPhysicsInformation());
        sandboxComponents.forEach(this::addComponent);
        sandboxComponents.clear();
    }

    private SandboxObject addCollider(Collider collider, Color color, boolean fill) {
        sandboxComponents.add(collider);
        sandboxComponents.add(new ShapeSprite(color, fill));
        return this;
    }

    private SandboxObject addRigidbody(float mass, PhysicsMaterial material) {
        sandboxComponents.add(new Rigidbody(mass).setMaterial(material));
        return this;
    }

    SandboxObject addPhysics(Collider collider, Color color, PhysicsMaterial material) {
        return addCollider(collider, color, false)
                .addRigidbody(collider.getMass(DENSITY), material);
    }

    SandboxObject addStaticPhysics(Collider collider, PhysicsMaterial material) {
        return addCollider(collider, Colors.DARK_GRAY, true)
                .addRigidbody(0f, material);
    }

    SandboxObject addMouseMovement() {
        sandboxComponents.add(new DragAndDrop("left mouse"));
        sandboxComponents.add(new MouseFlick("right mouse", 25f) {
            @Override
            protected void flickGameObject(Vec2 input, Rigidbody rb) {
                rb.addVelocity(input);
            }
        });
        return this;
    }

    SandboxObject addInitialVelocity(Vec2 velocity) {
        sandboxComponents.add(new Script() {
            @Override
            protected void start() {
                var rb = getRigidbody();
                if (rb != null) rb.setVelocity(velocity);
            }
        });
        return this;
    }

    SandboxObject addSandboxComponent(Component component) {
        sandboxComponents.add(component);
        return this;
    }

}
