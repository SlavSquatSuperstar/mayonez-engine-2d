package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.Colors;
import mayonez.graphics.sprites.ShapeSprite;
import mayonez.input.KeyInput;
import mayonez.input.MouseInput;
import mayonez.math.Random;
import mayonez.math.Vec2;
import mayonez.physics.PhysicsMaterial;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BallCollider;
import mayonez.physics.colliders.BoxCollider;
import mayonez.physics.colliders.Collider;
import mayonez.physics.colliders.PolygonCollider;
import mayonez.scripts.KeepInScene;
import mayonez.scripts.movement.DragAndDrop;
import mayonez.scripts.movement.MoveMode;

/**
 * For testing dynamic movement and collision resolution.
 *
 * @author SlavSquatSupertar
 */
public class SandboxScene extends Scene {

    private static final PhysicsMaterial NORMAL_MATERIAL = new PhysicsMaterial(0.4f, 0.4f, 0.3f);
    private static final PhysicsMaterial BOUNCY_MATERIAL = new PhysicsMaterial(0f, 0f, 1f);
    private static final PhysicsMaterial STICKY_MATERIAL = new PhysicsMaterial(1f, 1f, 0f);
    private static final float DENSITY = 2f;

    private boolean enabledGravity;

    public SandboxScene(String name) {
        super(name, Preferences.getScreenWidth(), Preferences.getScreenHeight(), 10);
    }

    @Override
    protected void init() {
        setGravity(new Vec2());
        enabledGravity = false;

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
        if (KeyInput.keyPressed("space")) enabledGravity = !enabledGravity;
        if (enabledGravity) setGravity(new Vec2(0, -9.8f));
        else setGravity(new Vec2());

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

    @Override
    public void onUserRender() {
        // Draw center, velocity, direction vector
        for (var obj : getObjects()) {
            var col = obj.getComponent(Collider.class);
            var spr = obj.getComponent(ShapeSprite.class);
            if (col != null && spr != null) {
                var color = spr.color;
                if (color != null && !col.isStatic()) {
                    DebugDraw.drawPoint(col.center(), Colors.BLACK);
                    DebugDraw.drawVector(col.center(), col.getRigidbody().getVelocity().mul(0.1f), color);
                    DebugDraw.drawVector(col.getRigidbody().getPosition(), col.getRigidbody().getTransform().getRight(), Colors.BLACK);
                }
            }
        }
    }

    // Helper Methods
    // TODO make prefab class

    protected final GameObject createBall(Vec2 size, Vec2 position, PhysicsMaterial material) {
        return new GameObject("Ball", position) {
            @Override
            protected void init() {
                var circle = new BallCollider(size);
                addComponent(circle);
                addComponent(new ShapeSprite(Colors.BLUE, false));
                addComponent(new Rigidbody(circle.getMass(DENSITY)).setMaterial(material));
                addComponent(new KeepInScene(KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse"));
                addComponent(new MouseFlick(MoveMode.VELOCITY, "right mouse", 25f, false));
            }
        };
    }

    protected final GameObject createBox(Vec2 size, Vec2 position, float rotation, PhysicsMaterial material) {
        return new GameObject("Box", position) {
            @Override
            protected void init() {
                transform.rotate(rotation);
                var box = new BoxCollider(size);
                addComponent(box);
                addComponent(new ShapeSprite(Colors.ORANGE, false));
                addComponent(new Rigidbody(box.getMass(DENSITY)).setMaterial(material));
                addComponent(new KeepInScene(KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse"));
                addComponent(new MouseFlick(MoveMode.VELOCITY, "right mouse", 25, false));
            }
        };
    }

    protected final GameObject createStaticBox(String name, Vec2 position, Vec2 size, float rotation) {
        return new GameObject(name, position) {
            @Override
            protected void init() {
                transform.rotate(rotation);
                addComponent(new Rigidbody(0f));
                addComponent(new BoxCollider(size));
                addComponent(new ShapeSprite(Colors.DARK_GRAY, true));
            }
        };
    }

    protected final GameObject createRandomShape(Vec2 position, int type) {
        String name;
        if (type == 1) name = "Random Ball";
        else if (type == 2) name = "Random Box";
        else if (type == 3) name = "Random Triangle";
        else if (type == 4) name = "Random Polygon";
        else return null;

        return new GameObject(name, position) {
            @Override
            protected void init() {
                transform.setRotation(Random.randomFloat(0f, 360f));
                // balls are not rendered correctly if rotated

                Collider col;
                switch (type) {
                    case 1 -> col = new BallCollider(Random.randomVector(new Vec2(4f), new Vec2(10f)));
                    case 2 -> col = new BoxCollider(Random.randomVector(new Vec2(4f), new Vec2(10f)));
                    case 3 -> col = new PolygonCollider(3, Random.randomFloat(3f, 6f));
                    default -> col = new PolygonCollider(Random.randomInt(5, 8), Random.randomFloat(3f, 6f));
                }

                addComponent(col);
                addComponent(new Rigidbody(col.getMass(DENSITY)).setMaterial(randomMaterial()));
                addComponent(new ShapeSprite(Random.randomColor(), false));
                addComponent(new KeepInScene(KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse"));
                addComponent(new MouseFlick(MoveMode.VELOCITY, "right mouse", 25f, false));
                addComponent(new Script() {
                    private float lifetime;

                    @Override
                    public void start() {
                        lifetime = Random.randomFloat(15f, 20f);
                    }

                    @Override
                    public void update(float dt) {
                        lifetime -= dt;
                        if (lifetime <= 0) setDestroyed();
                    }
                });
            }
        };
    }

    private PhysicsMaterial randomMaterial() {
        return new PhysicsMaterial(Random.randomFloat(0f, 1f), Random.randomFloat(0f, 1f), Random.randomFloat(0f, 1f));
    }
}
