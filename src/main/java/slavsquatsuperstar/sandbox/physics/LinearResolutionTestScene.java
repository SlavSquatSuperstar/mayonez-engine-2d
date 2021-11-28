package slavsquatsuperstar.sandbox.physics;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Game;

public class LinearResolutionTestScene extends PhysicsTestScene {

    public LinearResolutionTestScene(String name) {
        super(name, 6);
    }

    @Override
    protected void init() {
        super.init();

        for (int i = 0; i < NUM_SHAPES; i++) {
            if (i % 3 == 0) {
                addObject(createCircle(MathUtils.random(3f, 6f), new Vec2(MathUtils.random(0, getWidth()),
                        MathUtils.random(0, getHeight())), BOUNCY_MATERIAL));
            } else if (i % 3 == 1) {
                addObject(createAABB(MathUtils.random(5f, 12f), MathUtils.random(5f, 12f),
                        new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight())), BOUNCY_MATERIAL));
            } else {
                addObject(createOBB(MathUtils.random(5f, 12f), MathUtils.random(5f, 12f),
                        new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight())), MathUtils.random(0, 90), BOUNCY_MATERIAL));
            }
        }

    }

    public static void main(String[] args) {
        Game game = Game.instance();
        Game.loadScene(new LinearResolutionTestScene("Linear Impulse Resolution Test"));
        game.start();
    }

}
