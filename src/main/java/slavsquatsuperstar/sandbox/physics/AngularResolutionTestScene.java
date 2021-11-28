package slavsquatsuperstar.sandbox.physics;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.AlignedBoxCollider2D;

public class AngularResolutionTestScene extends PhysicsTestScene {

    public AngularResolutionTestScene(String name) {
        super(name, 1);
    }

    @Override
    protected void init() {
        super.init();

        addObject(new GameObject("Floor", new Vec2(getWidth() / 2f, getHeight() - 1)) {
            @Override
            protected void init() {
                addComponent(new Rigidbody2D(0));
                addComponent(new AlignedBoxCollider2D(new Vec2(getWidth(), 2)));
            }
        });

        for (int i = 0; i < NUM_SHAPES; i++) {
//            if (i % 3 == 0) {
//                addObject(createCircle(MathUtils.random(3f, 6f), new Vec2(MathUtils.random(0, getWidth()),
//                        MathUtils.random(0, getHeight())), BOUNCY_MATERIAL));
//            } else {
            addObject(createOBB(MathUtils.random(5f, 12f), MathUtils.random(5f, 12f),
                    new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight())), MathUtils.random(0, 90), BOUNCY_MATERIAL));
//            }
        }

    }

    public static void main(String[] args) {
        Game game = Game.instance();
        Game.loadScene(new AngularResolutionTestScene("Angular Impulse Resolution Test"));
        game.start();
    }

}
