package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.mouse.*;
import mayonez.scripts.movement.*;

/**
 * A simulated  billiard (pool) ball.
 * <p>
 * Pool balls weigh ~160-170 g (0.165 kg) and are ~57 mm (0.057 m) wide.
 * Using an 100:1 scale, the in-game balls appear 5.70 m wide.
 * <p>
 * Source: <a buttons="https://en.wikipedia.org/wiki/Billiard_ball">Wikipedia</a>
 *
 * @author SlavSquatSuperstar
 */
class PoolBall extends GameObject {

    private static final PhysicsMaterial POOL_BALL_MAT
            = new PhysicsMaterial(0.05f, 0.05f, 0.95f);
    static final float BALL_RADIUS = 5.70f * 0.5f;
    static final float BALL_MASS = 0.165f;

    private static final Color[] ballColors = new Color[]{
            Colors.YELLOW, Colors.BLUE, Colors.RED, Colors.PURPLE,
            Colors.ORANGE, Colors.GREEN, Colors.BROWN, new Color(12, 12, 12)
    };

    private final Color color;
    private final boolean solid;
    private final boolean isCue;

    // Constructors

    private PoolBall(String name, Vec2 position, Color color, boolean solid, boolean isCue) {
        super(name, position);
        this.color = color;
        this.solid = solid;
        this.isCue = isCue;
    }

    // Create the cue ball
    PoolBall(Vec2 position) {
        this("Cue Ball", position, Color.grayscale(232), true, true);
    }

    // Create a numbered ball
    PoolBall(Vec2 position, int ballNum) {
        this(String.format("%d Ball", ballNum), position,
                ballColors[Math.abs(ballNum - 1) % 8], ballNum <= 8, false);
    }

    @Override
    protected void init() {
        addComponent(new BallCollider(BALL_RADIUS));
        if (solid) {
            addComponent(new ShapeSprite(color, true));
        } else {
            addComponent(new ShapeSprite(Colors.WHITE, true));
            addComponent(new ShapeSprite(color, false));
        }
        addComponent(new Rigidbody(BALL_MASS).setMaterial(POOL_BALL_MAT).setDrag(0.1f));
        addComponent(new DragAndDrop("left mouse"));
        if (isCue) {
            addComponent(new MouseFlick("right mouse", 20, MoveMode.IMPULSE, false));
        }
    }

}
