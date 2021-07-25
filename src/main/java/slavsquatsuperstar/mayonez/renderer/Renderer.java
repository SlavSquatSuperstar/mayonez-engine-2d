package slavsquatsuperstar.mayonez.renderer;

import slavsquatsuperstar.mayonez.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws all in-game textures and shapes onto the screen.
 */
public class Renderer {

    private final List<GameObject> objects;
    private final DebugDraw debugDraw; // put DebugDraw in Renderer to access camera offset
    /**
     * Reference to scene camera
     */
    private Camera camera = null;

    public Renderer() {
        objects = new ArrayList<>();
        debugDraw = new DebugDraw();
    }

    // TODO Render layer: clear list and submit visible objects every frame?
    public void render(Graphics2D g2) {
        // Save a copy of the unmodified transform
        AffineTransform transform = g2.getTransform();

        // Move the screen and render everything at the offset position
        if (camera != null) {
            Vector2 camOffset = camera.getOffset().mul(-Game.currentScene().getCellSize());
            g2.translate(camOffset.x, camOffset.y);
        }

        // TODO optimize: only render if in screen

        objects.forEach(o -> o.render(g2));
        debugDraw.render(g2);

        // Reset the screen's transform to its unmodified state
        g2.setTransform(transform);
    }

    // GameObject Methods

    /**
     * Removes all objects from this renderer and submits all {@link GameObject}s from the given scene for rendering.
     *
     * @param newScene a scene
     */
    public void setScene(Scene newScene) {
        camera = newScene.camera();
        objects.clear();
        objects.addAll(newScene.getObjects(null));
    }

    /**
     * Submit a {@link GameObject} for rendering.
     */
    public void add(GameObject obj) {
        objects.add(obj);
    }

    public void remove(GameObject obj) {
        objects.remove(obj);
    }

}
