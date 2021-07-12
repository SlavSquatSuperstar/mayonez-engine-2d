package slavsquatsuperstar.mayonez;

import org.apache.commons.lang3.exception.ExceptionUtils;
import slavsquatsuperstar.game.LevelEditorScene;
import slavsquatsuperstar.game.LevelScene;
import slavsquatsuperstar.game.PhysicsTestScene;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * The application that contains the engine's core loop.
 *
 * @author SlavSquatSuperstar
 */
public class Game implements Runnable {

    /*
     * Field Declarations
     */

    // Time Fields
    public static final long TIME_STARTED = System.nanoTime();
    public static final float TIME_STEP = 1.0f / Preferences.FPS;

    // Singleton Fields
    // TODO make start/stop static?
    private static Game game;

    // Input Fields
    // TODO make input singleton
    private final KeyInput keyboard;
    private final MouseInput mouse;

    // Window Fields
    private final JFrame window;

    // Thread Fields
    private Thread thread;
    private boolean running;
    private int width, height;

    // Renderer Fields
    private Graphics gfx;
    private BufferStrategy buffers;
    private DebugDraw debugDraw;

    // Scene Fields
    private Scene currentScene;

    /*
     * Method Declarations
     */

    private Game() {
        // Set up the window
        window = new JFrame(Preferences.TITLE + " " + Preferences.VERSION);
        width = Preferences.SCREEN_WIDTH;
        height = Preferences.SCREEN_HEIGHT;
        window.setSize(width, height);
        window.setResizable(false);
        window.setLocationRelativeTo(null); // center in screen
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // make sure 'x' button quits program

        // Add input listeners
        keyboard = new KeyInput();
        mouse = new MouseInput();
        window.addKeyListener(keyboard);
        window.addMouseListener(mouse);
        window.addMouseMotionListener(mouse);
    }

    // Game Loop Methods

    public synchronized static Game instance() { // only create the game once
        // get params from preferences
        return (null == game) ? game = new Game() : game;
    }

    public static KeyInput keyboard() {
        return game.keyboard;
    }

    public static MouseInput mouse() {
        return game.mouse;
    }

    /**
     * @return the time in seconds since this game started.
     */
    public static float getTime() {
        return (System.nanoTime() - TIME_STARTED) / 1.0E9f;
    }

    public static boolean isFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = ge.getDefaultScreenDevice();
        return device.getFullScreenWindow() != null;
    }

    // Getters and Setters

    public static void loadScene(int scene) {
        switch (scene) {
            case 0:
                game.currentScene = new LevelEditorScene("Level Editor");
                break;
            case 1:
                game.currentScene = new LevelScene("Level");
                break;
            case 2:
                game.currentScene = new PhysicsTestScene("Physics Test Scene");
                break;
            default:
                Logger.log("Game: Unknown scene");
        }

        game.startCurrentScene();
    }

    public static Scene currentScene() {
        return game.currentScene;
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

            float currentFrameTime = getTime(); // Time for current frame
            float passedTime = currentFrameTime - lastTime;
            deltaTime += passedTime;
            timer += passedTime;
            lastTime = currentFrameTime; // Reset lastTime

            try {
                // Update the game as many times as necessary even if the frame freezes
                while (deltaTime >= TIME_STEP) {
                    update(deltaTime);
                    // Update = Graphics frame rate, FixedUpdate = Physics frame rate
                    deltaTime -= TIME_STEP;
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
                    Logger.log("Frames per Second: %d", frames);
                    frames = 0;
                    timer = 0;
                }
            } catch (Exception e) {
                Logger.log(ExceptionUtils.getStackTrace(e));
                e.printStackTrace();
                stop(1);
            }

        } // end loop

        stop(0);

    }

    /**
     * Refreshes all objects in the current scene.
     *
     * @param dt The time elapsed since the last frame
     */
    public void update(float dt) throws Exception { // TODO pass dt or use Game.timestep?
        // TODO Poll input events
        if (keyboard.keyDown("exit"))
            running = false;

        if (currentScene != null)
            currentScene.update(dt);
    }

    /**
     * Redraws all objects in the current scene.
     */
    public void render() throws Exception {
        if (buffers == null) {
            initGraphics();
            return;
        }

        /*
         * Use a do-while loop to avoid losing buffer frames Source:
         * https://stackoverflow.com/questions/13590002/understand-bufferstrategy
         */
        do {
            // Clear the screen
            gfx = buffers.getDrawGraphics();
            gfx.clearRect(0, 0, width, height);

            if (null != currentScene)
                currentScene.render((Graphics2D) gfx);
            debugDraw.render((Graphics2D) gfx);

            gfx.dispose();
            buffers.show();
        } while (buffers.contentsLost());

    }

    /**
     * Initializes the game engine, displays the window, and starts the current scene (if not null).
     */
    public synchronized void start() {
        if (running) // don't start if already running
            return;

        Logger.log("Engine: Starting");
        Logger.log("Welcome to %s %s", Preferences.TITLE, Preferences.VERSION);
        running = true;

        // Start thread
        thread = new Thread(this);
        thread.start();

        // Display window and initialize graphics buffer
        window.setVisible(true);
        initGraphics();

        startCurrentScene();
    }

    /**
     * Shuts down the engine and closes the window.
     */
    public synchronized void stop(int status) {
        running = false;
        Logger.log("Engine: Stopping with exit code %d", status);

        // Free System resources
        window.setVisible(false);
        window.dispose();
        gfx.dispose();

        // Stop thread
        thread.interrupt();
        if (Logger.saveLogs)
            Logger.log("Logger: Saved log to file \"%s\"", Logger.logFilename);
        System.exit(status);
    }

    // Private Methods

    private void initGraphics() {
        if (window == null)
            return;
        try {
            window.createBufferStrategy(2);
            buffers = window.getBufferStrategy();
            debugDraw = new DebugDraw();
        } catch (IllegalStateException e) {
            Logger.log("Engine: Error initializing window graphics; trying again next frame.");
        }
    }

    /**
     * Start sthe current scene, if not null
     */
    private void startCurrentScene() {
        if (currentScene != null && running) {
            currentScene.start();
            Logger.log("Game: Loaded scene \"%s\"", currentScene.getName());
        }
    }

}
