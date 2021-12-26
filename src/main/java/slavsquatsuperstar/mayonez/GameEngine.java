package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.physics2d.Physics2D;
import slavsquatsuperstar.mayonez.renderer.Renderer;

/**
 * The application that contains the engine's core loop.
 *
 * @author SlavSquatSuperstar
 */
// TODO probably better off with enums of companion objects
abstract class GameEngine {

    protected boolean running = false;

    // Game Layers
    protected Physics2D physics;
    protected Renderer renderer;
    protected Scene scene;
    protected Window window;

    // Resource Management Methods

    /**
     * Set up system resources and initialize the application
     */
    public abstract void start();

    /**
     * Free system resources and quit the application.
     */
    public abstract void stop();

    // Game Loop Methods

    public void beginFrame() {
        window.beginFrame();
    }

    /**
     * Refreshes all objects in the current scene.
     *
     * @param dt seconds since the last frame
     */
    public abstract void update(float dt);

    /**
     * Redraws all objects in the current scene.
     */
    public abstract void render();

    public void endFrame() {
        window.endFrame();
    }

    // Scene Methods

    /**
     * Starts the current scene, if not null
     */
    protected void startScene() {
        if (scene != null && running) {
            scene.start();
            physics.setScene(scene);
            renderer.setScene(scene);
            Logger.trace("Game: Loaded scene \"%s\"", scene.getName());
        }
    }

    protected void setScene(Scene scene) {
        this.scene = scene;
        startScene();
    }

}
