package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.components.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.components.scripts.KeepInScene;
import slavsquatsuperstar.mayonez.components.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.CircleCollider;
import slavsquatsuperstar.mayonez.physics2d.primitives.Line2D;
import slavsquatsuperstar.util.MathUtils;

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
                for (GameObject o : getScene().getObjects(null)) {
                    CircleCollider c = o.getComponent(CircleCollider.class);
                    if (c != null) {
                        DebugDraw.drawCircle(c, Colors.BLUE);
                        DebugDraw.drawVector(c.getRigidbody().velocity().mul(5), c.getCenter(), Colors.BLUE);
                    }
                }
            }
        });

        addObject(new GameObject("Circle 1", new Vector2(300, 100)) {
            @Override
            protected void init() {
                addComponent(new CircleCollider(25f));
                addComponent(new Rigidbody2D(15f));
//                addComponent(new MouseMovement(MovementScript.Mode.IMPULSE, 2));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new KeyMovement(KeyMovement.Mode.IMPULSE, 2));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                addComponent(new Script() {
                    @Override
                    public void update(float dt) {
                        if (Game.mouse().buttonDown("left mouse")) {
                            scene().addObject(new GameObject("Circle", Game.mouse().getPosition()) {
                                @Override
                                protected void init() {
                                    float radius = MathUtils.random(10f, 40f);
                                    addComponent(new CircleCollider(radius));
                                    addComponent(new Rigidbody2D(radius * 0.5f));
                                    addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                                }
                            });
                        }
                    }
                });
            }
        });

        addObject(new GameObject("Circle 2", new Vector2(300, 200)) {
            @Override
            protected void init() {
                addComponent(new CircleCollider(50f));
                addComponent(new Rigidbody2D(0));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
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
