package slavsquatsuperstar.mayonez;

import org.apache.commons.lang3.exception.ExceptionUtils;
import slavsquatsuperstar.mayonez.fileio.Assets;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics2d.Physics2D;
import slavsquatsuperstar.mayonez.renderer.GameRenderer;
import slavsquatsuperstar.mayonez.renderer.IMGUI;
import slavsquatsuperstar.mayonez.renderer.JRenderer;

import java.awt.*;

/**
 * The application that contains the engine's core loop.
 *
 * @author SlavSquatSuperstar
 */
public class Game implements Runnable {

    static {
        Initializer.init();
        game = instance(); // "Lazy" singleton construction
    }

    /*
     * Field Declarations
     */

    // Singleton Fields
    // TODO make start/stop static?
    private static Game game;

    // Thread Fields
    private static Thread thread;
    private static boolean running;

    // Game Layers
    private static GameWindow window;
    private static Scene currentScene;
    private static GameRenderer renderer;
    private static Physics2D physics;
    private static IMGUI imgui;

    private Game() {
        // Read preferences and initialize logger
        Initializer.init();

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

    // Game Loop Methods

    public synchronized static Game instance() {
        return (null == game) ? game = new Game() : game;
    }

    public static boolean isFullScreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        return device.getFullScreenWindow() != null;
    }

    // Getters and Setters

    public static void loadScene(Scene newScene) {
        currentScene = newScene;
        startCurrentScene();
    }

    public static Scene currentScene() {
        return currentScene;
    }

    public static GameRenderer getRenderer() {
        return renderer;
    }

    public static Physics2D getPhysics() {
        return physics;
    }

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
            stop(1);
        }

        stop(0);
    }

    /**
     * Refreshes all objects in the current scene.
     *
     * @param dt seconds since the last frame
     */
    public void update(float dt) throws Exception { // TODO pass dt or use Game.timestep?
        // TODO Poll input events
        if (KeyInput.keyDown("exit")) {
            running = false;
            return;
        }
        if (currentScene != null) currentScene.update(dt);
        // TODO multithread physics, set time step higher than refresh rate for smoother results
        physics.physicsUpdate(dt);
    }

    /**
     * Redraws all objects in the current scene.
     */
    public void render() throws Exception {
        window.render((g2) -> {
            if (null != currentScene) currentScene.render(g2);
            renderer.render(g2);
            imgui.render(g2);
        });
    }

    // Helper Methods

    /**
     * Initializes the game engine, displays the window, and starts the current scene (if not null).
     */
    public static synchronized void start() {
        if (running) return; // Don't start if already running

        running = true;
        Logger.log("Engine: Starting %s %s", Preferences.TITLE, Preferences.VERSION);

        Logger.trace("Engine: Loading assets");
        Assets.scanResources("assets"); // Load all game assets

        // Display window
        window.start();

        // Start thread
        thread = new Thread(game);
        thread.start();

        startCurrentScene();
    }

    /**
     * Shuts down the engine and closes the window.
     *
     * @param status The exit code for this application. A value of 0 indicates normal, while non-zero status codes
     *               indicate an error.
     */
    public static synchronized void stop(int status) {
        running = false;
        Logger.log("Engine: Stopping with exit code %d", status);

        // Free System resources
        window.stop();

        // Stop thread
        thread.interrupt();
        Logger.printExitMessage();
        System.exit(status);
    }

    // Helper Methods

    /**
     * Starts the current scene, if not null
     */
    private static void startCurrentScene() {
        if (currentScene != null && running) {
            currentScene.start();
            Game.getPhysics().setScene(currentScene);
            Game.getRenderer().setScene(currentScene);
            Logger.trace("Game: Loaded scene \"%s\"", currentScene.getName());
        }
    }

}
