package slavsquatsuperstar.mayonez.graphics.renderer;

import org.joml.Vector4f;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.shapes.Edge;

/**
 * An {@link Edge} object that can be drawn in OpenGL.
 *
 * @author SlavSquatSuperstar
 */
public class DebugLine {

    public final Vec2 start, end;
    public final Vector4f color;
    public final int zIndex;

    public DebugLine(Vec2 start, Vec2 end, Vector4f color, int zIndex) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.zIndex = zIndex;
    }

}
