package slavsquatsuperstar.sandbox;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Component;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.BoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.CircleCollider;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;
import slavsquatsuperstar.mayonez.renderer.DebugDraw;
import slavsquatsuperstar.mayonez.scripts.*;

import java.awt.*;

public class PhysicsTestScene extends Scene {

    public PhysicsTestScene() {
        super("Physics Test Scene", Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT, 10);
        setBackground(Colors.WHITE);
//        setGravity(new Vec2());
    }

    public static void main(String[] args) {
        Game game = Game.instance();
        game.start();
        Game.loadScene(new PhysicsTestScene());
    }

    @Override
    protected void init() {
        addObject(new GameObject("Debug Draw", new Vec2()) {
            @Override
            public void render(Graphics2D g2) {
                for (GameObject o : getScene().getObjects(null)) {
                    if (o.name.equals("Camera"))
                        continue;

                    Collider2D col = o.getComponent(Collider2D.class);
                    if (col != null) {
                        Color color = Colors.BLACK;
                        if (!col.isStatic()) {
                            if (col instanceof CircleCollider)
                                color = Colors.BLUE;
                            else if (col instanceof AlignedBoxCollider2D)
                                color = Colors.LIGHT_GREEN;
                            else if (col instanceof BoxCollider2D)
                                color = Colors.ORANGE;
                        }

                        // Draw velocity vector
                        if (!col.isStatic())
                            DebugDraw.drawVector(col.center(), col.getRigidbody().getVelocity().div(10), color);

                        // Draw Shape
                        DebugDraw.drawShape(col, color);
                    }
                }
            }
        });

        GameObject player = new GameObject("Player Shape", new Vec2(20, 10)) {
            @Override
            protected void init() {
                float radius = 4f;
                float size = 8f;
                float speed = 1f;
//                addComponent(new CircleCollider(radius));
//                addComponent(new Rigidbody2D(radius * radius * MathUtils.PI / 10f));
//                addComponent(new AlignedBoxCollider2D(new Vec2(size, size)));
                addComponent(new BoxCollider2D(new Vec2(size, size)));
                addComponent(new Rigidbody2D(size * size / 10f).setFollowsGravity(false));
                addComponent(new MouseFlick("right mouse", 15, false));
                addComponent(new KeyMovement(MoveMode.IMPULSE, speed / 2));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                addComponent(new Script() {
                    private Rigidbody2D rb;

                    @Override
                    public void start() {
                        getCollider().setBounce(0.3f);
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
                addComponent(new Component() {
                    private BoxCollider2D box;

                    @Override
                    public void start() {
                        box = getComponent(BoxCollider2D.class);
                    }

                    @Override
                    public void render(Graphics2D g2) {
                        DebugDraw.drawVector(transform.position, transform.getDirection(), Colors.BLACK);
                    }
                });
            }
        };
        addObject(player);

        addObject(new GameObject("Ground", new Vec2(getWidth() / 2f, getHeight() - 1)) {
            @Override
            protected void init() {
                addComponent(new Rigidbody2D(0));
                addComponent(new AlignedBoxCollider2D(new Vec2(getWidth(), 2)));
            }
        });
        addObject(new GameObject("Left Ramp", new Vec2(20, 20)) {
            @Override
            protected void init() {
                transform.rotate(40);
                addComponent(new Rigidbody2D(0));
                addComponent(new BoxCollider2D(new Vec2(20, 4)));
            }
        });
        addObject(new GameObject("Right Ramp", new Vec2(90, 30)) {
            @Override
            protected void init() {
                transform.rotate(-25);
                addComponent(new Rigidbody2D(0));
                addComponent(new BoxCollider2D(new Vec2(24, 4)));
            }
        });

        addObject(createCircle(5, new Vec2(10, 30)));
        addObject(createCircle(2, new Vec2(90, 10)));
        addObject(createAABB(12, 8, new Vec2(50, 20)));
        addObject(createOBB(5, 10, new Vec2(30, 40), 30));
        addObject(createOBB(10, 6, new Vec2(70, 30), 45));
        addObject(createOBB(3, 15, new Vec2(90, 40), 90));
    }

    public GameObject createCircle(float radius, Vec2 position) {
        return new GameObject("Circle", position) {
            @Override
            protected void init() {
                addComponent(new CircleCollider(radius).setBounce(0.3f));
                addComponent(new Rigidbody2D(radius * radius * MathUtils.PI / 10f));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new MouseFlick("right mouse", 15, false));
            }
        };
    }

    public GameObject createAABB(float width, float height, Vec2 position) {
        return new GameObject("AABB Rectangle", position) {
            @Override
            protected void init() {
                addComponent(new AlignedBoxCollider2D(new Vec2(width, height)).setBounce(0.3f));
                addComponent(new Rigidbody2D(width * height / 10f));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new MouseFlick("right mouse", 15, false));
            }
        };
    }

    public GameObject createOBB(float width, float height, Vec2 position, float rotation) {
        return new GameObject("OBB Rectangle", position) {
            @Override
            protected void init() {
                transform.rotate(rotation);
                addComponent(new BoxCollider2D(new Vec2(width, height)).setBounce(0.3f));
                addComponent(new Rigidbody2D(width * height / 10f));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new MouseFlick("right mouse", 15, false));
            }
        };
    }
}
