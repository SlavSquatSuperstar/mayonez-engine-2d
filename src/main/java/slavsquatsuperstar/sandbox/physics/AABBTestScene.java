package slavsquatsuperstar.sandbox.physics;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;
import slavsquatsuperstar.mayonez.renderer.DebugDraw;
import slavsquatsuperstar.mayonez.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.scripts.KeepInScene;
import slavsquatsuperstar.mayonez.scripts.MouseFlick;

import java.awt.*;

public class AABBTestScene extends Scene {

    public AABBTestScene(String name) {
        super(name, Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT, 10);
        setBackground(Colors.WHITE);
        setGravity(new Vec2());
    }

    @Override
    protected void init() {
        addObject(new GameObject("Debug Draw", new Vec2()) {
            @Override
            public void render(Graphics2D g2) {
                for (GameObject o : getScene().getObjects(null)) {
                    if (o.name.equals("Camera"))
                        continue;

                    // Draw Circles
                    Collider2D col = o.getComponent(Collider2D.class);
                    if (col != null) {
                        Color color = Colors.BLACK;
                        // Draw velocity and direction vector
                        if (!col.isStatic()) {
                            color = Colors.LIGHT_GREEN;
                            DebugDraw.drawVector(col.center(), col.getRigidbody().getVelocity().div(10), color);
                            DebugDraw.drawVector(col.getRigidbody().getPosition(), col.getRigidbody().getTransform().getDirection(), Colors.BLACK);
                        }
                        DebugDraw.drawShape(col, color);
                    }
                }
            }
        });

        for (int i = 0; i < PhysicsTestScene.NUM_SHAPES; i++) {
            addObject(new GameObject("AABB Rectangle", new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight()))) {
                @Override
                protected void init() {
                    float width = MathUtils.random(4f, 16f);
                    float height = MathUtils.random(4f, 16f);
                    addComponent(new AlignedBoxCollider2D(new Vec2(width, height)).setMaterial(PhysicsTestScene.BOUNCY_MATERIAL));
                    addComponent(new Rigidbody2D(width * height / 4));
                    addComponent(new KeepInScene(getScene(), KeepInScene.Mode.BOUNCE));
                    addComponent(new DragAndDrop("left mouse", false));
                    addComponent(new MouseFlick("right mouse", 15, false));
                }
            });
        }
    }

    public static void main(String[] args) {
        Game game = Game.instance();
        Game.loadScene(new AABBTestScene("AABB Physics Test"));
        game.start();
    }

}
