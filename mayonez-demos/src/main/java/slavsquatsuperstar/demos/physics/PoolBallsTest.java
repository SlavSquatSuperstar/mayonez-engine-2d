package slavsquatsuperstar.demos.physics;

import mayonez.GameObject;
import mayonez.graphics.Color;
import mayonez.graphics.Colors;
import mayonez.graphics.sprite.ShapeSprite;
import mayonez.math.FloatMath;
import mayonez.math.Vec2;
import mayonez.physics.PhysicsMaterial;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BallCollider;
import mayonez.scripts.KeepInScene;
import mayonez.scripts.movement.DragAndDrop;
import mayonez.scripts.movement.MouseFlick;
import mayonez.scripts.movement.MoveMode;

/**
 * Pool balls weigh ~160-170 g (0.165 kg) are ~57 mm (0.057 m) wide. Using an 80:1 scale, the in-game balls weigh 6.6 kg
 * and are 4.65 m wide
 * <p>
 * Source: <a buttons="https://en.wikipedia.org/wiki/Billiard_ball">Wikipedia</a>
 */
public class PoolBallsTest extends PhysicsTestScene {

    private final PhysicsMaterial POOL_BALL_MAT = new PhysicsMaterial(0.05f, 0.05f, 0.95f);
    private final float BALL_RADIUS = 2.28f;
    private final float BALL_MASS = 13.2f;

    public PoolBallsTest(String name) {
        super(name, 0);
    }

    @Override
    protected void init() {
        super.init();
        addObject(new PoolBall("Cue Ball", new Vec2(-40, 0), Colors.LIGHT_GRAY, true));

        float xStart = -5;
        float yStart = 0;
        int ballCount = 0;
        Color[] colors = new Color[]{
                Colors.YELLOW, Colors.BLUE, Colors.RED, Colors.PURPLE,
                Colors.ORANGE, Colors.GREEN, Colors.BROWN, Colors.BLACK
        };

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col <= row; col++) {
                // x = x0 + √3r * i
                float x = xStart + (float) row * FloatMath.sqrt(3f) * BALL_RADIUS;
                // y = y0 + 2r * i
                float y = yStart + (float) col * 2 * BALL_RADIUS;
                Color ballColor = colors[ballCount % 8]; // TODO put 8 ball in middle
                addObject(new PoolBall(String.format("%d Ball", ballCount), new Vec2(x, y), ballColor, ballCount < 8));
                ballCount++;
            }
            // y0 = h/2 - r * i
            yStart -= BALL_RADIUS;
        }
    }

    private class PoolBall extends GameObject {

        private final Color color;
        private final boolean fill;

        public PoolBall(String name, Vec2 position, Color color, boolean fill) {
            super(name, position);
            this.color = color;
            this.fill = fill;
        }

        @Override
        protected void init() {
            addComponent(new BallCollider(BALL_RADIUS));
            addComponent(new ShapeSprite(color, fill));
            addComponent(new Rigidbody(BALL_MASS).setMaterial(POOL_BALL_MAT));
            addComponent(new KeepInScene(KeepInScene.Mode.BOUNCE));
            addComponent(new DragAndDrop("left mouse"));
            addComponent(new MouseFlick(MoveMode.VELOCITY, "right mouse", 50, false));
        }
    }

}
