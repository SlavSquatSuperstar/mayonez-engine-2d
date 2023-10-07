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
public class SandboxObject extends GameObject {

    private static final float DENSITY = 2f;
    private final float rotation;

    public SandboxObject(String name, Vec2 position, float rotation) {
        super(name, new Transform(position));
        this.rotation = rotation;
    }

    @Override
    protected void init() {
        transform.setRotation(rotation);
        addComponent(new DrawPhysicsInformation());
    }

    public SandboxObject addPhysics(Collider collider, Color color, boolean fill, PhysicsMaterial material) {
        addComponent(collider);
        addComponent(new Rigidbody(collider.getMass(DENSITY)).setMaterial(material));
        addComponent(new ShapeSprite(color, fill));
        return this;
    }

    public SandboxObject addStaticPhysics(Vec2 size) {
        addComponent(new BoxCollider(size));
        addComponent(new Rigidbody(0f));
        addComponent(new ShapeSprite(Colors.DARK_GRAY, true));
        return this;
    }

    public SandboxObject addMouseMovement() {
        addComponent(new KeepInScene(KeepInScene.Mode.BOUNCE));
        addComponent(new DragAndDrop("left mouse"));
        addComponent(new MouseFlick("right mouse", 25f,
                MoveMode.VELOCITY, false));
        return this;
    }

    public SandboxObject setLifetime(float lifeTime) {
        addComponent(new DestroyAfterDuration(lifeTime));
        return this;
    }

}
