package mayonez.engine;

import mayonez.Mayonez;
import mayonez.Preferences;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.input.KeyInput;
import mayonez.input.MouseInput;

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
        window = new JWindow(String.format("%s %s (AWT)", Preferences.getTitle(), Preferences.getVersion()),
                Preferences.getScreenWidth(), Preferences.getScreenHeight());

        // Add input listeners
        window.setKeyInput(KeyInput.INSTANCE);
        window.setMouseInput(MouseInput.INSTANCE);
    }

    public boolean isFullScreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        return device.getFullScreenWindow() != null;
    }

    // Game Loop Methods

    @Override
    public void update(float dt) throws Exception {
        if (getScene() != null) getScene().update(dt);
    }

    @Override
    public void render() throws Exception {
        window.render((args) -> {
            Graphics2D g2 = (Graphics2D) args[0];
            if (getScene() != null) getScene().render(g2);
        });
    }

    @Override
    public float getCurrentTime() {
        return Mayonez.getSeconds();
    }

    @Override
    public String toString() {
        return "AWT Game";
    }
}