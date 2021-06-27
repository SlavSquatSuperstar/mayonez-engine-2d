package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.game.LevelEditorScene;
import slavsquatsuperstar.game.LevelScene;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game implements Runnable {

    /*
     * Field Declarations
     */

    // Time Fields
    public static float timeStep = 1.0f / Preferences.FPS;
    public static long timeStarted = System.nanoTime();

    // Singleton Fields
    private static Game game;

    // Thread Fields
    private Thread thread;
    private boolean running;

    // Input Fields
    private KeyInput keyboard;
    private MouseInput mouse;

    // Window Fields
    private JFrame window;
    private int width, height;

    // Renderer Fields
    private Graphics g;
    private BufferStrategy bs;

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

        initGraphics();
    }

    // Game Loop Methods

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
                while (deltaTime >= timeStep) {
                    update(deltaTime);
                    // Update = Graphics frame rate, FixedUpdate = Physics frame rate
                    deltaTime -= timeStep;
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
    public void update(float dt) throws Exception {
        // Poll events
        if (keyboard.keyDown("exit"))
            running = false;

        if (currentScene != null)
            currentScene.update(dt);
    }

    /**
     * Redraws all objects in the current scene.
     */
    public void render() throws Exception {
        if (bs == null) {
            initGraphics();
            return;
        }

        /*
         * Use a do-while loop to avoid losing buffer frames Source:
         * https://stackoverflow.com/questions/13590002/understand-bufferstrategy
         */
        do {
            // Clear the screen
            g = bs.getDrawGraphics();
            g.clearRect(0, 0, width, height);

            if (null != currentScene)
                currentScene.render((Graphics2D) g);

            g.dispose();
            bs.show();
        } while (bs.contentsLost());

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

        // Start scene
        startCurrentScene();
    }

    /**
     * Shuts down the engine and hides the window.
     */
    public synchronized void stop(int status) {
        running = false;
        Logger.log("Engine: Stopping with exit code %d", status);

        // Free System resources
        window.setVisible(false);
        window.dispose();
        g.dispose();

        // Stop thread
        thread.interrupt();
        if (Logger.saveLogs)
            Logger.log("Logger: Saved log to file \"%s\"", Logger.logFilename);
        System.exit(status);
    }

    // Getters and Setters

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
        return (System.nanoTime() - timeStarted) / 1.0E9f;
    }

    public static boolean isFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = ge.getDefaultScreenDevice();
        return device.getFullScreenWindow() != null;
    }

    public static void loadScene(int scene) {
        switch (scene) {
            case 0:
                game.currentScene = new LevelEditorScene("Level Editor");
                break;
            case 1:
                game.currentScene = new LevelScene("Level");
                break;
            default:
                Logger.log("Game: Unknown scene");
        }

        game.startCurrentScene();
    }

    public static Scene currentScene() {
        return game.currentScene;
    }

    // Private Methods

    private void initGraphics() {
        if (window == null || !window.isVisible())
            return;

        window.createBufferStrategy(2);
        bs = window.getBufferStrategy();
    }

    private void startCurrentScene() {
        if (currentScene != null && running) {
            currentScene.start();
            Logger.log("Game: Loaded scene \"%s\"", currentScene.getName());
        }
    }

}
