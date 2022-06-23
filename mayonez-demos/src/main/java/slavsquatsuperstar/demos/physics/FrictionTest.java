package slavsquatsuperstar.demos.physics;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Colors;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.BoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.CircleCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;
import slavsquatsuperstar.mayonez.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.scripts.KeepInScene;
import slavsquatsuperstar.mayonez.scripts.MouseFlick;
import slavsquatsuperstar.mayonez.scripts.MoveMode;

public class FrictionTest extends PhysicsTestScene {

    public FrictionTest(String name) {
        super(name, 2);
    }

    @Override
    protected void init() {
        addObject(new GameObject("Ground", new Vec2(getWidth() / 2f, 1)) {
            @Override
            protected void init() {
                addComponent(new Rigidbody2D(0).setFixedRotation(true));
                addComponent(new BoxCollider2D(new Vec2(getWidth(), 2)).setDrawColor(Colors.BLACK));
            }
        });

        addObject(new GameObject("Sloped Ramp", new Vec2(30, 50)) {
            @Override
            protected void init() {
                transform.rotate(-25);
                addComponent(new Rigidbody2D(0));
                addComponent(new BoxCollider2D(new Vec2(40, 5)).setMaterial(NORMAL_MATERIAL).setDrawColor(Colors.BLACK));
            }
        });

        addObject(createCircle(2, new Vec2(15, 65), NORMAL_MATERIAL));

        addObject(new GameObject("Player Circle", new Vec2(25, 8)) {
            @Override
            protected void init() {
                float speed = 2f;
//                Collider2D collider = new BoxCollider2D(new Vec2(6, 6));
                Collider2D collider = new CircleCollider2D(3f).setDrawColor(Colors.BLUE);
                addComponent(collider);
                addComponent(new Rigidbody2D(collider.getMass(DENSITY)));
                addComponent(new MouseFlick(MoveMode.VELOCITY, "right mouse", 15, false));
                addComponent(new DragAndDrop("left mouse"));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                addComponent(new Script() {
                    private Rigidbody2D rb;

                    @Override
                    public void start() {
                        getCollider().setMaterial(PhysicsTestScene.NORMAL_MATERIAL);
                        rb = getRigidbody();
                    }

                    @Override
                    public void update(float dt) {
                        if (KeyInput.keyDown("Q"))
                            rb.addAngularImpulse(speed);
                        else if (KeyInput.keyDown("E"))
                            rb.addAngularImpulse(-speed);

                        rb.addImpulse(new Vec2(KeyInput.getAxis("horizontal") * speed, 0));
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
