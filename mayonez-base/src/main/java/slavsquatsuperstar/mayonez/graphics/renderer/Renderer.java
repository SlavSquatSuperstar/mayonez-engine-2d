package slavsquatsuperstar.mayonez.graphics.renderer;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.graphics.Camera;

import java.awt.*;

/**
 * Draws objects in a scene to the screen.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Renderer {

    // Object Methods

    /**
     * Submits a {@link GameObject} for rendering.
     *
     * @param o the game object
     */
    public abstract void addObject(GameObject o);

    /**
     * Removes a {@link GameObject} from the renderer.
     *
     * @param o the game object
     */
    public abstract void removeObject(GameObject o);

    /**
     * Clears this renderer and submits all objects from the given {@link Scene} for rendering.
     *
     * @param newScene a scene
     */
    public abstract void setScene(Scene newScene);

    // Game Loop Methods

    /**
     * Draws all objects in the scene.
     *
     * @param g2 a {@link Graphics2D} object for the AWT engine/
     */
    public abstract void render(Graphics2D g2);

    /**
     * Removes all objects and frees resources upon quitting the program.
     */
    public abstract void stop();

    // Helper Methods

    public Camera getCamera() {
        return Mayonez.getScene().getCamera();
    }

}
