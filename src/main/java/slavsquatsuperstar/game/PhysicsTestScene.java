package slavsquatsuperstar.game;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.*;
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
        GameObject player = new GameObject("Player Shape", new Vec2(30, 10)) {
            @Override
            protected void init() {
                float radius = 4f;
                float size = 8f;
//                addComponent(new CircleCollider(radius));
//                addComponent(new Rigidbody2D(radius * radius * MathUtils.PI));
//                addComponent(new AlignedBoxCollider2D(new Vec2(size, size)));
                addComponent(new BoxCollider2D(new Vec2(size, size)));
                addComponent(new Rigidbody2D(size * size));
                addComponent(new MouseFlick("right mouse", 1, false));
                addComponent(new KeyMovement(MoveMode.IMPULSE, 1));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new Script() {
                    private float spin = 2;

                    @Override
                    public void update(float dt) {
                        if (KeyInput.keyDown("Q"))
                            transform.rotate(-spin);
                        if (KeyInput.keyDown("E"))
                            transform.rotate(spin);
                    }
                });
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
            }
        };
        addObject(player);

        addObject(new GameObject("Debug Draw", new Vec2()) {
            RaycastResult rc = new RaycastResult();

            @Override
            public void render(Graphics2D g2) {
                Edge2D ray = new Edge2D(player.transform.position, MouseInput.getPosition());
                DebugDraw.drawLine(ray.start, ray.end, Colors.BLACK);

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
                            DebugDraw.drawVector(col.center(), col.getRigidbody().velocity().mul(5), color);

                        // Draw Shape
                        DebugDraw.drawShape(col, color);

                        // Draw Ray
                        if (col.raycast(new Ray2D(ray), rc, ray.getLength())) {
                            DebugDraw.drawPoint(rc.getContact(), Colors.RED);
                            DebugDraw.drawVector(rc.getContact(), rc.getNormal(), Colors.RED);
                        }
                    }
                }
            }
        });

        addObject(createCircle(2, new Vec2(10, 10)));
        addObject(createCircle(5, new Vec2(30, 30)));

        addObject(createAABB(12, 8, new Vec2(50, 20)));
        addObject(createAABB(5, 10, new Vec2(70, 20)));

        addObject(createOBB(10, 6, new Vec2(70, 40), 45));
        addObject(createOBB(5, 5, new Vec2(90, 40), 0));

        // Randomly generate shapes
        for (int i = 0; i < 6; i++) {
            if (i % 3 == 0)
                addObject(createCircle(MathUtils.random(2, 5), new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight()))));
            else if (i % 2 == 0)
                addObject(createAABB(MathUtils.random(4, 10), MathUtils.random(4, 10), new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight()))));
            else
                addObject(createOBB(MathUtils.random(4, 10), MathUtils.random(4, 10), new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight())), MathUtils.random(0, 90)));
        }

    }

    public GameObject createCircle(float radius, Vec2 position) {
        return new GameObject("Circle", position) {
            @Override
            protected void init() {
                addComponent(new CircleCollider(radius));
                addComponent(new Rigidbody2D(radius * radius * MathUtils.PI));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new MouseFlick("right mouse", 1, false));
            }
        };
    }

    public GameObject createAABB(float width, float height, Vec2 position) {
        return new GameObject("AABB Rectangke", position) {
            @Override
            protected void init() {
                addComponent(new AlignedBoxCollider2D(new Vec2(width, height)));
                addComponent(new Rigidbody2D(width * height));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new MouseFlick("right mouse", 1, false));
            }
        };
    }

    public GameObject createOBB(float width, float height, Vec2 position, float rotation) {
        return new GameObject("OBB Rectangle", position) {
            @Override
            protected void init() {
                transform.rotate(rotation);
                addComponent(new BoxCollider2D(new Vec2(width, height)));
                addComponent(new Rigidbody2D(width * height));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new MouseFlick("right mouse", 1, false));
            }
        };
    }
}
