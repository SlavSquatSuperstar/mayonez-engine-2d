package mayonez

import mayonez.graphics.*
import mayonez.graphics.renderer.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.util.*

private val DEFAULT_COLOR: MColor = Colors.BLACK

/**
 * Allows colliders and shapes to be manually drawn onto the screen through
 * the scene's [DebugRenderer]. All shapes are specified using world
 * coordinates, and colliders are centered around an object's position.
 *
 * @author SlavSquatSuperstar
 */
class DebugDraw internal constructor(
    private val scale: Float, private val debugRenderer: DebugRenderer
) {

    companion object {
        /** How many pixels wide a curve should be drawn. */
        const val DEFAULT_STROKE_SIZE: Float = 2f
    }

    // TODO draw with brush
//    private var brush: ShapeBrush? = null
    private var zIndex: Int? = null
    private var strokeSize: Float = DEFAULT_STROKE_SIZE

    // Public Draw Methods

    /**
     * Sets a custom z-index for all subsequent shapes.
     *
     * @param zIndex the z-index
     */
    fun setZIndex(zIndex: Int) {
        this.zIndex = zIndex
    }

    /** Resets the z-index back to default. */
    fun resetZIndex() {
        zIndex = null
    }

    /**
     * Sets a custom stroke size for all subsequent shapes.
     *
     * @param strokeSize the stroke size
     */
    fun setStrokeSize(strokeSize: Float) {
        this.strokeSize = strokeSize
    }

    /** Resets the stroke size back to default. */
    fun resetStrokeSize() {
        strokeSize = DEFAULT_STROKE_SIZE
    }

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
            color, DrawPriority.POINT
        )
    }

    /**
     * Draws a line segment onto the screen and specifies the color.
     *
     * @param start the segment's starting point, in world coordinates
     * @param end the segment's ending point, in world coordinates
     * @param color the color to use
     */
    fun drawLine(start: Vec2, end: Vec2, color: MColor?) {
        addShapeToRenderer(
            Edge(start.toScreen(scale), end.toScreen(scale)),
            color, DrawPriority.LINE
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
    fun drawVector(origin: Vec2, direction: Vec2, color: MColor?) {
        drawLine(origin, origin.add(direction), color)
    }

    /**
     * Draws a shape outline onto the screen and specifies the color.
     *
     * @param shape a [Shape]
     * @param color the color to use
     */
    fun drawShape(shape: Shape?, color: MColor?) {
        addShapeToRenderer(
            shape?.toScreen(scale) ?: return,
            color, DrawPriority.SHAPE_OUTLINE
        )
    }

//    fun drawShape(shape: Shape?, brush: ShapeBrush?) {
//
//    }

    /**
     * Draws a solid shape onto the screen and specifies the color.
     *
     * @param shape a [Shape]
     * @param color the color to use
     */
    fun fillShape(shape: Shape?, color: MColor?) {
        addShapeToRenderer(
            shape?.toScreen(scale) ?: return,
            color, DrawPriority.SOLID_SHAPE
        )
    }

    // Private Draw methods

    private fun addShapeToRenderer(
        shape: Shape, color: MColor?, priority: DrawPriority
    ) {
        val zIndex = zIndex ?: priority.zIndex
        val brush = ShapeBrush(color ?: DEFAULT_COLOR, priority.fill, zIndex, strokeSize)
        debugRenderer.addShape(DebugShape(shape, brush))
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