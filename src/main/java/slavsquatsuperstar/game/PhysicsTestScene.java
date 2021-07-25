package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.components.scripts.*;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.CircleCollider;
import slavsquatsuperstar.mayonez.physics2d.primitives.Collider2D;
import slavsquatsuperstar.util.MathUtils;

import java.awt.*;

public class PhysicsTestScene extends Scene {

    public PhysicsTestScene(String name) {
        super(name, Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT, 10);
        setGravity(new Vector2(0, 0));
    }

    @Override
    protected void init() {
        Logger.log(null);

        addObject(new GameObject("Debug Draw", new Vector2()) {
            @Override
            public void render(Graphics2D g2) {
//                Line2D line = new Line2D(new Vector2(20, 00), Game.mouse().getPosition());
//                DebugDraw.drawVector(line.toVector().mul(1), line.start, Colors.BLACK);

                for (GameObject o : getScene().getObjects(null)) {
                    Collider2D col = o.getComponent(Collider2D.class);
                    if (col != null) {
                        Color color = Colors.BLACK;
                        if (col instanceof CircleCollider)
                            color = Colors.BLUE;
                        else if (col instanceof AlignedBoxCollider2D)
                            color = Colors.LIGHT_GREEN;

                        // Draw velocity vector
                        DebugDraw.drawVector(col.getRigidbody().velocity().mul(5), col.center(), color);

                        // Draw Shape
//                        if (col.raycast(new Ray2D(line), null, 0))
//                            color = Colors.ORANGE;
                        DebugDraw.drawShape(col, color);
                    }
                }
            }
        });

        addObject(new GameObject("Player Shape", new Vector2(30, 10)) {
            @Override
            protected void init() {
                float radius = 3.5f;
                float size = 5f;
//                addComponent(new CircleCollider(radius));
                addComponent(new AlignedBoxCollider2D(new Vector2(size, size)));
                addComponent(new Rigidbody2D(radius * radius * MathUtils.PI));
//                addComponent(new MouseFlick("left mouse", 10, false));
//                addComponent(new FollowMouse(MoveMode.FOLLOW_MOUSE, 2));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new KeyMovement(MoveMode.IMPULSE, 1f));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
            }
        });

        addObject(createCircle(2, new Vector2(10, 10)));
        addObject(createCircle(5, new Vector2(30, 30)));

        addObject(createAABB(12, 8, new Vector2(50, 20)));
        addObject(createAABB(5, 10, new Vector2(70, 20)));

        // Randomly generate Circles and AABBs
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0)
                addObject(createCircle(MathUtils.random(2, 5), new Vector2(MathUtils.random(0, width), MathUtils.random(0, height))));
            else
                addObject(createAABB(MathUtils.random(4, 10), MathUtils.random(4, 10), new Vector2(MathUtils.random(0, width), MathUtils.random(0, height))));
        }

    }

    public GameObject createCircle(float radius, Vector2 position) {
        return new GameObject("Circle", position) {
            @Override
            protected void init() {
                addComponent(new CircleCollider(radius));
                addComponent(new Rigidbody2D(radius * radius * MathUtils.PI));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse", false));
            }
        };
    }

    public GameObject createAABB(float width, float height, Vector2 position) {
        return new GameObject("Rectangle", position) {
            @Override
            protected void init() {
                addComponent(new AlignedBoxCollider2D(new Vector2(width, height)));
                addComponent(new Rigidbody2D(width * height));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse", false));
            }
        };
    }
}
