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
internal fun DebugShape.getParts(): Array<out MShape> {
    return when (val shape = this.shape) {
        is Edge -> arrayOf(shape) // add line directly
        is MPolygon -> shape.getParts(this.fill) // else break into lines or triangles
        is Ellipse -> shape.toPolygon().getParts(this.fill)
        else -> emptyArray()
    }
}

private fun MPolygon.getParts(fill: Boolean): Array<out MShape> {
    return if (fill) this.triangles else this.edges
}

// GL Lines Shape Methods

internal fun getLines(edge: Edge, shape: DebugShape, lineStyle: LineStyle): List<DebugShape> {
    return when (lineStyle) {
        LineStyle.SINGLE -> listOf(shape)
        LineStyle.MULTIPLE -> getLineAsMultiple(edge, shape)
        LineStyle.QUADS -> getLineAsQuads(edge, shape)
    }
}

private fun getLineAsMultiple(line: Edge, shape: DebugShape): List<DebugShape> {
    val list = ArrayList<DebugShape>()
    val len = line.length
    val stroke = shape.strokeSize
    val stretched = line.scale(Vec2((len + stroke - 1f) / len), null)

    val norm = line.unitNormalLeft()
    val start = (1 - stroke) * 0.5f // -(stroke - 1) / 2

    for (i in 0..<stroke.roundToInt()) {
        val lineElem = stretched.translate(norm * (start + i))
        list.add(shape.copy(shape = lineElem))
    }
    return list
}

private fun getLineAsQuads(edge: Edge, shape: DebugShape): List<DebugShape> {
    val list = ArrayList<DebugShape>()
    val len = edge.length
    val stroke = shape.strokeSize

    val stretchedLen = len + stroke - 1f
    val rect = Rectangle(edge.center(), Vec2(stretchedLen, stroke), edge.toVector().angle())
    for (tri in rect.triangles) {
        // Change brush fill to "true" since using quads
        val brush = shape.copyBrushToStyle(LineStyle.QUADS)
        list.add(DebugShape(tri, brush))
    }
    return list
}

