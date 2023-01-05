package slavsquatsuperstar.demos.physics;

import mayonez.Preferences;
import mayonez.Scene;
import mayonez.math.FloatMath;
import mayonez.math.Random;
import mayonez.math.Vec2;

import java.util.LinkedList;

public class PoolBallsScene extends Scene {

    public PoolBallsScene(String name) {
        super(name, Preferences.getScreenWidth(), Preferences.getScreenHeight(), 8);
    }

    @Override
    protected void init() {
        setGravity(new Vec2());
        addObject(new PoolBall(new Vec2(-40, 0)));

        float xStart = -5;
        float yStart = 0;
        int ballCount = 0;
        int[] ballNums = randomizeBalls();

        for (int row = 0; row < 5; row++) {
            float radius = PoolBall.BALL_RADIUS;
            for (int col = 0; col <= row; col++) {
                // x = x0 + √3r * i
                float x = xStart + (float) row * FloatMath.sqrt(3f) * radius;
                // y = y0 + 2r * i
                float y = yStart + (float) col * 2 * radius;

                int ballNum = ballNums[ballCount];
                addObject(new PoolBall(new Vec2(x, y), ballNum));
                ballCount++;
            }
            // y0 = h/2 - r * i
            yStart -= radius;
        }
    }

    // Helper Methods

    private int[] randomizeBalls() {
        int[] ballNums = new int[15];
        LinkedList<Integer> nums = new LinkedList<>();
        for (int i = 1; i <= 7; i++) nums.add(i);
        for (int i = 9; i <= 15; i++) nums.add(i);
        for (int i = 0; i < ballNums.length; i++) {
            if (i == 4) ballNums[i] = 8; // always put 8-ball in middle
            else {
                int index = Random.randomInt(0, nums.size() - 1);
                ballNums[i] = nums.remove(index);
            }
        }
        return ballNums;
    }

}
