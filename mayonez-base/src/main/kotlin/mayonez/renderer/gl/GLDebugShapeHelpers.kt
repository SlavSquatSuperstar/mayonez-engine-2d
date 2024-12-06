package mayonez.renderer.gl

import mayonez.graphics.debug.*
import mayonez.math.*
import mayonez.math.shapes.*
import kotlin.math.*

/**
 * Break down this shape into its simplest components (lines or triangles).
 *
 * @return an array of primitive shapes
 */
internal fun DebugShape.getParts(zoom: Float): Array<out MShape> {
    return when (val shape = this.shape) {
        is Edge -> arrayOf(shape) // Add line directly
        is MPolygon -> shape.getParts(this.fill) // Break polys into lines or triangles
        is Ellipse -> shape.toPolygon(zoom).getParts(this.fill) // Convert circles to polys
        else -> emptyArray()
    }
}

// Poly > Lines/Tris
private fun MPolygon.getParts(fill: Boolean): Array<out MShape> {
    return if (fill) this.triangles else this.edges
}

// Circle > Poly
private fun Ellipse.toPolygon(zoom: Float): MPolygon {
    // Apparent circumference in pixels
    val numEdges = (this.circumference() * 0.1f * zoom).roundToInt().coerceAtLeast(8)
    return this.toPolygon(numEdges)
}

// Line > Tris
internal fun Edge.getDrawParts(brush: ShapeBrush, zoom: Float): List<DebugShape> {
    val stroke = brush.strokeSize / zoom // Apparent width in pixels
    val rect = Rectangle(this.center(), Vec2(this.length, stroke), this.toVector().angle())
    return rect.triangles.map { tri -> tri.getDrawShape(brush) }
}

internal fun Triangle.getDrawShape(brush: ShapeBrush): DebugShape {
    return DebugShape(this, brush.copy(fill = true))
}
