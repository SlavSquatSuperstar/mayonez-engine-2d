package slavsquatsuperstar.sandbox.physics;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.BoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.CircleCollider;
import slavsquatsuperstar.mayonez.scripts.*;

public class CollisionTest extends PhysicsTestScene {

    public CollisionTest(String name) {
        super(name, 0);
    }

    public static void main(String[] args) {
        Game.loadScene(new CollisionTest("Collision Test Scene"));
        Game.start();
    }

    @Override
    protected void init() {
        GameObject player = new GameObject("Player Shape", new Vec2(25, 60)) {
            @Override
            protected void init() {
                float radius = 4f;
                float size = 8f;
                float speed = 2f;
                addComponent(new CircleCollider(radius));
                addComponent(new Rigidbody2D(radius * radius * MathUtils.PI / 10f));
//                addComponent(new AlignedBoxCollider2D(new Vec2(size, size)));
//                addComponent(new BoxCollider2D(new Vec2(size, size)));
//                addComponent(new Rigidbody2D(size * size / 10f).setFollowsGravity(true));
                addComponent(new MouseFlick(MoveMode.VELOCITY, "right mouse", 15, false));
                addComponent(new KeyMovement(MoveMode.IMPULSE, speed / 2));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                addComponent(new Script() {
                    private Rigidbody2D rb;

                    @Override
                    public void start() {
                        getCollider().setMaterial(PhysicsTestScene.TEST_MATERIAL);
                        rb = getRigidbody();
                    }

                    @Override
                    public void update(float dt) {
                        if (KeyInput.keyDown("Q"))
                            rb.addAngularImpulse(-speed);
                        else if (KeyInput.keyDown("E"))
                            rb.addAngularImpulse(speed);
                    }
                });
            }
        };
        addObject(player);

        addObject(new GameObject("Ground", new Vec2(getWidth() / 2f, 1)) {
            @Override
            protected void init() {
                addComponent(new Rigidbody2D(0));
                addComponent(new AlignedBoxCollider2D(new Vec2(getWidth(), 2)));
            }
        });
        addObject(new GameObject("Left Ramp", new Vec2(20, 40)) {
            @Override
            protected void init() {
                transform.rotate(-40);
                addComponent(new Rigidbody2D(0));
                addComponent(new BoxCollider2D(new Vec2(20, 4)));
            }
        });
        addObject(new GameObject("Right Ramp", new Vec2(90, 40)) {
            @Override
            protected void init() {
                transform.rotate(25);
                addComponent(new Rigidbody2D(0));
                addComponent(new BoxCollider2D(new Vec2(24, 4)));
            }
        });

        // Add Other Test Objects
        addObject(createCircle(5, new Vec2(90, 50), PhysicsTestScene.TEST_MATERIAL));
        addObject(createCircle(2, new Vec2(10, 30), PhysicsTestScene.TEST_MATERIAL));
        addObject(createOBB(5, 10, new Vec2(40, 40), -30, PhysicsTestScene.TEST_MATERIAL));
        addObject(createOBB(10, 6, new Vec2(70, 30), -45, PhysicsTestScene.TEST_MATERIAL));
        addObject(createOBB(3, 15, new Vec2(90, 20), 90, PhysicsTestScene.TEST_MATERIAL));
    }

}
