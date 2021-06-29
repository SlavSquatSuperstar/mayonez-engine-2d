package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.components.CircleSprite;
import slavsquatsuperstar.mayonez.components.RectangleSprite;
import slavsquatsuperstar.mayonez.physics2d.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.CircleCollider;
import slavsquatsuperstar.mayonez.physics2d.RigidBody2D;

import java.awt.*;

public class PhysicsTestScene extends Scene {
    public PhysicsTestScene(String name) {
        super(name);
        background = Color.WHITE;
        setGravity(new Vector2(0, 2));
    }

    @Override
    protected void init() {
        addObject(new GameObject("Circle 1", new Vector2(300, 100)) {
            @Override
            protected void init() {
                float radius = 25;
                addComponent(new CircleSprite(radius, Color.BLUE));
                addComponent(new CircleCollider(radius));
                addComponent(new RigidBody2D(1f));
            }
        });

        addObject(new GameObject("Circle 2", new Vector2(400, 100)) {
            @Override
            protected void init() {
                float radius = 35;
                addComponent(new CircleSprite(radius, Color.CYAN));
                addComponent(new CircleCollider(radius));
                addComponent(new RigidBody2D(2f));
            }
        });

        addObject(new GameObject("AABB 1", new Vector2(500, 100)) {
            @Override
            protected void init() {
                float width = 60;
                float height = 40;
                addComponent(new RectangleSprite(width, height, Color.GREEN));
                addComponent(new AlignedBoxCollider2D(new Vector2(width, height)));
                addComponent(new RigidBody2D(15f));
            }
        });

        addObject(new GameObject("AABB 2", new Vector2(600, 100)) {
            @Override
            protected void init() {
                float width = 30;
                float height = 80;
                addComponent(new RectangleSprite(width, height, Color.ORANGE));
                addComponent(new AlignedBoxCollider2D(new Vector2(width, height)));
                addComponent(new RigidBody2D(30f));
            }
        });
    }
}
