package slavsquatsuperstar.demos.physics;

import mayonez.GameObject;
import mayonez.Mayonez;
import mayonez.Script;
import mayonez.input.KeyInput;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BallCollider;
import mayonez.physics.colliders.BoxCollider;
import mayonez.physics.colliders.Collider;
import mayonez.scripts.DragAndDrop;
import mayonez.scripts.KeepInScene;
import mayonez.scripts.MouseFlick;
import mayonez.scripts.MoveMode;
import mayonez.graphics.Colors;

public class AngularMotionTest extends PhysicsTestScene {

    public AngularMotionTest(String name) {
        super(name, 8);
    }

    @Override
    protected void init() {
        super.init();
        addObject(createStaticBox("Floor", new Vec2(getWidth() / 2f, 1), new Vec2(getWidth(), 2), 0));

//        for (int i = 0; i < NUM_SHAPES; i++) {
//            if (i % 3 == 0) {
//                addObject(createCircle(MathUtils.random(3f, 6f), new Vec2(MathUtils.random(0, getWidth()),
//                        MathUtils.random(0, getHeight())), NORMAL_MATERIAL));
//            } else {
//            addObject(createOBB(MathUtils.random(5f, 12f), MathUtils.random(5f, 12f),
//                    new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight())), MathUtils.random(0, 90), NORMAL_MATERIAL));
//            }
//        }

        addObject(createShape(new Vec2(30, 50), new BallCollider(2.5f)));
        addObject(createShape(new Vec2(50, 50), new BallCollider(5)));
        addObject(createShape(new Vec2(70, 50), new BallCollider(7.5f)));

        addObject(createShape(new Vec2(30, 30), new BoxCollider(new Vec2(5))));
        addObject(createShape(new Vec2(50, 30), new BoxCollider(new Vec2(10))));
        addObject(createShape(new Vec2(70, 30), new BoxCollider(new Vec2(15))));
    }

    private static GameObject createShape(Vec2 position, Collider col) {
        return new GameObject("Shape", position) {
            @Override
            protected void init() {
                Rigidbody rb;
                addComponent(col.setDebugDraw(Colors.BLUE, false));
                float mass = col.getMass(5f);
                addComponent(rb = new Rigidbody(5).setFollowsGravity(true));
                addComponent(new KeepInScene(new Vec2(), getScene().getSize(), KeepInScene.Mode.STOP));
                addComponent(new DragAndDrop("left mouse"));
                addComponent(new MouseFlick(MoveMode.VELOCITY, "right mouse", 15, false));
                addComponent(new Script() {
                    @Override
                    public void update(float dt) {
                        rb.applyAngularImpulse(10 * -KeyInput.getAxis("horizontal"));
//                        if (KeyInput.keyPressed("space")) {
//                            rb.setAngVelocity(0f);
//                            Logger.log("%s: %.4f", col, rb.getAngMass());
//                        }
                    }
                });
            }
        };
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.start(new AngularMotionTest("Angular Resolution Test"));
    }

}
