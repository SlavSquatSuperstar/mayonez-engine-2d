package slavsquatsuperstar.mayonez.engine;

import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.graphics.Engine;
import slavsquatsuperstar.mayonez.graphics.EngineType;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics2d.Physics2D;
import slavsquatsuperstar.mayonez.graphics.IMGUI;
import slavsquatsuperstar.mayonez.graphics.renderer.JRenderer;

import java.awt.*;

/**
 * An instance of this game using Java's AWT and Swing libraries.
 *
 * @author SlavSquatSuperstar
 */
@Engine(EngineType.AWT)
public final class JGame extends GameEngine {

    private final IMGUI imgui;

    public JGame() {
        // Set up the window
        window = new JWindow(Preferences.getTitle() + " " + Preferences.getVersion(),
                Preferences.getScreenWidth(), Preferences.getScreenHeight());

        // Add input listeners
        window.setKeyInput(KeyInput.INSTANCE);
        window.setMouseInput(MouseInput.INSTANCE);

        renderer = new JRenderer();
        physics = new Physics2D();
        imgui = IMGUI.INSTANCE;
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
        // TODO multithread physics, set time step higher than refresh rate for smoother results
        physics.physicsUpdate(dt);
    }

    @Override
    public void render() throws Exception {
        window.render((g2) -> {
            if (null != scene) scene.render(g2);
            renderer.render(g2);
            imgui.render(g2);
        });
    }

    @Override
    public float getCurrentTime() {
        return Mayonez.getTime();
    }
}
