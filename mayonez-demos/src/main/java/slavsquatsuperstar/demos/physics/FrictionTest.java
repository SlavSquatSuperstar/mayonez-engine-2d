package slavsquatsuperstar.demos.physics;

import slavsquatsuperstar.mayonez.math.Vec2;
import slavsquatsuperstar.mayonez.scripts.*;
import slavsquatsuperstar.mayonez.util.Colors;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.physics.colliders.BallCollider;
import slavsquatsuperstar.mayonez.physics.colliders.Collider;

public class FrictionTest extends PhysicsTestScene {

    public FrictionTest(String name) {
        super(name, 2);
    }

    @Override
    protected void init() {
        super.init();
        addObject(new GameObject("Ground", new Vec2(getWidth() / 2f, 1)) {
            @Override
            protected void init() {
                addComponent(new Rigidbody(0).setFixedRotation(true));
                addComponent(new BoxCollider(new Vec2(getWidth(), 2)).setDebugDraw(Colors.BLACK, false));
            }
        });

        addObject(new GameObject("Sloped Ramp", new Vec2(30, 50)) {
            @Override
            protected void init() {
                transform.rotate(-25);
                addComponent(new Rigidbody(0).setMaterial(NORMAL_MATERIAL));
                addComponent(new BoxCollider(new Vec2(40, 5)).setDebugDraw(Colors.BLACK, false));
            }
        });

        addObject(createBall(new Vec2(2), new Vec2(15, 65), NORMAL_MATERIAL));

        addObject(new GameObject("Player Circle", new Vec2(25, 8)) {
            @Override
            protected void init() {
                float speed = 2f;
//                Collider2D collider = new BoxCollider2D(new Vec2(6, 6));
                Collider collider = new BallCollider(3f).setDebugDraw(Colors.BLUE, false);
                addComponent(collider);
                addComponent(new Rigidbody(collider.getMass(DENSITY)));
                addComponent(new MouseFlick(MoveMode.VELOCITY, "right mouse", 15, false));
                addComponent(new DragAndDrop("left mouse"));
                addComponent(new KeepInScene(new Vec2(), new Vec2(getScene().getSize()), KeepInScene.Mode.BOUNCE));
                addComponent(new Script() {
                    private Rigidbody rb;

                    @Override
                    public void start() {
                        rb = getRigidbody().setMaterial(PhysicsTestScene.NORMAL_MATERIAL);
                    }

                    @Override
                    public void update(float dt) {
                        if (KeyInput.keyDown("q"))
                            rb.applyAngularImpulse(speed);
                        else if (KeyInput.keyDown("e"))
                            rb.applyAngularImpulse(-speed);

                        rb.applyImpulse(new Vec2(KeyInput.getAxis("horizontal") * speed,
                                KeyInput.getAxis("vertical") * speed));
                    }
                });
            }
        });
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.setScene(new FrictionTest("Friction Test Scene"));
        Mayonez.start();
    }
}
