package slavsquatsuperstar.mayonez;

import org.apache.commons.lang3.exception.ExceptionUtils;
import slavsquatsuperstar.fileio.Assets;
import slavsquatsuperstar.mayonez.physics2d.Physics2D;
import slavsquatsuperstar.mayonez.renderer.IMGUI;
import slavsquatsuperstar.mayonez.renderer.Renderer;
import slavsquatsuperstar.sandbox.LevelEditorScene;
import slavsquatsuperstar.sandbox.LevelScene;
import slavsquatsuperstar.sandbox.PhysicsTestScene;
import slavsquatsuperstar.sandbox.RendererTestScene;

import java.awt.*;

/**
 * The application that contains the engine's core loop.
 *
 * @author SlavSquatSuperstar
 */
public class Game implements Runnable {

    // Singleton Fields
    // TODO make start/stop static?
    private static Game game;

    // Thread Fields
    private Thread thread;
    private boolean running;

    // Window Fields
    private final IGameWindow window;
    private static final int WIDTH = Preferences.SCREEN_WIDTH;
    private static final int HEIGHT = Preferences.SCREEN_HEIGHT;

    // Game Layers
    private Scene currentScene;
    private Physics2D physics;
    private Renderer renderer;
    private IMGUI imgui;

    private Game() {
        // Set up the window
        window = new Window(Preferences.TITLE + " " + Preferences.VERSION, WIDTH, HEIGHT);

        // Add input listeners
        window.addKeyInput(KeyInput.INSTANCE);
        window.addMouseInput(MouseInput.INSTANCE);

        physics = new Physics2D();
        renderer = new Renderer();
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
        game.currentScene = newScene;
        game.startCurrentScene();
    }

    public static void loadScene(int scene) {
        switch (scene) {
            case 0:
                game.currentScene = new LevelEditorScene("Level Editor");
                break;
            case 1:
                game.currentScene = new LevelScene("Level");
                break;
            case 2:
                game.currentScene = new PhysicsTestScene();
                break;
            case 3:
                game.currentScene = new RendererTestScene();
                break;
            default:
                Logger.log("Game: Unknown scene");
        }

        game.startCurrentScene();
    }

    public static Scene currentScene() {
        return game.currentScene;
    }

    public static Renderer getRenderer() {
        return game.renderer;
    }

    public static Physics2D getPhysics() {
        return game.physics;
    }

    @Override
    public void run() {
        // All time values are in seconds
        float lastTime = 0; // Last time the game loop iterated
        float deltaTime = 0; // Time since last frame

        // For rendering
        boolean ticked = false; // Has engine actually updated?

        // For debugging
        float timer = 0;
        int frames = 0;

        while (running) {
            float currentFrameTime = Time.getTime(); // Time for current frame
            float passedTime = currentFrameTime - lastTime;
            deltaTime += passedTime;
            timer += passedTime;
            lastTime = currentFrameTime; // Reset lastTime

            try {
                // Update the game as many times as possible even if the frame freezes
                while (deltaTime >= Time.TIME_STEP) {
                    update(deltaTime);
                    // Update = Graphics frame rate, FixedUpdate = Physics frame rate
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
            } catch (Exception e) {
                Logger.warn(ExceptionUtils.getStackTrace(e));
                e.printStackTrace();
                stop(1);
            }

        } // end loop

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

        if (currentScene != null)
            currentScene.update(dt);
        // TODO multithread physics, set time step higher than refresh rate for smoother results
        physics.physicsUpdate(dt);
    }

    /**
     * Redraws all objects in the current scene.
     */
    public void render() throws Exception {
        window.render((g2) -> {
            if (null != currentScene)
                currentScene.render(g2);
            renderer.render(g2);
            imgui.render(g2);
        });

    }

    // Helper Methods

    /**
     * Initializes the game engine, displays the window, and starts the current scene (if not null).
     */
    public synchronized void start() {
        if (running) // don't start if already running
            return;

        Logger.log("Engine: Starting %s %s", Preferences.TITLE, Preferences.VERSION);
        running = true;

        Logger.log("Engine: Loading assets");
        Assets.scanResources("assets"); // Load all game assets

        // Display window
        window.start();

        // Start thread
        thread = new Thread(this);
        thread.start();

        startCurrentScene();
    }

    /**
     * Shuts down the engine and closes the window.
     *
     * @param status The exit code for this application. A value of 0 indicates normal, while non-zero status codes
     *               indicate an error.
     */
    public synchronized void stop(int status) {
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
    private void startCurrentScene() {
        if (currentScene != null && running) {
            currentScene.start();
            physics.setScene(currentScene);
            renderer.setScene(currentScene);
            Logger.trace("Game: Loaded scene \"%s\"", currentScene.getName());
        }
    }

}
