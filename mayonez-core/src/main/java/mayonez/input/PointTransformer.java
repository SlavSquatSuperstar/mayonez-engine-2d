package mayonez.input;

import mayonez.math.*;

/**
 * Transforms a point on the screen into a point in the world.
 *
 * @author SlavSquatSuperstar
 */
public interface PointTransformer {

    /**
     * Transform screen coordinates into world coordinates.
     *
     * @param screen a pixel on the screen
     * @return the world position of the pixel
     */
    Vec2 toWorld(Vec2 screen);

}
