package slavsquatsuperstar.mayonez;

import org.apache.commons.lang3.exception.ExceptionUtils;
import slavsquatsuperstar.mayonez.physics2d.Physics2D;
import slavsquatsuperstar.mayonez.renderer.IMGUI;
import slavsquatsuperstar.mayonez.renderer.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * The application that contains the engine's core loop.
 *
 * @author SlavSquatSuperstar
 */
public class Game implements Runnable {

    static {
        game = Game.instance(); // "Lazy" singleton construction
    }

    /*
     * Field Declarations
     */

    // Time Fields
    public static final long TIME_STARTED = System.nanoTime();
    public static final float TIME_STEP = 1.0f / Preferences.FPS;

    // Singleton Fields
    // TODO make start/stop static?
    private static Game game;

    // Window Fields
    private final JFrame window;

    // Input Fields
    private final KeyInput keyboard;
    private final MouseInput mouse;

    // Thread Fields
    private Thread thread;
    private boolean running;
    private int width, height;

    // Renderer Fields
    private Graphics gfx;
    private BufferStrategy buffers;

    // Game Layers
    private Scene currentScene;
    private Physics2D physics;
    private Renderer renderer;
    private IMGUI imgui;

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
        keyboard = KeyInput.INSTANCE;
        mouse = MouseInput.INSTANCE;
        window.addKeyListener(keyboard);
        window.addMouseListener(mouse);
        window.addMouseMotionListener(mouse);

        physics = new Physics2D();
        renderer = new Renderer();
        imgui = IMGUI.INSTANCE;
    }

    // Game Loop Methods

    public synchronized static Game instance() { // only create the game once
        // get params from preferences
        return (null == game) ? game = new Game() : game;
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

    public static void loadScene(Scene newScene) {
        game.currentScene = newScene;
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

            float currentFrameTime = getTime(); // Time for current frame
            float passedTime = currentFrameTime - lastTime;
            deltaTime += passedTime;
            timer += passedTime;
            lastTime = currentFrameTime; // Reset lastTime

            try {
                // Update the game as many times as possible even if the frame freezes
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
        if (buffers == null) {
            initGraphics();
            return;
        }

        try {
            /*
             * Use a do-while loop to avoid losing buffer frames Source:
             * https://stackoverflow.com/questions/13590002/understand-bufferstrategy
             */
            do {
                // Clear the screen
                gfx = buffers.getDrawGraphics();
                gfx.clearRect(0, 0, width, height);

                Graphics2D g2 = (Graphics2D) gfx;
                if (null != currentScene) currentScene.render(g2);
                renderer.render(g2);
                imgui.render(g2);

                gfx.dispose();
                buffers.show();
            } while (buffers.contentsLost());
        } catch (IllegalStateException e) {
            Logger.log("Engine: Error rendering screen; trying again next frame.");
        }

    }

    // Helper Methods

    /**
     * Initializes the game engine, displays the window, and starts the current scene (if not null).
     */
    public static synchronized void start() {
        if (game.running) return; // Don't start if already running

        Logger.log("Engine: Starting");
        Logger.log("Welcome to %s %s", Preferences.TITLE, Preferences.VERSION);
        game.running = true;

        // Display window and initialize graphics buffer
        game.window.setVisible(true);
        game.initGraphics();

        // Start thread
        game.thread = new Thread(game);
        game.thread.start();

        game.startCurrentScene();
    }

    /**
     * Shuts down the engine and closes the window.
     *
     * @param status The exit code for this application. A value of 0 indicates normal, while non-zero status codes
     *               indicate an error.
     */
    public static synchronized void stop(int status) {
        game.running = false;
        Logger.log("Engine: Stopping with exit code %d", status);

        // Free System resources
        game.window.setVisible(false);
        game.window.dispose();
        game.gfx.dispose();

        // Stop thread
        game.thread.interrupt();
        Logger.printExitMessage();
        System.exit(status);
    }

    // Getter methods

    private void initGraphics() {
        if (window == null)
            return;
        try {
            window.createBufferStrategy(2);
            buffers = window.getBufferStrategy();
        } catch (IllegalStateException e) {
            Logger.log("Engine: Error initializing window graphics; trying again next frame.");
        }
    }

    /**
     * Starts the current scene, if not null
     */
    private void startCurrentScene() {
        if (currentScene != null && running) {
            currentScene.start();
            physics.setScene(currentScene);
            renderer.setScene(currentScene);
            Logger.log("Game: Loaded scene \"%s\"", currentScene.getName());
        }
    }

}
