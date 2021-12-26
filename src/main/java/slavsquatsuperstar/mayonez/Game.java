package slavsquatsuperstar.mayonez;

import org.apache.commons.lang3.exception.ExceptionUtils;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics2d.Physics2D;
import slavsquatsuperstar.mayonez.renderer.IMGUI;
import slavsquatsuperstar.mayonez.renderer.JRenderer;

import java.awt.*;

/**
 * An instance of this game using Java's AWT and Swing libraries.
 *
 * @author SlavSquatSuperstar
 */
class Game extends GameEngine implements Runnable {

    private final IMGUI imgui;
    private Thread thread;

    public Game() {
        // Read preferences and initialize logger
        if (!Mayonez.INSTANCE.getINIT_PREFERENCES()) Mayonez.init();

        // Set up the window
        window = new JWindow(Preferences.TITLE + " " + Preferences.VERSION, Preferences.SCREEN_WIDTH,
                Preferences.SCREEN_HEIGHT);

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

    // Resource Management

    @Override
    public synchronized void start() {
        if (running) return; // Don't start if already running
        running = true;

        window.start();
        thread = new Thread(this);
        thread.start();
        startScene();
    }

    @Override
    public synchronized void stop() {
        if (!running) return;
        running = false;

        window.stop();
        thread.interrupt();
    }

    // Game Loop Methods

    @Override
    public void run() {
        // All time values are in seconds
        float lastTime = 0; // Last time the game loop iterated
        float currentTime; // Time for current frame
        float deltaTime = 0; // Time since last update frame

        // For rendering
        boolean ticked = false; // Has engine actually updated?

        // For debugging
        float timer = 0;
        int frames = 0;

        try {
            while (running && window.notClosedByUser()) {
                currentTime = Time.getTime();
                float passedTime = currentTime - lastTime; // Time since last loop iteration
                deltaTime += passedTime;
                timer += passedTime;
                lastTime = currentTime; // Reset lastTime

                window.beginFrame();

                // Update the game as many times as possible even if the screen freezes
                while (deltaTime >= Time.TIME_STEP) {
                    update(deltaTime);
                    deltaTime -= Time.TIME_STEP;
                    ticked = true;
                }
                // Only render if the game has updated to save resources
                if (ticked) {
                    render();
                    frames++;
                    ticked = false;
                }
                // Print ticks and frames each second
                if (timer >= 1) {
                    Logger.trace("Frames per Second: %d", frames);
                    frames = 0;
                    timer = 0;
                }

                window.endFrame();
            } // end loop

        } catch (Exception e) {
            Logger.warn(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
            Mayonez.stop(1);
        }

        Mayonez.stop(0);
    }

    @Override
    public void update(float dt) {
        // TODO Poll input events
        if (scene != null) scene.update(dt);
        // TODO multithread physics, set time step higher than refresh rate for smoother results
        physics.physicsUpdate(dt);
    }

    @Override
    public void render() {
        window.render((g2) -> {
            if (null != scene) scene.render(g2);
            renderer.render(g2);
            imgui.render(g2);
        });
    }

}
