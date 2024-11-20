package mayonez.input;

import mayonez.math.*;

/**
 * Transforms positions and directions on the screen to positions and directions
 * in the world.
 *
 * @author SlavSquatSuperstar
 */
public interface PointTransformer {

    /**
     * Transform a screen position point into world position point.
     *
     * @param screenPos the position, in pixels
     * @return the position, in world units
     */
    Vec2 toWorldPosition(Vec2 screenPos);

    /**
     * Transform a screen displacement vector into world displacement vector.
     *
     * @param screenDisp the displacement, in pixels
     * @return the displacement, in world units
     */
    Vec2 toWorldDisplacement(Vec2 screenDisp);

}
