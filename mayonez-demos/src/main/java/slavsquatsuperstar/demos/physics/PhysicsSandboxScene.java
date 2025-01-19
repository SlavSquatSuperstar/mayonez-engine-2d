package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.dynamics.*;
import slavsquatsuperstar.demos.physics.sandbox.SandboxUI;

import static slavsquatsuperstar.demos.physics.SandboxObjectPrefabs.*;

/**
 * A scene for testing dynamic movement and collision resolution.
 *
 * @author SlavSquatSupertar
 */
public class PhysicsSandboxScene extends Scene {

    // Constants
    // TODO 0 static friction makes objects slide up ramp
    static final PhysicsMaterial NORMAL_MATERIAL = new PhysicsMaterial(0.4f, 0.4f, 0.3f);
    static final PhysicsMaterial BOUNCY_MATERIAL = new PhysicsMaterial(0.1f, 0.1f, 1f);
    static final PhysicsMaterial STICKY_MATERIAL = new PhysicsMaterial(0.9f, 0.9f, 0f);
    private static final float SCENE_SCALE = 10f;

    // Fields
    private final float width = Preferences.getScreenWidth() / SCENE_SCALE;
    private final float height = Preferences.getScreenHeight() / SCENE_SCALE;

    public PhysicsSandboxScene(String name) {
        super(name);
    }

    @Override
    protected void init() {
        getCamera().setBackgroundColor(Colors.WHITE);
        getCamera().setCameraScale(SCENE_SCALE);

        // Add Static Objects
        addObject(createStaticBox("Left Ramp", new Vec2(-25, 20), new Vec2(36, 4), -20));
        addObject(createStaticBox("Right Ramp", new Vec2(15, -5), new Vec2(60, 4), 15));

        // Add Dynamic Objects
        addObject(createBall(new Vec2(8), new Vec2(-35, 30), STICKY_MATERIAL));
        addObject(createBall(new Vec2(10), new Vec2(35, 15), BOUNCY_MATERIAL));
        addObject(createBox(new Vec2(6, 6), new Vec2(0, 5), 30, STICKY_MATERIAL));
        addObject(createBox(new Vec2(10, 6), new Vec2(-30, -5), -45, BOUNCY_MATERIAL));

        // Add Boundary Objects
        // Only ground is visible
        addObject(createStaticBox("Ground", new Vec2(0, -0.5f * (height - 2)),
                new Vec2(width, 2), 0));
        addObject(createStaticBox("Ceiling", new Vec2(0, 0.5f * (height + 1)),
                new Vec2(width, 1), 0));
        addObject(createStaticBox("Left Wall", new Vec2(-0.5f * (width + 1), 0),
                new Vec2(1, height), 0));
        addObject(createStaticBox("Right Wall", new Vec2(0.5f * (width + 1), 0),
                new Vec2(1, height), 0));

        // Add UI
        addObject(new SandboxUI("Scene UI"));
    }

    @Override
    protected void onUserUpdate(float dt) {
        // Create Random Shapes
        // TODO mouse pos gets out of sync
        if (!KeyInput.keyDown("left shift")) {
            for (int i = 1; i <= 4; i++) {
                if (KeyInput.keyPressed(String.valueOf(i))) {
                    addObject(createRandomShape(MouseInput.getPosition(), i));
                    return;
                }
            }
        }
    }

}
