package mayonez

import mayonez.graphics.*
import mayonez.graphics.renderer.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.util.*

/**
 * Allows colliders and shapes to be manually drawn onto the screen through
 * the scene's [DebugRenderer]. All shapes are specified using world
 * coordinates, and colliders are centered around an object's position.
 *
 * @author SlavSquatSuperstar
 */
class DebugDraw internal constructor(private val scale: Float, private val debugRenderer: DebugRenderer) {

    // Draw Points/Lines
    /**
     * Draws a point onto the screen.
     *
     * @param position where the point is located, in world coordinates
     * @param color the color to use
     */
    fun drawPoint(position: Vec2, color: MColor?) {
        // Fill a circle with radius "STROKE_SIZE" in pixels
        addShape(Circle(position.toScreen(), DebugShape.STROKE_SIZE), color, true, DrawPriority.POINT)
    }

    /**
     * Draws a line segment onto the screen.
     *
     * @param start the segment's starting point, in world coordinates
     * @param end the segment's ending point, in world coordinates
     * @param color the color to use
     */
    fun drawLine(start: Vec2, end: Vec2, color: MColor?) {
        addShape(Edge(start.toScreen(), end.toScreen()), color, false, DrawPriority.LINE)
    }

    /**
     * Draws a vector onto the screen.
     *
     * @param origin the vector's starting point, in world coordinates
     * @param direction how far away the vector's end point is, in world
     *     coordinates
     * @param color the color to use
     */
    fun drawVector(origin: Vec2, direction: Vec2, color: MColor?) = drawLine(origin, origin.add(direction), color)

    // Draw Shapes
    /**
     * Draws the outline of a shape onto the screen.
     *
     * @param shape a [Shape]
     * @param color the color to use
     */
    fun drawShape(shape: Shape?, color: MColor?) = debugDrawShape(shape, color, false, DrawPriority.SHAPE)

    /**
     * Draws a shape onto the screen and fills in the interior.
     *
     * @param shape a [Shape]
     * @param color the color to use
     */
    fun fillShape(shape: Shape?, color: MColor?) = debugDrawShape(shape, color, true, DrawPriority.FILL)

    // Internal Draw methods

    private fun debugDrawShape(shape: Shape?, color: MColor?, fill: Boolean, priority: DrawPriority) {
        // screen coordinates only
        when (shape) {
            is Edge -> addShape(shape.toScreen(), color, false, DrawPriority.LINE)
            is Polygon -> addShape(shape.toScreen(), color, fill, priority)
            is Circle -> addShape(shape.toScreen(), color, fill, priority)
            is Ellipse -> {
                if (shape.isCircle) addShape(shape.boundingCircle().toScreen(), color, fill, priority)
                addShape(shape.toScreen(), color, fill, priority)
            }
        }
    }

    private fun addShape(shape: Shape, color: MColor?, fill: Boolean, priority: DrawPriority) {
        // if color is null, draw black
        debugRenderer.addShape(DebugShape(shape, color ?: Colors.BLACK, fill, priority))
    }

    // Helper Methods

    /**
     * Converts a pair of coordinates from world to screen units.
     *
     * @return the corresponding screen pixels
     */
    private fun Vec2.toScreen(): Vec2 = this * scale

    /**
     * Converts all points on a shape to screen units.
     *
     * @return the corresponding screen pixels
     */
    private fun Shape.toScreen(): Shape = this.scale(Vec2(scale), Vec2(0f))

}