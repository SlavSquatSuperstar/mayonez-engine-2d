package mayonez.renderer;

import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.math.*;
import org.joml.*;

/**
 * Defines from what perspective to render the scene and what transformations
 * to apply.
 * <p>
 * Source: <a href="https://learnopengl.com/Getting-started/Coordinate-Systems">
 * Learn OpenGL - Coordinate Systems</a>
 *
 * @author SlavSquatSuperstar
 */
public interface Viewport extends PointTransformer {

    // TODO more transform methods

    // Color Methods

    /**
     * The background color of this viewport's screen.
     *
     * @return the color
     */
    Color getBackgroundColor();

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
    Vec2 getScreenCenter();

    /**
     * The position in pixels of the viewport's bottom-left corner (canvas origin).
     *
     * @return the camera's offset
     */
    Vec2 getScreenOffset();

    // Rotation Methods

    /**
     * The viewport's rotation in the scene.
     *
     * @return the rotation
     */
    float getRotation();

    // Size Methods

    /**
     * How zoomed in or out the viewport is.
     *
     * @return the zoom, a positive number
     */
    float getZoom();

    /**
     * The size of the viewport relative to the screen, or the number of screen
     * pixels per world unit.
     *
     * @return the scale
     */
    float getCameraScale();

    // Camera Transformation Methods

    /**
     * The view matrix, which transforms world space into view space, or the
     * camera's point of view.
     *
     * @return the 4x4 view matrix, identity by default
     */
    default Matrix4f getViewMatrix() {
        return new Matrix4f();
    }

    /**
     * The projection matrix, which transforms view space into clip space, or the
     * normalized screen coordinates.
     *
     * @return the 4x4 projection matrix, identity by default
     */
    default Matrix4f getProjectionMatrix() {
        return new Matrix4f();
    }

    /**
     * Adjust the view matrix after the camera has been moved, rotated, or zoomed.
     */
    default void updateViewMatrix() {
    }

    /**
     * Adjust the projection matrix after the screen size or aspect ratio has
     * been changed.
     */
    default void updateProjectionMatrix() {
    }

}
