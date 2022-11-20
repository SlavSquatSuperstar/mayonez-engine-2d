package slavsquatsuperstar.demos.physics;

import slavsquatsuperstar.mayonez.math.MathUtils;
import slavsquatsuperstar.mayonez.math.Vec2;
import slavsquatsuperstar.mayonez.scripts.*;
import slavsquatsuperstar.mayonez.util.Colors;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.physics.PhysicsMaterial;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.BallCollider;

/**
 * Pool balls weigh ~160-170 g (0.165 kg) are ~57 mm (0.057 m) wide. Using an 80:1 scale, the in-game balls weigh 6.6 kg
 * and are 4.65 m wide
 * <p>
 * Source: <a buttons="https://en.wikipedia.org/wiki/Billiard_ball">Wikipedia</a>
 */
public class PoolBallsTest extends PhysicsTestScene {

    private final PhysicsMaterial POOL_BALL_MAT = new PhysicsMaterial(0.0f, 0.0f, 1.0f);
    private final float BALL_RADIUS = 2.28f;
    private final float BALL_MASS = 13.2f;

    public PoolBallsTest(String name) {
        super(name, 0);
    }

    @Override
    protected void init() {
        super.init();
        addObject(createCircle("Cue Ball", new Vec2(10, getHeight() / 2f)));

        float xStart = 50;
        float yStart = getHeight() / 2f;
        int ballCount = 1;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j <= i; j++) {
                // x = x0 + √3r * i
                float x = xStart + (float) i * MathUtils.sqrt(3f) * BALL_RADIUS;
                // y = y0 + 2r * i
                float y = yStart + (float) j * 2 * BALL_RADIUS;
                addObject(createCircle(String.format("Pool Ball #%d", ballCount++), new Vec2(x, y)));
            }
            // y0 = h/2 - r * i
            yStart -= BALL_RADIUS;
        }
    }

    private GameObject createCircle(String name, Vec2 position) {
        return new GameObject(name, position) {
            @Override
            protected void init() {
                addComponent(new BallCollider(BALL_RADIUS).setDebugDraw(Colors.BLUE, false));
                addComponent(new Rigidbody(BALL_MASS).setMaterial(POOL_BALL_MAT));
                addComponent(new KeepInScene(new Vec2(), getScene().getSize(), KeepInScene.Mode.BOUNCE));
                addComponent(new DragAndDrop("left mouse"));
                addComponent(new MouseFlick(MoveMode.IMPULSE, "right mouse", 80, false));
            }
        };
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.setScene(new PoolBallsTest("Pool Balls Test"));
        Mayonez.start();
    }

}
