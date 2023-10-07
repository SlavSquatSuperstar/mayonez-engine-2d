package mayonez.graphics.debug

import mayonez.graphics.*
import mayonez.graphics.renderer.*
import mayonez.math.*
import mayonez.math.shapes.*

/**
 * Allows colliders and shapes to be manually drawn onto the screen through
 * the scene's [mayonez.graphics.renderer.DebugRenderer]. All shapes are
 * specified using world coordinates, and colliders are centered around an
 * object's position.
 *
 * Note: Due to how shapes are drawn in OpenGL, transparency is disabled,
 * and all colors will have their alpha set to 255. To draw transparent
 * shapes in the GL engine, use a [mayonez.graphics.sprites.Sprite]
 * instead.
 *
 * @author SlavSquatSuperstar
 */
class DebugDraw internal constructor(
    private val scale: Float, private val debugRenderer: DebugRenderer
) {

    companion object {
        /** How many pixels wide a curve should be drawn. */
        internal const val DEFAULT_STROKE_SIZE: Float = 2f
        internal val DEFAULT_COLOR: MColor = Colors.BLACK
    }

    // Draw Point Methods

    /**
     * Draws a point onto the screen and specifies the color.
     *
     * @param position where the point is located, in world coordinates
     * @param color the color to use
     */
    fun drawPoint(position: Vec2, color: MColor?) {
        // Fill a circle with radius "STROKE_SIZE" in pixels
        addShapeToRenderer(
            Circle(position.toScreen(scale), DEFAULT_STROKE_SIZE),
            createPointBrush(color)
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
        addShapeToRenderer(
            Edge(start.toScreen(scale), end.toScreen(scale)),
            brush ?: ShapeBrush.createLineBrush(DEFAULT_COLOR)
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
        drawLine(origin, origin.add(direction), color)
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
        drawLine(origin, origin.add(direction), brush)
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
        addShapeToRenderer(
            shape?.toScreen(scale) ?: return,
            brush ?: ShapeBrush.createOutlineBrush(DEFAULT_COLOR)
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
        addShapeToRenderer(
            shape?.toScreen(scale) ?: return,
            brush ?: ShapeBrush.createSolidBrush(DEFAULT_COLOR)
        )
    }

    // Private Draw methods

    private fun addShapeToRenderer(shape: Shape, brush: ShapeBrush) {
        debugRenderer.addShape(DebugShape(shape, brush))
    }

    private fun createPointBrush(color: MColor?): ShapeBrush {
        return ShapeBrush.createSolidBrush(color).setZIndex(DrawPriority.POINT.zIndex)
    }

}

// Helper Methods

/**
 * Converts a pair of coordinates from world to screen units.
 *
 * @return the corresponding screen pixels
 */
private fun Vec2.toScreen(screenScale: Float): Vec2 = this * screenScale

/**
 * Converts all points on a shape to screen units.
 *
 * @return the corresponding screen pixels
 */
private fun Shape.toScreen(screenScale: Float): Shape {
    return this.scale(Vec2(screenScale), Vec2(0f))
}