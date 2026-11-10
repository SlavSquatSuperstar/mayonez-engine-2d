package mayonez.graphics.debug

import mayonez.graphics.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.renderer.*

// TODO render server
// TODO draw sprite on-demand
// TODO draw text on-demand
/**
 * Allows colliders and shapes to be manually drawn onto the screen through
 * the scene's [mayonez.renderer.DebugRenderer]. All shapes are specified
 * using world coordinates, and all collider shapes are centered around the
 * collider's position.
 *
 * Note: Due to how shapes are drawn in OpenGL, transparency is currently
 * disabled, and all colors will have their alpha set to 255. To draw
 * transparent shapes in the GL backend, use a [mayonez.graphics.sprites.Sprite]
 * with a shape texture instead.
 *
 * @author SlavSquatSuperstar
 */
class DebugDraw internal constructor(
    private val debugRenderer: DebugRenderer
) {

    // Draw Point Methods

    /**
     * Draws a point onto the screen and specifies the color.
     *
     * @param position where the point is located, in world coordinates
     * @param color the color to use
     */
    fun drawPoint(position: Vec2, color: MColor?) {
        // Fill a circle with radius "STROKE_SIZE" in pixels
        val pixelScale = debugRenderer.viewport.cameraScale
        debugRenderer.addShape(
            Circle(position, ShapeBrush.DEFAULT_STROKE_SIZE / pixelScale),
            ShapeBrush.createPointBrush(color)
        )
    }

    // Draw Line Methods

    /**
     * Draws a line segment onto the screen and specifies the color.
     *
     * @param start the segment's starting point, in world coordinates
     * @param end the segment's ending point, in world coordinates
     * @param color the color to use
     */
    fun drawLine(start: Vec2?, end: Vec2?, color: MColor?) {
        drawLine(start, end, ShapeBrush.createLineBrush(color))
    }

    /**
     * Draws a line segment onto the screen and specifies the brush.
     *
     * @param start the segment's starting point, in world coordinates
     * @param end the segment's ending point, in world coordinates
     * @param brush the brush to use
     */
    fun drawLine(start: Vec2?, end: Vec2?, brush: ShapeBrush?) {
        if (start == null || end == null) return
        debugRenderer.addShape(
            Edge(start, end),
            brush ?: ShapeBrush.createLineBrush()
        )
    }

    /**
     * Draws a vector onto the screen and specifies the color.
     *
     * @param origin the vector's starting point, in world coordinates
     * @param direction how far away the vector's end point is, in world
     *     coordinates
     * @param color the color to use
     */
    fun drawVector(origin: Vec2?, direction: Vec2?, color: MColor?) {
        if (origin == null || direction == null) return
        drawLine(origin, origin + direction, color)
    }

    /**
     * Draws a vector onto the screen and specifies the brush.
     *
     * @param origin the vector's starting point, in world coordinates
     * @param direction how far away the vector's end point is, in world
     *     coordinates
     * @param brush the color to use
     */
    fun drawVector(origin: Vec2?, direction: Vec2?, brush: ShapeBrush?) {
        if (origin == null || direction == null) return
        drawLine(origin, origin + direction, brush)
    }

    // Draw Shape Methods

    /**
     * Draws a shape outline onto the screen and specifies the color.
     *
     * @param shape a [Shape]
     * @param color the color to use
     */
    fun drawShape(shape: Shape?, color: MColor?) {
        drawShape(shape, ShapeBrush.createOutlineBrush(color))
    }

    /**
     * Draws a shape outline onto the screen and specifies the brush.
     *
     * @param shape a [Shape]
     * @param brush the brush to use
     */
    fun drawShape(shape: Shape?, brush: ShapeBrush?) {
        debugRenderer.addShape(
            shape ?: return,
            brush ?: ShapeBrush.createOutlineBrush()
        )
    }

    /**
     * Draws a solid shape onto the screen and specifies the color.
     *
     * @param shape a [Shape]
     * @param color the color to use
     */
    fun fillShape(shape: Shape?, color: MColor?) {
        fillShape(shape, ShapeBrush.createSolidBrush(color))
    }

    /**
     * Draws a solid shape onto the screen and specifies the brush.
     *
     * @param shape a [Shape]
     * @param brush the brush to use
     */
    fun fillShape(shape: Shape?, brush: ShapeBrush?) {
        debugRenderer.addShape(
            shape ?: return,
            brush ?: ShapeBrush.createSolidBrush()
        )
    }

    /**
     * Draws a [DebugShape] onto the screen.
     *
     * @param shape the shape and its brush
     */
    fun drawShape(shape: DebugShape?) {
        debugRenderer.addShape(shape ?: return)
    }

}

// Helper Methods
private fun DebugRenderer.addShape(shape: Shape, brush: ShapeBrush) {
    this.addShape(DebugShape(shape, brush))
}
