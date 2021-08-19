package slavsquatsuperstar.mayonezgl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Time;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

/**
 * The application that contains the engine's core loop.
 *
 * @author SlavSquatSuperstar
 */
public class GameGL { // can't implement runnable otherwise GLFW will crash

    // Singleton Fields
    public static GameGL game;

    // Game Fields
    private boolean running = false;

    // Renderer Fields
    private final WindowGL window;

    public GameGL() {
        window = new WindowGL("Mayonez + LWJGL", 300, 300);
    }

    public static GameGL instance() {
        return game == null ? game = new GameGL() : game;
    }

    // Game Loop Methods

    public void start() {
        if (running) // Don't start the game if already running
            return;

        Logger.log("Engine: Starting");
        Logger.log("Welcome to %s %s", Preferences.TITLE, Preferences.VERSION);
        running = true;

        window.start();
        run();
    }

    public void stop(int status) {
        if (!running)
            return;

        Logger.log("Engine: Stopping with exit code %d", status);
        running = false;
        window.stop();

        Logger.printExitMessage();
        System.exit(status);
    }

    public void run() {
        // All time values are in seconds
        float lastTime = 0; // Last time the game loop iterated
        float deltaTime = 0; // Time since last frame

        // For rendering
        boolean ticked = false; // Has engine actually updated?

        // For debugging
        float timer = 0;
        int frames = 0;

        try {
            // Render to the screen until the user closes the window or pressed the ESCAPE key
            while (window.isOpen()) {
                float currentFrameTime = Time.getTime(); // Time for current frame
                float passedTime = currentFrameTime - lastTime;
                deltaTime += passedTime;
                timer += passedTime;
                lastTime = currentFrameTime; // Reset lastTime

                window.update(); // Poll input

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
                    Logger.log("Frames per Second: %d", frames);
                    frames = 0;
                    timer = 0;
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
        if (KeyInputGL.isKeyPressed(GLFW_KEY_W))
            Logger.log("Begin W");
        if (KeyInputGL.isKeyHeld(GLFW_KEY_W))
            Logger.log("W");
        if (MouseInputGL.isButtonPressed(GLFW_MOUSE_BUTTON_1))
            Logger.log("Position: %.4f, %.4f", MouseInputGL.getX(), MouseInputGL.getY());
        if (MouseInputGL.isDragging())
            Logger.log("Displacement: %.4f, %.4f", MouseInputGL.getDx(), MouseInputGL.getDy());
    }

    public void render() {
        window.render();
    }
}