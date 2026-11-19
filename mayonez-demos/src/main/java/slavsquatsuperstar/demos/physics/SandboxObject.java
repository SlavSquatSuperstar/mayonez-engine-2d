package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.physics.CollisionEvent;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.DestroyAfterDuration;
import mayonez.scripts.mouse.*;
import slavsquatsuperstar.demos.RandomShapeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static mayonez.math.Random.randomFloat;

/**
 * A prefab shape with a collider and rigid body that can be moved with the mouse.
 *
 * @author SlavSquatSuperstar
 */
class SandboxObject extends Node {

    // Shape Generation
    private static final float MIN_LENGTH = 4f;
    private static final float MAX_LENGTH = 10f;
    private static final RandomShapeGenerator gen = new RandomShapeGenerator(
            Vec2::new, () -> 0f, () -> randomFloat(MIN_LENGTH, MAX_LENGTH)
    );
    private static final float DENSITY = 2f;

    // Shape Types
    private static final SandboxShapeType[] SHAPE_TYPES = {
            new SandboxShapeType("Box", gen::getRandomRectangle),
            new SandboxShapeType("Ball", gen::getRandomEllipse),
            new SandboxShapeType("Polygon", gen::getRandomPolygon),
            new SandboxShapeType("Edge", gen::getRandomEdge),
    };
    static final int NUM_SHAPE_TYPES = SHAPE_TYPES.length;

    // Instance Fields
    private final List<Node> sandboxComponents;
    private final Vec2 initialVelocity;

    SandboxObject(String name, Vec2 position, float rotation) {
        super(name, new Transform(position, rotation));
        sandboxComponents = new ArrayList<>();
        initialVelocity = new Vec2();
    }

    @Override
    protected void init() {
        sandboxComponents.forEach(this::addChild);
        sandboxComponents.clear();
    }

    // Builder Methods

    SandboxObject addPhysics(Shape shape, Color color, PhysicsMaterial material) {
        var shapeSprite = new DrawPhysicsInformation(color, false);
        var collider = new Collider(shape) {
            @Override
            protected void start() {
                super.start();
                getPhysicsBody().addVelocity(initialVelocity);
            }
        };
        sandboxComponents.add(collider);
        sandboxComponents.add(shapeSprite);
        sandboxComponents.add(new Rigidbody(collider.getMass(DENSITY))
                .setMaterial(material));
        return this;
    }

    SandboxObject addStaticPhysics(Shape shape, PhysicsMaterial material) {
        sandboxComponents.add(new Collider(shape));
        sandboxComponents.add(new ShapeSprite(Colors.DARK_GRAY, true));
        sandboxComponents.add(new Rigidbody(0f).setMaterial(material));
        return this;
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
        initialVelocity.set(velocity);
        return this;
    }

    SandboxObject addSandboxComponent(Node component) {
        sandboxComponents.add(component);
        return this;
    }

    // Factory Methods

    static SandboxObject createBall(Vec2 position, Vec2 size, PhysicsMaterial material) {
        return new SandboxObject("Ball", position, 0f)
                .addPhysics(new Ellipse(new Vec2(size)), Colors.BLUE, material)
                .addMouseMovement();
    }

    static SandboxObject createBox(
            Vec2 position, Vec2 size, float rotation, PhysicsMaterial material
    ) {
        return new SandboxObject("Box", position, rotation)
                .addPhysics(new Rectangle(size), Colors.ORANGE, material)
                .addMouseMovement();
    }

    static SandboxObject createStaticBox(
            String name, Vec2 position, Vec2 size, float rotation, PhysicsMaterial material
    ) {
        return new SandboxObject(name, position, rotation)
                .addStaticPhysics(new Rectangle(size), material);
    }

    static Node createRandomShape(Vec2 position, int type) {
        if (type < 0 || type >= NUM_SHAPE_TYPES) return null;

        var name = SHAPE_TYPES[type].name();
        var shape = SHAPE_TYPES[type].getShape();
        var material = new PhysicsMaterial(
                randomFloat(0f, 1f), randomFloat(0f, 1f), randomFloat(0f, 1f)
        );
        return new SandboxObject(name, position, Random.randomAngle())
                .addPhysics(shape, Colors.randomColor(), material)
                .addMouseMovement()
                .addSandboxComponent(
                        new DestroyAfterDuration(randomFloat(15f, 20f))
                );
    }

    // Helper Class

    private record SandboxShapeType(String name, Supplier<Shape> generator) {
        Shape getShape() {
            return generator.get();
        }
    }

}
