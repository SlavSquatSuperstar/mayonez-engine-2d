package slavsquatsuperstar.demos.physics;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.math.Vec2;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.BallCollider;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.physics.colliders.Collider;
import slavsquatsuperstar.mayonez.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.scripts.KeepInScene;
import slavsquatsuperstar.mayonez.scripts.MouseFlick;
import slavsquatsuperstar.mayonez.scripts.MoveMode;
import slavsquatsuperstar.mayonez.util.Colors;
import slavsquatsuperstar.mayonez.util.Logger;

public class AngularResolutionTest extends PhysicsTestScene {

    public AngularResolutionTest(String name) {
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
                addComponent(rb = new Rigidbody(col.getMass(5f)).setFollowsGravity(true));
                addComponent(new KeepInScene(new Vec2(), getScene().getSize(), KeepInScene.Mode.STOP));
                addComponent(new DragAndDrop("left mouse"));
                addComponent(new MouseFlick(MoveMode.VELOCITY, "right mouse", 15, false));
                addComponent(new Script() {
                    @Override
                    public void update(float dt) {
                        rb.applyAngularImpulse(10f * -KeyInput.getAxis("horizontal"));
                        if (KeyInput.keyPressed("space")) {
//                            rb.setAngVelocity(0f);
                            Logger.log("%s: %.4f", col, rb.getAngMass());
                        }
                    }
                });
            }
        };
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.start(new AngularResolutionTest("Angular Resolution Test"));
    }

}
