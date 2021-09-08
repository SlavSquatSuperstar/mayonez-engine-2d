package slavsquatsuperstar.mayonezgl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import slavsquatsuperstar.fileio.Assets;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Time;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonezgl.renderer.RendererGL;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * The application that contains the engine's core loop.
 *
 * @author SlavSquatSuperstar
 */
public class GameGL { // can't implement runnable otherwise GLFW will crash

    // Singleton Fields
    private static GameGL game;

    // Game Fields
    private static boolean running = false;
    private static SceneGL scene;

    // Window Fields
    private final WindowGL window;
    private static final int WIDTH = Preferences.SCREEN_WIDTH;
    private static final int HEIGHT = Preferences.SCREEN_HEIGHT;

    // Game Layers
    private static final RendererGL renderer = new RendererGL();

    public GameGL() {
        window = new WindowGL("Mayonez + LWJGL", WIDTH, HEIGHT);
        window.setKeyInput(KeyInput.INSTANCE);
        window.setMouseInput(MouseInput.INSTANCE);
    }

    public static GameGL instance() {
        return game == null ? game = new GameGL() : game;
    }

    // Getter Methods

    public static SceneGL getScene() {
        return GameGL.scene;
    }

    public static void setScene(SceneGL scene) {
        GameGL.scene = scene;
        startScene();
    }

    public static RendererGL getRenderer() {
        return renderer;
    }

    // Game Loop Methods

    public void start() {
        if (running) return; // Don't start the game if already running

        running = true;
        Logger.log("Engine: Starting");
        Logger.log("Welcome to %s %s", Preferences.TITLE, Preferences.VERSION);

        Logger.log("Engine: Loading assets");
        Assets.scanResources("assets"); // Load all game assets

        window.start();
        startScene();
        run();
    }

    public void stop(int status) {
        if (!running) return;

        Logger.log("Engine: Stopping with exit code %d", status);
        running = false;
        window.stop();

        Logger.printExitMessage();
        System.exit(status);
    }

    public void run() {
        // All time values are in seconds
        float lastTime = 0f; // Last time the game loop iterated
        float currentTime; // Time of current frame
        float deltaTime = 0f; // Unprocessed time since last frame

        float timer = 0f;
        int frames = 0;

        try {
            // Render to the screen until the user closes the window or pressed the ESCAPE key
            while (running && !window.isClosedByUser()) {
                boolean ticked = false; // Has engine actually updated?
                currentTime = (float) glfwGetTime();
                float passedTime = currentTime - lastTime;
                deltaTime += passedTime;
                timer += passedTime;
                lastTime = currentTime;

                window.beginFrame(); // Poll input

                while (deltaTime >= Time.TIME_STEP) {
                    deltaTime -= Time.TIME_STEP;
                    update(Time.TIME_STEP);
                    ticked = true;
                }
                if (ticked) {
                    render();
                    frames++;
                }
                if (timer >= 1) {
                    Logger.log("Frames per Second: %d", frames);
                    timer = 0;
                    frames = 0;
                }
                window.endFrame();
            }
        } catch (Exception e) {
            Logger.log(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
            stop(1);
        }

        stop(0);
    }

    public void update(float dt) {
        if (scene != null) scene.update(dt);
        Logger.log(MouseInput.getScreenDisp());
    }

    public void render() {
        window.render((_g2) -> {
            if (scene != null) renderer.render();
        });
    }

    // Helper Methods

    private static void startScene() {
        if (scene != null && running) {
            scene.start();
            scene.getObjects(null).forEach(renderer::addObject);
            Logger.trace("Game: Loaded scene \"%s\"", scene.getName());
        }
    }

}