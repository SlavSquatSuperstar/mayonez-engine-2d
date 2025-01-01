package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;
import mayonez.scripts.mouse.*;
import mayonez.scripts.movement.*;
import slavsquatsuperstar.demos.physics.scripts.DrawPhysicsInformation;
import slavsquatsuperstar.demos.physics.scripts.MouseFlick;

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

    SandboxObject addPhysics(Collider collider, Color color, PhysicsMaterial material) {
        addComponent(collider);
        addComponent(new Rigidbody(collider.getMass(DENSITY)).setMaterial(material));
        addComponent(new ShapeSprite(color, false));
        return this;
    }

    SandboxObject addStaticPhysics(Vec2 size) {
        addComponent(new BoxCollider(size));
        addComponent(new Rigidbody(0f).setMaterial(PhysicsSandboxScene.NORMAL_MATERIAL));
        addComponent(new ShapeSprite(Colors.DARK_GRAY, true));
        return this;
    }

    SandboxObject addMouseMovement() {
        addComponent(new DragAndDrop("left mouse"));
        addComponent(new MouseFlick("right mouse", 25f,
                MoveMode.VELOCITY, false));
        return this;
    }

    SandboxObject setLifetime(float lifeTime) {
        addComponent(new DestroyAfterDuration(lifeTime));
        return this;
    }

}
