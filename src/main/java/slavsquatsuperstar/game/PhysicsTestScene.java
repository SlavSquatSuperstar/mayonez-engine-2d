package slavsquatsuperstar.game;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.components.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.components.scripts.KeepInScene;
import slavsquatsuperstar.mayonez.components.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.CircleCollider;
import slavsquatsuperstar.mayonez.physics2d.primitives.Collider2D;
import slavsquatsuperstar.util.MathUtils;

import java.awt.*;

public class PhysicsTestScene extends Scene {

//    Line2D line = new Line2D(new Vector2(300, 300), new Vector2(600, 300));

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
//                Line2D line = new Line2D(new Vector2(200, 200), Game.mouse().getPosition());
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

        addObject(new GameObject("Player Shape", new Vector2(300, 100)) {
            @Override
            protected void init() {
                float radius = 35f;
                float size = 50f;
//                addComponent(new CircleCollider(radius));
                addComponent(new AlignedBoxCollider2D(new Vector2(size, size)));
                addComponent(new Rigidbody2D(radius * radius * MathUtils.PI / 100f));
//                addComponent(new MouseMovement(MovementScript.Mode.FOLLOW_MOUSE, 2));
                addComponent(new DragAndDrop("left mouse", false));
                addComponent(new KeyMovement(KeyMovement.Mode.IMPULSE, 4));
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
                                    addComponent(new Rigidbody2D(radius * radius * MathUtils.PI * 0.5f));
                                    addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                                }
                            });
                        } else if (Game.mouse().buttonDown("right mouse")) {
                            scene().addObject(new GameObject("Rectangle", Game.mouse().getPosition()) {
                                @Override
                                protected void init() {
                                    float width = MathUtils.random(10f, 40f);
                                    float height = MathUtils.random(10f, 40f);
                                    addComponent(new AlignedBoxCollider2D(new Vector2(width, height)));
                                    addComponent(new Rigidbody2D(width * height / 100f));
                                    addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                                }
                            });
                        }
                    }
                });
            }
        });

        addObject(new GameObject("Small Circle", new Vector2(100, 100)) {
            @Override
            protected void init() {
                float radius = 20f;
                addComponent(new CircleCollider(radius));
//                addComponent(new AlignedBoxCollider2D(new Vector2(100, 100)));
                addComponent(new Rigidbody2D(radius * radius * MathUtils.PI / 100f));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
            }
        });

        addObject(new GameObject("Big Circle", new Vector2(300, 300)) {
            @Override
            protected void init() {
                float radius = 50f;
                addComponent(new CircleCollider(radius));
//                addComponent(new AlignedBoxCollider2D(new Vector2(100, 100)));
                addComponent(new Rigidbody2D(radius * radius * MathUtils.PI / 100f));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
            }
        });

        addObject(new GameObject("Small AABB", new Vector2(500, 200)) {
            @Override
            protected void init() {
                float width = 120, height = 80;
                addComponent(new AlignedBoxCollider2D(new Vector2(width, height)));
                addComponent(new Rigidbody2D(width * height / 100f));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
            }
        });

        addObject(new GameObject("Big AABB", new Vector2(700, 200)) {
            @Override
            protected void init() {
                float width = 50, height = 100;
                addComponent(new AlignedBoxCollider2D(new Vector2(width, height)));
                addComponent(new Rigidbody2D(width * height / 100f));
                addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
            }
        });

    }
}
