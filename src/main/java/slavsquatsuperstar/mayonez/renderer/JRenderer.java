package slavsquatsuperstar.mayonez.renderer;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.graphics.DebugDraw;
import slavsquatsuperstar.mayonez.graphics.JCamera;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws all in-game textures and shapes onto the screen with Java's AWT and Swing libraries.
 */
public final class JRenderer extends Renderer {

    private final List<GameObject> objects;
    private final DebugDraw debugDraw; // put DebugDraw in Renderer to access camera offset
    /**
     * Reference to scene camera
     */
    private JCamera camera = null;

    public JRenderer() {
        objects = new ArrayList<>();
        debugDraw = DebugDraw.INSTANCE;
    }

    public void render(Graphics2D g2) {
        assert g2 != null;

        // Save a copy of the unmodified transform
        AffineTransform xf = g2.getTransform();

        // Move the screen and render everything at the offset position
        if (camera != null) {
            Vec2 camOffset = camera.getOffset().mul(-Mayonez.getScene().getCellSize());
            g2.translate(camOffset.x, camOffset.y);
        }
        objects.forEach(o -> o.render(g2));
        debugDraw.render(g2);

        // Reset the screen's transform to its unmodified state
        g2.setTransform(xf);
    }

    // GameObject Methods

    /**
     * Removes all objects from this renderer and submits all {@link GameObject}s from the given scene for rendering.
     *
     * @param newScene a scene
     */
    @Override
    public void setScene(Scene newScene) {
        camera = (JCamera) newScene.getCamera();
        objects.clear();
        objects.addAll(newScene.getObjects(null));
    }

    /**
     * Submit a {@link GameObject} for rendering.
     *
     * @param obj the game object
     */
    @Override
    public void addObject(GameObject obj) {
        objects.add(obj);
    }

    @Override
    public void removeObject(GameObject obj) {
        objects.remove(obj);
    }

}
