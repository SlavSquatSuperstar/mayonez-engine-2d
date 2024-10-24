package mayonez.renderer;

import mayonez.input.*;
import mayonez.math.*;
import org.joml.*;

/**
 * Defines from what perspective to render the scene and what transformations
 * to apply.
 * <br>
 * Source: <a href="https://learnopengl.com/Getting-started/Coordinate-Systems">
 * Learn OpenGL</a>
 *
 * @author SlavSquatSuperstar
 */
public interface Viewport extends PointTransformer {

    // TODO more transform methods

    // Position Methods

    /**
     * The viewport's position in the scene in world units.
     *
     * @return the position
     */
    Vec2 getPosition();

    /**
     * The position in pixels of the viewport's center position.
     *
     * @return the camera's screen position
     */
    Vec2 getViewCenter();

    /**
     * The position in pixels of the viewport's bottom-left corner (canvas origin).
     *
     * @return the camera's offset
     */
    Vec2 getScreenOffset();

    // Camera Effects

    /**
     * The viewport's rotation in the scene.
     *
     * @return the rotation
     */
    float getRotation();

    /**
     * How zoomed in or out the viewport is.
     *
     * @return the zoom, a positive number
     */
    float getZoom();

    // Camera Transformations

    /**
     * The view matrix, which transforms world space into view space, or the camera's
     * point of view.
     *
     * @return the 4x4 view matrix
     */
    Matrix4f getViewMatrix();

    /**
     * The projection matrix, which transforms view space into clip space, or normalized
     * screen coordinates.
     *
     * @return the 4x4 projection matrix
     */
    Matrix4f getProjectionMatrix();

}
