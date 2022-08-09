package slavsquatsuperstar.mayonez.engine;

import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.graphics.renderer.JRenderer;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics.PhysicsWorld;

import java.awt.*;

/**
 * An instance of this game using Java's AWT and Swing libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public final class JGame extends GameEngine {

    public JGame() {
        // Set up the window
        window = new JWindow(Preferences.getTitle() + " " + Preferences.getVersion(),
                Preferences.getScreenWidth(), Preferences.getScreenHeight());

        // Add input listeners
        window.setKeyInput(KeyInput.INSTANCE);
        window.setMouseInput(MouseInput.INSTANCE);

        renderer = new JRenderer();
        physics = new PhysicsWorld();
    }

    public boolean isFullScreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        return device.getFullScreenWindow() != null;
    }

    // Game Loop Methods

    @Override
    public void update(float dt) throws Exception {
        // TODO Poll input events
        if (scene != null) scene.update(dt);
        physics.physicsUpdate(dt);
    }

    @Override
    public void render() throws Exception {
        window.render((args) -> {
            Graphics2D g2 = (Graphics2D) args[0];
            if (null != scene) scene.render(g2);
            renderer.render(g2);
        });
    }

    @Override
    public float getCurrentTime() {
        return Mayonez.getTime();
    }
}
