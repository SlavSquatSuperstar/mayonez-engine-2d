package slavsquatsuperstar.sandbox.physics;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Game;

public class AABBTestScene extends PhysicsTestScene {

    public AABBTestScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        super.init();

        for (int i = 0; i < PhysicsTestScene.NUM_SHAPES; i++) {
            addObject(createAABB(MathUtils.random(5f, 12f), MathUtils.random(5f, 12f),
                    new Vec2(MathUtils.random(0, getWidth()), MathUtils.random(0, getHeight())), BOUNCY_MATERIAL));
        }
    }

    public static void main(String[] args) {
        Game game = Game.instance();
        Game.loadScene(new AABBTestScene("AABB Physics Test"));
        game.start();
    }

}
