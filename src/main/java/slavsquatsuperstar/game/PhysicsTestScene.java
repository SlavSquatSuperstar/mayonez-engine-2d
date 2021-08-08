package slavsquatsuperstar.game;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
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

    public PhysicsTestScene(String name) {
        super(name, Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT, 10);
        setGravity(new Vec2(0, 0));
        setBackground(Colors.WHITE);
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
                        if (col instanceof CircleCollider)
                            color = Colors.BLUE;
                        else if (col instanceof AlignedBoxCollider2D)
                            color = Colors.LIGHT_GREEN;
                        else if (col instanceof BoxCollider2D)
                            color = Colors.ORANGE;

                        // Draw velocity vector
                        if (!col.isStatic())
                            DebugDraw.drawVector(col.center(), col.getRigidbody().getVelocity().div(10), color);

                        // Draw Shape
                        DebugDraw.drawShape(col, color);
                    }
                }
            }
        });

        GameObject player = new GameObject("Player Shape", new Vec2(30, 10)) {
            @Override
            protected void init() {
                float radius = 4f;
                float size = 8f;
                float speed = 1f;
//                addComponent(new CircleCollider(radius));
//                addComponent(new Rigidbody2D(radius * radius * MathUtils.PI / 20f));
//                addComponent(new AlignedBoxCollider2D(new Vec2(size, size)));
                addComponent(new BoxCollider2D(new Vec2(size, size)));
                addComponent(new Rigidbody2D(size * size / 20f));
                addComponent(new MouseFlick("right mouse", 15, false));
                addComponent(new KeyMovement(MoveMode.IMPULSE, speed));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                addComponent(new Script() {
                    private Rigidbody2D rb;

                    @Override
                    public void start() {
                        rb = getComponent(Rigidbody2D.class);
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

        addObject(createCircle(2, new Vec2(10, 10)));
        addObject(createCircle(5, new Vec2(30, 30)));
        addObject(createAABB(12, 8, new Vec2(50, 20)));
        addObject(createAABB(5, 10, new Vec2(70, 20)));
        addObject(createOBB(10, 6, new Vec2(70, 40), 45));
        addObject(createOBB(5, 5, new Vec2(90, 40), 0));

        // Randomly generate shapes
        for (int i = 0; i < 6; i++) {
            switch (i % 3) {
                case 0:
                    addObject(createCircle(MathUtils.random(2, 5), new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight()))));
                    break;
                case 1:
                    addObject(createAABB(MathUtils.random(4, 10), MathUtils.random(4, 10), new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight()))));
                    break;
                case 2:
                    addObject(createOBB(MathUtils.random(4, 10), MathUtils.random(4, 10), new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight())), MathUtils.random(0, 90)));
                    break;
            }
        }
    }

    public GameObject createCircle(float radius, Vec2 position) {
        return new GameObject("Circle", position) {
            @Override
            protected void init() {
                addComponent(new CircleCollider(radius));
                addComponent(new Rigidbody2D(radius * radius * MathUtils.PI / 20f));
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
                addComponent(new AlignedBoxCollider2D(new Vec2(width, height)));
                addComponent(new Rigidbody2D(width * height / 20f));
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
                addComponent(new BoxCollider2D(new Vec2(width, height)));
                addComponent(new Rigidbody2D(width * height / 20f));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new MouseFlick("right mouse", 15, false));
            }
        };
    }
}
