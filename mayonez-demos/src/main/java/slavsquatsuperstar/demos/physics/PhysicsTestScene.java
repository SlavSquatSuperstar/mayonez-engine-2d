package slavsquatsuperstar.demos.physics;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.scripts.*;
import slavsquatsuperstar.util.Colors;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.DebugDraw;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.physics.PhysicsMaterial;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.physics.colliders.BallCollider;
import slavsquatsuperstar.mayonez.physics.colliders.Collider;

import java.awt.*;

public abstract class PhysicsTestScene extends Scene {

    static final PhysicsMaterial NORMAL_MATERIAL = new PhysicsMaterial(0.4f, 0.4f, 0.3f);
    static final PhysicsMaterial BOUNCY_MATERIAL = new PhysicsMaterial(0f, 0f, 1f);
    static final PhysicsMaterial STICKY_MATERIAL = new PhysicsMaterial(1f, 1f, 0f);
    final float DENSITY = 1f;
    final int NUM_SHAPES;

    private boolean enabledGravity = false;

    public PhysicsTestScene(String name, int numShapes) {
        super(name, Preferences.getScreenWidth(), Preferences.getScreenHeight(), 10);
        setBackground(Colors.WHITE);
        NUM_SHAPES = numShapes;
        setGravity(new Vec2());
        getCamera().setPosition(getSize().mul(0.5f));
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
                    DebugDraw.drawVector(col.getRigidbody().getPosition(), col.getRigidbody().getTransform().getDirection(), Colors.BLACK);
//                    DebugDraw.drawShape(col, color); // Draw Shape
                }
            }
        }
    }

    @Override
    protected void onUserUpdate(float dt) {
        getCamera().setPosition(getSize().mul(0.5f));

        if (KeyInput.keyPressed("space")) {
//            System.out.println("pressed");
            enabledGravity = !enabledGravity;
        } else if (KeyInput.keyDown("space")) {
//            System.out.println("held");
        }

//        if (MouseInput.buttonPressed("left mouse")) {
//            System.out.println("pressed");
//        } else if (MouseInput.buttonDown("left mouse")) {
//            System.out.println("held");
//        }

        if (enabledGravity) setGravity(new Vec2(0, -18));
        else setGravity(new Vec2());
    }

    protected final GameObject createBall(Vec2 size, Vec2 position, PhysicsMaterial material) {
        return new GameObject("Ball", position) {
            @Override
            protected void init() {
                Collider circle = new BallCollider(size).setMaterial(material).setDebugDraw(Colors.BLUE, false);
                addComponent(circle);
                addComponent(new Rigidbody(circle.getMass(DENSITY)));
                addComponent(new KeepInScene(new Vec2(), getScene().getSize(), KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse"));
                addComponent(new MouseFlick(MoveMode.VELOCITY, "right mouse", 15, false));
            }
        };
    }

    protected final GameObject createBox(Vec2 size, Vec2 position, float rotation, PhysicsMaterial material) {
        return new GameObject("Box", position) {
            @Override
            protected void init() {
//                transform.rotate(rotation);
                Collider box = new BoxCollider(size).setMaterial(material).setDebugDraw(Colors.ORANGE, false);
                addComponent(box);
                addComponent(new Rigidbody(box.getMass(DENSITY)));
                addComponent(new KeepInScene(new Vec2(), getScene().getSize(), KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse"));
                addComponent(new MouseFlick(MoveMode.VELOCITY, "right mouse", 15, false));
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
}
