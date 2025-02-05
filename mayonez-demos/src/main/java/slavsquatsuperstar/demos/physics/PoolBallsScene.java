package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.Random;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import slavsquatsuperstar.demos.DemoScene;

import java.util.*;

/**
 * A simulation of eight-ball pool using the physics engine.
 *
 * @author SlavSquatSuperstar
 */
public class PoolBallsScene extends DemoScene {

    // Constants
    private static final PhysicsMaterial WALL_MAT
            = new PhysicsMaterial(0.05f, 0.05f, 0.95f);
    private static final float SCENE_SCALE = 8f;

    // Fields
    private final float width = Preferences.getScreenWidth() / SCENE_SCALE;
    private final float height =  Preferences.getScreenHeight() / SCENE_SCALE;

    public PoolBallsScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        setGravity(new Vec2());
        getCamera().setCameraScale(SCENE_SCALE);

        // Add Boundary Objects
        var wallThickness = 5;
        addObject(createBoundaryObject("Upper Wall", new Vec2(0, -0.5f * (height + 1)),
                new Vec2(width, wallThickness)));
        addObject(createBoundaryObject("Lower Wall", new Vec2(0, 0.5f * (height + 1)),
                new Vec2(width, wallThickness)));
        addObject(createBoundaryObject("Left Wall", new Vec2(-0.5f * (width + 1), 0),
                new Vec2(wallThickness, height)));
        addObject(createBoundaryObject("Right Wall", new Vec2(0.5f * (width + 1), 0),
                new Vec2(wallThickness, height)));

        // Add Cue Ball
        addObject(new PoolBall(new Vec2(-40, 0)));

        // Add Pool Balls
        var xStart = -5f;
        var yStart = 0f;
        var ballCount = 0;
        var ballNums = randomizeBalls();

        for (var row = 0; row < 5; row++) {
            var radius = PoolBall.BALL_RADIUS;
            for (var col = 0; col <= row; col++) {
                // x = x0 + √3r * i
                var x = xStart + (float) row * MathUtils.sqrt(3f) * radius;
                // y = y0 + 2r * i
                var y = yStart + (float) col * 2 * radius;

                var ballNum = ballNums[ballCount];
                addObject(new PoolBall(new Vec2(x, y), ballNum));
                ballCount += 1;
            }
            // y0 = h/2 - r * i
            yStart -= radius;
        }
    }

    // Helper Methods

    private GameObject createBoundaryObject(String name, Vec2 position, Vec2 size) {
        return new GameObject(name, new Transform(position)) {
            @Override
            protected void init() {
                addComponent(new BoxCollider(size));
                addComponent(new Rigidbody(0f).setMaterial(WALL_MAT));
                addComponent(new ShapeSprite(Colors.DARK_GRAY, true));
            }
        };
    }

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
