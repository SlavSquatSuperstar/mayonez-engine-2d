package slavsquatsuperstar.mayonez.graphics.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import slavsquatsuperstar.mayonez.physics.shapes.Edge;

/**
 * An {@link Edge} object that can be drawn in OpenGL.
 *
 * @author SlavSquatSuperstar
 */
public class LinePrimitive {

    private Vector2f start, end;
    private Vector3f color;
    private float lifetime;

    public LinePrimitive(Vector2f start, Vector2f end, Vector3f color, float lifetime) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.lifetime = lifetime;
    }

    public float beginFrame() {
        this.lifetime--;
        return this.lifetime;
    }

    public Vector2f getStart() {
        return start;
    }

    public Vector2f getEnd() {
        return end;
    }

    public Vector3f getColor() {
        return color;
    }


}
