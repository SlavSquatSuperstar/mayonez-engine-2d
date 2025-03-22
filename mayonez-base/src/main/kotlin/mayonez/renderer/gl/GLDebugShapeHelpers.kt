package mayonez.renderer.gl

import mayonez.graphics.debug.*
import mayonez.math.*
import mayonez.math.shapes.*

// Shape to Parts Methods

/**
 * Break down this shape into its simplest components for rendering.
 *
 * @return an array of primitive shapes
 */
internal fun DebugShape.getDrawParts(zoom: Float): List<DebugShape> {
    var drawParts = ArrayList<DebugShape>()
    this.shape.getParts(this.fill).forEach { shapePart ->
        when (shapePart) {
            is Edge -> drawParts.addAll(shapePart.getDrawParts(this.brush, zoom))
            is Triangle -> drawParts.add(shapePart.getDrawShape(this.brush))
            is Circle -> drawParts.add(shapePart.getDrawShape(this.brush, zoom))
            is Ellipse -> drawParts.add(shapePart.getDrawShape(this.brush, zoom))
        }
    }
    return drawParts
}

private fun MShape.getParts(fill: Boolean): Array<out MShape> {
    return when (this) {
        is Edge -> arrayOf(this) // Add lines directly
        // Break polys into lines or triangles
        is MPolygon -> if (fill) this.triangles else this.edges
        is Circle -> arrayOf(this) // Add circles directly
        is Ellipse -> arrayOf(this) // Add ellipses directly
        else -> emptyArray()
    }
}

// Shape to Debug Shape Methods

private fun Edge.getDrawParts(brush: ShapeBrush, zoom: Float): List<DebugShape> {
    val stroke = brush.strokeSize / zoom // Apparent width in pixels
    // TODO looks bad for large strokes
    val rect = Rectangle(this.center(), Vec2(this.length, stroke), this.toVector().angle())
    return rect.triangles.map { tri -> tri.getDrawShape(brush) }
}

private fun Triangle.getDrawShape(brush: ShapeBrush): DebugShape {
    return DebugShape(this, brush.copy(fill = true))
}

internal fun Circle.getDrawShape(brush: ShapeBrush, zoom: Float): DebugShape {
    val stroke = brush.strokeSize / zoom // Apparent width in pixels
    val totalRadius = if (brush.fill) this.radius
    else this.radius + stroke * 0.5f // Increase diameter by stroke
    return DebugShape(Circle(this.center(), totalRadius), brush.copy(strokeSize = stroke))
}

internal fun Ellipse.getDrawShape(brush: ShapeBrush, zoom: Float): DebugShape {
    val stroke = brush.strokeSize / zoom // Apparent width in pixels
    val totalSize = if (brush.fill) this.size
    else this.size + Vec2(stroke) // Increase dimensions by stroke
    return DebugShape(
        Ellipse(this.center(), totalSize, this.angle),
        brush.copy(strokeSize = stroke)
    )
}
