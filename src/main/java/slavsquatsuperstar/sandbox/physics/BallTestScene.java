package slavsquatsuperstar.sandbox.physics;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Game;

public class BallTestScene extends PhysicsTestScene {

    public BallTestScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        super.init();

        for (int i = 0; i < PhysicsTestScene.NUM_SHAPES; i++) {
            addObject(createCircle(MathUtils.random(3f, 6f), new Vec2(MathUtils.random(0, getWidth()),
                    MathUtils.random(0, getHeight())), BOUNCY_MATERIAL));
        }
    }

    public static void main(String[] args) {
        Game game = Game.instance();
        Game.loadScene(new BallTestScene("Ball Physics Test"));
        game.start();
    }

}
