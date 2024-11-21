package mayonez.renderer.gl

import mayonez.graphics.debug.*
import mayonez.math.*
import mayonez.math.shapes.*
import kotlin.math.*

// GL Shape Conversion Methods

/**
 * Break down this shape into its simplest components (lines or triangles).
 *
 * @return an array of primitive shapes
 */
internal fun DebugShape.getParts(zoom: Float): Array<out MShape> {
    return when (val shape = this.shape) {
        is Edge -> arrayOf(shape) // add line directly
        is MPolygon -> shape.getParts(this.fill) // else break into lines or triangles
        is Ellipse -> shape.toPolygon(shape.getNumEdges(zoom)).getParts(this.fill)
        else -> emptyArray()
    }
}

private fun MPolygon.getParts(fill: Boolean): Array<out MShape> {
    return if (fill) this.triangles else this.edges
}

private fun Ellipse.getNumEdges(zoom: Float): Int {
    // Apparent circumference in pixels
    return (this.circumference() * 0.1f * zoom).roundToInt().coerceAtLeast(8)
}

// GL Lines Shape Methods

internal fun Edge.getLines(shape: DebugShape, zoom: Float): List<DebugShape> {
    val list = ArrayList<DebugShape>()
    val len = this.length
    val stroke = shape.strokeSize / zoom // Apparent width in pixels

    val stretchedLen = len + stroke - 1f
    val rect = Rectangle(this.center(), Vec2(stretchedLen, stroke), this.toVector().angle())
    for (tri in rect.triangles) {
        // Change brush fill to "true" since using quads
        list.add(DebugShape(tri, shape.copyBrush(true)))
    }
    return list
}
