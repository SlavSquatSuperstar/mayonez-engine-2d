package slavsquatsuperstar.mayonezgl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.fileio.Assets;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics2d.Physics2D;
import slavsquatsuperstar.mayonez.renderer.GameRenderer;
import slavsquatsuperstar.mayonezgl.renderer.GLRenderer;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * The application that contains the engine's core loop.
 *
 * @author SlavSquatSuperstar
 */
public class GameGL { // can't implement runnable otherwise GLFW will crash

    static {
        System.out.println("static load");
        game = instance(); // "Lazy" singleton construction
    }

    // Singleton Fields
    private static GameGL game;

    // Game Fields
    private static boolean running = false;

    // Game Layers
    private static GameWindow window;
    private static SceneGL scene;
    private static GameRenderer renderer;
    private static Physics2D physics;

    public GameGL() {
        Initializer.init();

        window = new GLWindow("Mayonez + LWJGL", Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT);
        window.setKeyInput(KeyInput.INSTANCE);
        window.setMouseInput(MouseInput.INSTANCE);

        renderer = new GLRenderer();
        physics = new Physics2D();
    }

    public static GameGL instance() {
        return (null == game) ? game = new GameGL() : game;
    }

    // Getter Methods

    public static SceneGL getScene() {
        return GameGL.scene;
    }

    public static void setScene(SceneGL scene) {
        GameGL.scene = scene;
        game.startScene();
    }

    public static GameRenderer getRenderer() {
        return renderer;
    }

    public static Physics2D getPhysics() {
        return physics;
    }

    // Game Loop Methods

    public static void start() {
        if (running) return; // Don't start the game if already running

        running = true;
        Logger.log("Engine: Starting %s %s", Preferences.TITLE, Preferences.VERSION);

        Logger.log("Engine: Loading assets");
        Assets.scanResources("assets"); // Load all game assets

        window.start();
        game.startScene();
        game.run();
    }

    public static void stop(int status) {
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
            while (running && window.notClosedByUser()) {
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
    }

    public void render() {
        window.render((_g2) -> {
            if (scene != null) renderer.render(_g2); // don't pass a G2D object
        });
    }

    // Helper Methods

    private void startScene() {
        if (scene != null && running) {
            scene.start();
            renderer.setScene(scene);
            Logger.trace("Game: Loaded scene \"%s\"", scene.getName());
        }
    }

}