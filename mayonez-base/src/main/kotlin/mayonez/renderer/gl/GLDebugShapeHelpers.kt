package mayonez.renderer.gl

import mayonez.graphics.debug.*
import mayonez.math.*
import mayonez.math.shapes.*

/**
 * Break down this shape into its simplest components (lines or triangles).
 *
 * @return an array of primitive shapes
 */
internal fun DebugShape.getParts(): Array<out MShape> {
    return when (val shape = this.shape) {
        is Edge -> arrayOf(shape) // Add lines directly
        is MPolygon -> shape.getParts(this.fill) // Break polys into lines or triangles
        is Circle -> arrayOf(shape) // Add circles directly
        is Ellipse -> arrayOf(shape) // Add ellipses directly
        else -> emptyArray()
    }
}

// Poly > Lines/Tris
private fun MPolygon.getParts(fill: Boolean): Array<out MShape> {
    return if (fill) this.triangles else this.edges
}

// Line > Tris
internal fun Edge.getDrawParts(brush: ShapeBrush, zoom: Float): List<DebugShape> {
    val stroke = brush.strokeSize / zoom // Apparent width in pixels
    // TODO looks bad for large strokes
    val rect = Rectangle(this.center(), Vec2(this.length, stroke), this.toVector().angle())
    return rect.triangles.map { tri -> tri.getDrawShape(brush) }
}

internal fun Triangle.getDrawShape(brush: ShapeBrush): DebugShape {
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
