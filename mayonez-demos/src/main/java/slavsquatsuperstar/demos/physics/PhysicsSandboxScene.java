package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.dynamics.*;
import slavsquatsuperstar.demos.DemoScene;
import slavsquatsuperstar.demos.physics.sandbox.SandboxUI;

import static slavsquatsuperstar.demos.physics.SandboxObjectPrefabs.*;

/**
 * A scene for testing dynamic movement and collision resolution.
 *
 * @author SlavSquatSupertar
 */
public class PhysicsSandboxScene extends DemoScene {

    // Constants
    private static final PhysicsMaterial NORMAL_MATERIAL = new PhysicsMaterial(0.4f, 0.4f, 0.2f);
    private static final PhysicsMaterial BOUNCY_MATERIAL = new PhysicsMaterial(0f, 0f, 1f);
    private static final PhysicsMaterial STICKY_MATERIAL = new PhysicsMaterial(1f, 1f, 0f);
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
        addObject(createStaticBox("Left Ramp",
                new Vec2(-25, 20), new Vec2(36, 4), -20, NORMAL_MATERIAL));
        addObject(createStaticBox("Right Ramp",
                new Vec2(15, -5), new Vec2(60, 4), 15, NORMAL_MATERIAL));

        // Add Dynamic Objects
        addObject(createBall(new Vec2(-35, 35), new Vec2(8), STICKY_MATERIAL));
        addObject(createBall(new Vec2(35, 15), new Vec2(10), BOUNCY_MATERIAL));
        addObject(createBox(new Vec2(0, 5), new Vec2(6, 6), 30, BOUNCY_MATERIAL));
        addObject(createBox(new Vec2(-30, -5), new Vec2(10, 6), -45, STICKY_MATERIAL));

        // Add Boundary Objects
        // Only ground is visible
        addObject(createStaticBox("Ground", new Vec2(0, -0.5f * (height - 2)),
                new Vec2(width, 2), 0, NORMAL_MATERIAL));
        addObject(createStaticBox("Ceiling", new Vec2(0, 0.5f * (height + 1)),
                new Vec2(width, 1), 0, NORMAL_MATERIAL));
        addObject(createStaticBox("Left Wall", new Vec2(-0.5f * (width + 1), 0),
                new Vec2(1, height), 0, NORMAL_MATERIAL));
        addObject(createStaticBox("Right Wall", new Vec2(0.5f * (width + 1), 0),
                new Vec2(1, height), 0, NORMAL_MATERIAL));

        // Add UI
        addObject(new SandboxUI("Scene UI"));
    }

    @Override
    protected void onUserUpdate(float dt) {
        super.onUserUpdate(dt);
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
