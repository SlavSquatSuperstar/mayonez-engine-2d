package slavsquatsuperstar.mayonez.renderer;

import slavsquatsuperstar.math.Vec2;

/**
 * The viewport into the world.
 *
 * @author SlavSquatSuperstar
 */
public interface Camera {
    Vec2 getOffset();

    void setOffset(Vec2 offset);
}
