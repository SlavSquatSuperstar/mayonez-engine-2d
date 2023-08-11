package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.util.*;

/**
 * For testing dynamic movement and collision resolution.
 *
 * @author SlavSquatSupertar
 */
public class PhysicsSandboxScene extends Scene {

    private static final PhysicsMaterial NORMAL_MATERIAL = new PhysicsMaterial(0.4f, 0.4f, 0.3f);
    private static final PhysicsMaterial BOUNCY_MATERIAL = new PhysicsMaterial(0f, 0f, 1f);
    private static final PhysicsMaterial STICKY_MATERIAL = new PhysicsMaterial(1f, 1f, 0f);

    private boolean enabledGravity;

    public PhysicsSandboxScene(String name) {
        super(name, Preferences.getScreenWidth(), Preferences.getScreenHeight(), 10);
    }

    @Override
    protected void init() {
        setBackground(Colors.WHITE);
        setGravityEnabled(true);

        // Add Test Objects
        addObject(createStaticBox("Left Ramp", new Vec2(-25, 20), new Vec2(36, 4), -20));
        addObject(createBall(new Vec2(8), new Vec2(-25, 30), STICKY_MATERIAL));

        addObject(createStaticBox("Right Ramp", new Vec2(15, -5), new Vec2(60, 4), 15));
        addObject(createBall(new Vec2(10), new Vec2(35, 15), NORMAL_MATERIAL));
        addObject(createBox(new Vec2(6, 6), new Vec2(0, 5), 30, BOUNCY_MATERIAL));

        addObject(createStaticBox("Ground", new Vec2(0, -getHeight() * 0.5f + 1), new Vec2(getWidth(), 2), 0));
        addObject(createBox(new Vec2(10, 6), new Vec2(-30, -5), -45, NORMAL_MATERIAL));
    }

    @Override
    protected void onUserUpdate(float dt) {
        // Toggle Gravity
        if (KeyInput.keyPressed("space")) {
            enabledGravity = !enabledGravity;
            setGravityEnabled(enabledGravity);
        }

        // Create Random Shapes
        if (!KeyInput.keyDown("left shift")) {
            if (KeyInput.keyPressed("1")) {
                addObject(createRandomShape(MouseInput.getPosition(), 1));
            } else if (KeyInput.keyPressed("2")) {
                addObject(createRandomShape(MouseInput.getPosition(), 2));
            } else if (KeyInput.keyPressed("3")) {
                addObject(createRandomShape(MouseInput.getPosition(), 3));
            } else if (KeyInput.keyPressed("4")) {
                addObject(createRandomShape(MouseInput.getPosition(), 4));
            }
        }
    }

    private void setGravityEnabled(boolean enabled) {
        if (enabled) setGravity(new Vec2(-0, -PhysicsWorld.GRAVITY_CONSTANT));
        else setGravity(new Vec2());
    }

    // GameObject Helper Methods

    protected final GameObject createBall(Vec2 size, Vec2 position, PhysicsMaterial material) {
        return new SandboxObject("Ball", position, 0f)
                .addPhysics(new BallCollider(size), Colors.BLUE, false, material)
                .addMouseMovement();
    }

    protected final GameObject createBox(Vec2 size, Vec2 position, float rotation, PhysicsMaterial material) {
        return new SandboxObject("Box", position, rotation)
                .addPhysics(new BoxCollider(size), Colors.ORANGE, false, material)
                .addMouseMovement();
    }

    protected final GameObject createStaticBox(String name, Vec2 position, Vec2 size, float rotation) {
        return new SandboxObject(name, position, rotation)
                .addStaticPhysics(size);
    }

    protected final GameObject createRandomShape(Vec2 position, int type) {
        var name = getNameFromType(type);
        var rotation = Random.randomAngle();
        var col = getColliderFromType(type);

        return new SandboxObject(name, position, rotation)
                .addPhysics(col, Random.randomColor(), false, randomMaterial())
                .addMouseMovement()
                .setLifetime(Random.randomFloat(15f, 20f));
    }

    private static String getNameFromType(int type) {
        String name;
        switch (type) {
            case 1 -> name = "Random Ball";
            case 2 -> name = "Random Box";
            case 3 -> name = "Random Triangle";
            case 4 -> name = "Random Polygon";
            default -> name = "Unknown Shape";
        }
        return name;
    }

    private static Collider getColliderFromType(int type) {
        Collider col;
        switch (type) {
            case 1 -> col = new BallCollider(Random.randomVector(new Vec2(4f), new Vec2(10f)));
            case 2 -> col = new BoxCollider(Random.randomVector(new Vec2(4f), new Vec2(10f)));
            case 3 -> col = new PolygonCollider(3, Random.randomFloat(3f, 6f));
            case 4 -> col = new PolygonCollider(Random.randomInt(5, 8), Random.randomFloat(3f, 6f));
            default -> col = new BoxCollider(new Vec2(1f));
        }
        return col;
    }

    private PhysicsMaterial randomMaterial() {
        return new PhysicsMaterial(Random.randomFloat(0f, 1f), Random.randomFloat(0f, 1f), Random.randomFloat(0f, 1f));
    }

}
