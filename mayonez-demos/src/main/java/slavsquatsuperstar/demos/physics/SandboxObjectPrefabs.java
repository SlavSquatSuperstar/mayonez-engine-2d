package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;

/**
 * Creates prefab physics sandbox objects.
 *
 * @author SlavSquatSuperstar
 */
final class SandboxObjectPrefabs {

    private SandboxObjectPrefabs() {
    }

    static SandboxObject createBall(Vec2 position, Vec2 size, PhysicsMaterial material) {
        return new SandboxObject("Ball", position, 0f)
                .addPhysics(new BallCollider(size), Colors.BLUE, material)
                .addMouseMovement();
    }

    static SandboxObject createBox(
            Vec2 position, Vec2 size, float rotation, PhysicsMaterial material
    ) {
        return new SandboxObject("Box", position, rotation)
                .addPhysics(new BoxCollider(size), Colors.ORANGE, material)
                .addMouseMovement();
    }

    static SandboxObject createStaticBox(
            String name, Vec2 position, Vec2 size, float rotation, PhysicsMaterial material
    ) {
        return new SandboxObject(name, position, rotation)
                .addStaticPhysics(size, material);
    }

    static GameObject createRandomShape(Vec2 position, int type) {
        var name = getNameFromType(type);
        var rotation = Random.randomAngle();
        var col = getColliderFromType(type);

        return new SandboxObject(name, position, rotation)
                .addPhysics(col, Colors.randomColor(), randomMaterial())
                .addMouseMovement()
                .setLifetime(Random.randomFloat(15f, 20f));
    }

    private static String getNameFromType(int type) {
        String name;
        switch (type) {
            case 1 -> name = "Random Box";
            case 2 -> name = "Random Ball";
            case 3 -> name = "Random Triangle";
            case 4 -> name = "Random Polygon";
            case 5 -> name = "Random Edge";
            default -> name = "Unknown Shape";
        }
        return name;
    }

    private static Collider getColliderFromType(int type) {
        Collider col;
        switch (type) {
            case 1 -> col = new BoxCollider(Random.randomVector(new Vec2(4f), new Vec2(10f)));
            case 2 -> col = new BallCollider(Random.randomVector(new Vec2(4f), new Vec2(10f)));
            case 3 -> col = new PolygonCollider(3, Random.randomFloat(3f, 6f));
            case 4 -> col = new PolygonCollider(Random.randomInt(5, 8), Random.randomFloat(3f, 6f));
            case 5 -> col = new EdgeCollider(Random.randomInt(4, 12));
            default -> col = new BoxCollider(new Vec2(1f));
        }
        return col;
    }

    private static PhysicsMaterial randomMaterial() {
        return new PhysicsMaterial(Random.randomFloat(0f, 1f), Random.randomFloat(0f, 1f), Random.randomFloat(0f, 1f));
    }

}
