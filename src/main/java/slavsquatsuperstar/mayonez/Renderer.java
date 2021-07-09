package slavsquatsuperstar.mayonez;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private final List<GameObject> objects;
    private final Camera camera;

    public Renderer(Camera camera) {
        this.camera = camera;
        objects = new ArrayList<>();
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

    // TODO Render layer: clear list and submit visible objects every frame?
    public void render(Graphics2D g2) {
        // Save a copy of the unmodified transform
        AffineTransform transform = g2.getTransform();

        // Move the screen and render everything at the offset position
        float camX = camera.getX();
        float camY = camera.getY();
        g2.translate(-camX, -camY);
        // TODO only render if in screen

        objects.forEach(o -> o.render(g2));

        // Reset the screen's transform to its unmodified state
        g2.setTransform(transform);
    }

}
