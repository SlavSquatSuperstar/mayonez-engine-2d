package mayonez

import mayonez.annotations.EngineType
import mayonez.annotations.UsesEngine
import mayonez.graphics.Colors
import mayonez.graphics.renderer.DebugRenderer
import mayonez.graphics.renderer.DebugShape
import mayonez.math.Vec2
import mayonez.physics.shapes.*
import mayonez.util.MColor

/**
 * Draws colliders and shapes onto the screen. All shapes are specified using world coordinates, and colliders are
 * centered around an object's position.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
object DebugDraw {

    const val STROKE_SIZE = 2f
    private val scale: Float
        get() = SceneManager.currentScene.scale
    private val debugRenderer: DebugRenderer
        get() = SceneManager.currentScene.debugRenderer

    // Draw Points/Lines
    /**
     * Draws a point onto the screen.
     *
     * @param position where the point is located, in world coordinates
     * @param color    the color to use
     */
    @JvmStatic
    fun drawPoint(position: Vec2, color: MColor?) {
        if (!Mayonez.started) return
        // Fill a circle with radius "STROKE_SIZE" in pixels
        addShape(Circle(position.toScreen(), STROKE_SIZE), color, true, DebugShape.Priority.POINT)
    }

    /**
     * Draws a line segment onto the screen.
     *
     * @param start the segment's starting point, in world coordinates
     * @param end   the segment's ending point in, world coordinates
     * @param color the color to use
     */
    @JvmStatic
    fun drawLine(start: Vec2, end: Vec2, color: MColor?) {
        if (!Mayonez.started) return
        addShape(Edge(start.toScreen(), end.toScreen()), color, false, DebugShape.Priority.LINE)
    }

    /**
     * Draws a vector onto the screen.
     *
     * @param origin    the vector's starting point, in world coordinates
     * @param direction how far away the vector's end point is, in world coordinates
     * @param color     the color to use
     */
    @JvmStatic
    fun drawVector(origin: Vec2, direction: Vec2, color: MColor?) = drawLine(origin, origin.add(direction), color)

    // Draw Shapes
    /**
     * Draws the outline of a shape onto the screen.
     *
     * @param shape a [Shape]
     * @param color the color to use
     */
    @JvmStatic
    fun drawShape(shape: Shape?, color: MColor?) = drawShape(shape, color, DebugShape.Priority.SHAPE.zIndex)

    fun drawShape(shape: Shape?, color: MColor?, zIndex: Int) = debugDrawShape(shape, color, false, zIndex)

    /**
     * Draws a shape onto the screen and fills in the interior.
     *
     * @param shape a [Shape]
     * @param color the color to use
     */
    @JvmStatic
    fun fillShape(shape: Shape?, color: MColor?) = fillShape(shape, color, DebugShape.Priority.FILL.zIndex)

    fun fillShape(shape: Shape?, color: MColor?, zIndex: Int) = debugDrawShape(shape, color, true, zIndex)

    // Internal Draw methods

    private fun debugDrawShape(shape: Shape?, color: MColor?, fill: Boolean, zIndex: Int) {
        // screen coordinates only
        if (!Mayonez.started) return
        when (shape) {
            is Edge -> addShape(shape.toScreen(), color, false, DebugShape.Priority.LINE)
            is Polygon -> addShape(shape.toScreen(), color, fill, zIndex)
            is Circle -> addShape(shape.toScreen(), color, fill, zIndex)
            is Ellipse -> {
                if (shape.isCircle) addShape(shape.boundingCircle().toScreen(), color, fill, zIndex)
                addShape(shape.toScreen(), color, fill, zIndex)
            }
        }
    }

    private fun addShape(shape: Shape, color: MColor?, fill: Boolean, priority: DebugShape.Priority) {
        // handle null color here
        debugRenderer.addShape(DebugShape(shape, color ?: Colors.BLACK, fill, priority))
    }

    private fun addShape(shape: Shape, color: MColor?, fill: Boolean, zIndex: Int) {
        // handle null color here
        debugRenderer.addShape(DebugShape(shape, color ?: Colors.BLACK, fill, zIndex))
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