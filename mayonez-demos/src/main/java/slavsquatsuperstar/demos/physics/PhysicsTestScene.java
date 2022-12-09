package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.Color;
import mayonez.graphics.Colors;
import mayonez.input.KeyInput;
import mayonez.math.Vec2;
import mayonez.physics.PhysicsMaterial;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BallCollider;
import mayonez.physics.colliders.BoxCollider;
import mayonez.physics.colliders.Collider;
import mayonez.scripts.movement.DragAndDrop;
import mayonez.scripts.KeepInScene;
import mayonez.scripts.movement.MouseFlick;
import mayonez.scripts.movement.MoveMode;

import java.awt.*;

public abstract class PhysicsTestScene extends Scene {

    static final PhysicsMaterial NORMAL_MATERIAL = new PhysicsMaterial(0.4f, 0.4f, 0.3f);
    static final PhysicsMaterial BOUNCY_MATERIAL = new PhysicsMaterial(0f, 0f, 1f);
    static final PhysicsMaterial STICKY_MATERIAL = new PhysicsMaterial(1f, 1f, 0f);
    final float DENSITY = 2f;
    final int NUM_SHAPES;

    private boolean enabledGravity;

    public PhysicsTestScene(String name, int numShapes) {
        super(name, Preferences.getScreenWidth(), Preferences.getScreenHeight(), 10);
        NUM_SHAPES = numShapes;
        setGravity(new Vec2());
        getCamera().setPosition(getSize().mul(0.5f));
    }

    @Override
    protected void init() {
        enabledGravity = false;
    }

    @Override
    public void onUserRender(Graphics2D g2) {
        for (GameObject o : getObjects()) {
            Collider col = o.getComponent(Collider.class);
            if (col != null) {
                Color color = col.getDrawColor();
                // Draw velocity and direction vector
                if (color != null && !col.isStatic()) {
                    DebugDraw.drawPoint(col.center(), Colors.BLACK);
                    DebugDraw.drawVector(col.center(), col.getRigidbody().getVelocity().mul(0.1f), color);
                    DebugDraw.drawVector(col.getRigidbody().getPosition(), col.getRigidbody().getTransform().getRight(), Colors.BLACK);
//                    DebugDraw.drawShape(col, color); // Draw Shape
                }
            }
        }
    }

    @Override
    protected void onUserUpdate(float dt) {
        getCamera().setPosition(getSize().mul(0.5f));

        if (KeyInput.keyPressed("r")) SceneManager.reloadScene(); // reload scene
        else if (KeyInput.keyPressed("1")) SceneManager.setScene("Collision Test");
        else if (KeyInput.keyPressed("2")) SceneManager.setScene("Detection Test");
        else if (KeyInput.keyPressed("3")) SceneManager.setScene("Pool Balls Test");
        else if (KeyInput.keyPressed("3")) SceneManager.setScene("Friction Test");
        else if (KeyInput.keyPressed("4")) SceneManager.setScene("Angular Resolution Test");

        if (KeyInput.keyPressed("space")) {
            enabledGravity = !enabledGravity;
        }

        if (enabledGravity) setGravity(new Vec2(0, -9.8f));
        else setGravity(new Vec2());
    }

    protected final GameObject createBall(Vec2 size, Vec2 position, PhysicsMaterial material) {
        return new GameObject("Ball", position) {
            @Override
            protected void init() {
                Collider circle = new BallCollider(size).setDebugDraw(Colors.BLUE, false);
                addComponent(circle);
                addComponent(new Rigidbody(circle.getMass(DENSITY)).setMaterial(material));
                addComponent(new KeepInScene(new Vec2(), getScene().getSize(), KeepInScene.Mode.BOUNCE));
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
                Collider box = new BoxCollider(size).setDebugDraw(Colors.ORANGE, false);
                addComponent(box);
                addComponent(new Rigidbody(box.getMass(DENSITY)).setMaterial(material));
                addComponent(new KeepInScene(new Vec2(), getScene().getSize(), KeepInScene.Mode.BOUNCE));
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
                addComponent(new BoxCollider(size).setDebugDraw(Colors.DARK_GRAY, true));
            }
        };
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
//        SceneManager.addScene(new CollisionTest("Collision Test"));
        SceneManager.addScene(new DetectionTest("Detection Test"));
        SceneManager.addScene(new PoolBallsTest("Pool Balls Test"));
        SceneManager.addScene(new FrictionTest("Friction Test"));
        SceneManager.addScene(new AngularMotionTest("Angular Motion Test"));
//        Mayonez.start(SceneManager.getScene("Collision Test"));
        Mayonez.start(new CollisionTest("Collision Test"));
    }
}
