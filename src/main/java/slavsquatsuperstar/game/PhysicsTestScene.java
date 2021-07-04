package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.components.CircleSprite;
import slavsquatsuperstar.mayonez.components.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.components.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.CircleCollider;
import slavsquatsuperstar.mayonez.physics2d.primitives.Line2D;

import java.awt.*;

public class PhysicsTestScene extends Scene {

    Line2D line = new Line2D(new Vector2(300, 300), new Vector2(600, 300));

    public PhysicsTestScene(String name) {
        super(name);
        setGravity(new Vector2(0, 0));
    }

    @Override
    protected void init() {
//        addObject(new GameObject("Debug Draw", new Vector2()) {
//            @Override
//            public void render(Graphics2D g2) {
//                super.render(g2);
//                CircleCollider circle = getObject("Circle 1").getComponent(CircleCollider.class);
//                g2.setStroke(new BasicStroke(3f));
//                g2.setColor(Color.BLACK);
//                g2.drawLine((int) line.start.x, (int) line.start.y, (int) line.end.x, (int) line.end.y);
//                g2.setColor(Color.RED);
//                g2.drawLine((int) line.start.x, (int) line.start.y, (int) circle.getCenter().x, (int) circle.getCenter().y);
//
//                Vector2 startToCenter = circle.getCenter().sub(line.start);
//                Vector2 projected = startToCenter.project(line.toVector());
//                g2.setColor(Color.GREEN);
//                g2.drawLine((int) line.start.x, (int) line.start.y, (int) (line.start.x + projected.x), (int) (line.start.y + projected.y));
//                Vector2 nearest = line.start.add(projected);
//                g2.setColor(Color.RED);
//                g2.fillOval((int) (nearest.x - 2), (int) (nearest.y - 2), 4, 4);
//            }
//        });

        addObject(new GameObject("Circle 1", new Vector2(300, 100)) {
            @Override
            protected void init() {
                float radius = 25;
                addComponent(new CircleSprite(radius, Color.BLUE));
                addComponent(new CircleCollider(radius));
                addComponent(new Rigidbody2D(10f));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new KeyMovement(2, KeyMovement.Mode.IMPULSE));
            }
        });

        addObject(new GameObject("Circle 2", new Vector2(300, 150)) {
            @Override
            protected void init() {
                float radius = 35;
                addComponent(new CircleSprite(radius, Color.CYAN));
                addComponent(new CircleCollider(radius));
                addComponent(new Rigidbody2D(20f, false));
            }
        });

//        addObject(new GameObject("AABB 1", new Vector2(500, 100)) {
//            @Override
//            protected void init() {
//                float width = 60;
//                float height = 40;
//                addComponent(new RectangleSprite(width, height, Color.GREEN));
//                addComponent(new AlignedBoxCollider2D(new Vector2(width, height)));
//                addComponent(new Rigidbody2D(30f));
//            }
//        });
//
//        addObject(new GameObject("AABB 2", new Vector2(600, 100)) {
//            @Override
//            protected void init() {
//                float width = 30;
//                float height = 80;
//                addComponent(new RectangleSprite(width, height, Color.ORANGE));
//                addComponent(new AlignedBoxCollider2D(new Vector2(width, height)));
//                addComponent(new Rigidbody2D(40f));
//            }
//        });

    }
}
