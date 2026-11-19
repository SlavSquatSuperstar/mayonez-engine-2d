package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.math.*;
import mayonez.math.shapes.Ellipse;
import mayonez.math.shapes.Rectangle;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.DestroyAfterDuration;
import slavsquatsuperstar.demos.ShapeUtils;

/**
 * Creates prefab physics sandbox objects.
 *
 * @author SlavSquatSuperstar
 */
final class SandboxObjectPrefabs {

    private static final float MIN_LENGTH = 4f;
    private static final float MAX_LENGTH = 10f;
    private static final float MIN_RADIUS = MIN_LENGTH / 2f;
    private static final float MAX_RADIUS = MAX_LENGTH / 2f;

    private SandboxObjectPrefabs() {
    }

    static SandboxObject createBall(Vec2 position, Vec2 size, PhysicsMaterial material) {
        return new SandboxObject("Ball", position, 0f)
                .addPhysics(new Collider(new Ellipse(new Vec2(size))), Colors.BLUE, material)
                .addMouseMovement();
    }

    static SandboxObject createBox(
            Vec2 position, Vec2 size, float rotation, PhysicsMaterial material
    ) {
        return new SandboxObject("Box", position, rotation)
                .addPhysics(new Collider(new Rectangle(size)), Colors.ORANGE, material)
                .addMouseMovement();
    }

    static SandboxObject createStaticBox(
            String name, Vec2 position, Vec2 size, float rotation, PhysicsMaterial material
    ) {
        return new SandboxObject(name, position, rotation)
                .addStaticPhysics(new Collider(new Rectangle(size)), material);
    }

    static Node createRandomShape(Vec2 position, int type) {
        var name = getNameFromType(type);
        var rotation = Random.randomAngle();
        var col = getColliderFromType(type);

        return new SandboxObject(name, position, rotation)
                .addPhysics(col, Colors.randomColor(), randomMaterial())
                .addMouseMovement()
                .addSandboxComponent(
                        new DestroyAfterDuration(Random.randomFloat(15f, 20f))
                );
    }

    private static String getNameFromType(int type) {
        String name;
        switch (type) {
            case 1 -> name = "Random Box";
            case 2 -> name = "Random Ball";
            case 3 -> name = "Random Polygon";
            case 4 -> name = "Random Edge";
            default -> name = "Unknown Shape";
        }
        return name;
    }

    private static Collider getColliderFromType(int type) {
        Collider col;
        switch (type) {
            case 1 -> col = new Collider(ShapeUtils.getRandomRectangle(new Vec2(), MIN_LENGTH, MAX_LENGTH));
            case 2 -> col = new Collider(ShapeUtils.getRandomEllipse(new Vec2(), MIN_LENGTH, MAX_LENGTH));
            case 3 -> col = new Collider(ShapeUtils.getRandomPolygon(new Vec2(), MIN_RADIUS, MAX_RADIUS));
            case 4 -> col = new Collider(ShapeUtils.getRandomEdge(new Vec2(), MIN_LENGTH, MAX_LENGTH));
            default -> col = new Collider(new Rectangle(new Vec2(1f)));
        }
        return col;
    }

    private static PhysicsMaterial randomMaterial() {
        return new PhysicsMaterial(Random.randomFloat(0f, 1f), Random.randomFloat(0f, 1f), Random.randomFloat(0f, 1f));
    }

}
