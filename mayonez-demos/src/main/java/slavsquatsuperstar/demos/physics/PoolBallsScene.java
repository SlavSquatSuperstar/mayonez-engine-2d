package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.math.Random;
import mayonez.math.*;

import java.util.*;

/**
 * For testing the physics engine with simulated pool balls.
 *
 * @author SlavSquatSuperstar
 */
public class PoolBallsScene extends Scene {

    public PoolBallsScene(String name) {
        super(name, Preferences.getScreenWidth(), Preferences.getScreenHeight(), 8);
    }

    @Override
    protected void init() {
        setGravity(new Vec2());
        addObject(new PoolBall(new Vec2(-40, 0)));

        var xStart = -5;
        var yStart = 0;
        var ballCount = 0;
        var ballNums = randomizeBalls();

        for (var row = 0; row < 5; row++) {
            var radius = PoolBall.BALL_RADIUS;
            for (var col = 0; col <= row; col++) {
                // x = x0 + √3r * i
                var x = xStart + (float) row * FloatMath.sqrt(3f) * radius;
                // y = y0 + 2r * i
                var y = yStart + (float) col * 2 * radius;

                var ballNum = ballNums[ballCount];
                addObject(new PoolBall(new Vec2(x, y), ballNum));
                ballCount++;
            }
            // y0 = h/2 - r * i
            yStart -= radius;
        }
    }

    // Helper Methods

    private int[] randomizeBalls() {
        var ballNums = new int[15];
        var nums = new LinkedList<Integer>();
        for (var i = 1; i <= 7; i++) nums.add(i);
        for (var i = 9; i <= 15; i++) nums.add(i);
        for (var i = 0; i < ballNums.length; i++) {
            if (i == 4) ballNums[i] = 8; // always put 8-ball in middle
            else {
                var index = Random.randomInt(0, nums.size() - 1);
                ballNums[i] = nums.remove(index);
            }
        }
        return ballNums;
    }

}
