package slavsquatsuperstar.sandbox.physics;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.AlignedBoxCollider2D;

public class AngularResolutionTest extends PhysicsTestScene {

    public AngularResolutionTest(String name) {
        super(name, 8);
    }

    @Override
    protected void init() {
        addObject(new GameObject("Floor", new Vec2(getWidth() / 2f, getHeight() - 1)) {
            @Override
            protected void init() {
                addComponent(new Rigidbody2D(0));
                addComponent(new AlignedBoxCollider2D(new Vec2(getWidth(), 2)));
            }
        });

        for (int i = 0; i < NUM_SHAPES; i++) {
            if (i % 3 == 0) {
                addObject(createCircle(MathUtils.random(3f, 6f), new Vec2(MathUtils.random(0, getWidth()),
                        MathUtils.random(0, getHeight())), TEST_MATERIAL));
            } else {
            addObject(createOBB(MathUtils.random(5f, 12f), MathUtils.random(5f, 12f),
                    new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight())), MathUtils.random(0, 90), TEST_MATERIAL));
            }
        }

    }

    public static void main(String[] args) {
        Game.loadScene(new AngularResolutionTest("Angular Impulse Resolution Test"));
        Game.start();
    }

}
