package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.components.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.components.scripts.KeepInScene;
import slavsquatsuperstar.mayonez.components.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.CircleCollider;
import slavsquatsuperstar.mayonez.physics2d.primitives.Line2D;

import java.awt.*;

public class PhysicsTestScene extends Scene {

    Line2D line = new Line2D(new Vector2(300, 300), new Vector2(600, 300));

    public PhysicsTestScene(String name) {
        super(name, Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT);
        setGravity(new Vector2(0, 0));
    }

    @Override
    protected void init() {
        Logger.log(null);

        addObject(new GameObject("Debug Draw", new Vector2()) {
            @Override
            public void render(Graphics2D g2) {
                CircleCollider c1 = getObject("Circle 1").getComponent(CircleCollider.class);
                DebugDraw.drawVector(c1.getRigidbody().velocity(), c1.getCenter(), Color.BLUE);
                DebugDraw.drawCircle(c1, Color.BLUE);
                CircleCollider c2 = getObject("Circle 2").getComponent(CircleCollider.class);
                DebugDraw.drawCircle(c2, Color.CYAN);
            }
        });

        addObject(new GameObject("Circle 1", new Vector2(300, 100)) {
            @Override
            protected void init() {
                addComponent(new CircleCollider(25f));
                addComponent(new Rigidbody2D(10f));
                addComponent(new DragAndDrop("left mouse", false));
//                addComponent(new MouseMovement());
                addComponent(new KeyMovement(KeyMovement.Mode.IMPULSE, 2));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.WRAP));
            }
        });

        addObject(new GameObject("Circle 2", new Vector2(300, 150)) {
            @Override
            protected void init() {
                addComponent(new CircleCollider(35f));
                addComponent(new Rigidbody2D(20f, false));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.WRAP));
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
