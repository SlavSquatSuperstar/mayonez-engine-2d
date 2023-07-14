package mayonez.graphics.renderer

import mayonez.graphics.debug.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.util.*
import kotlin.math.*

// GL Add Shape Methods

internal fun MutableList<DebugShape>.addLine(edge: Edge, shape: DebugShape, lineStyle: LineStyle) {
    when (lineStyle) {
        LineStyle.SINGLE -> this.addLineAsSingle(shape)
        LineStyle.MULTIPLE -> this.addLineAsMultiple(edge, shape)
        LineStyle.QUADS -> this.addLineAsQuads(edge, shape)
    }
}

private fun MutableList<DebugShape>.addLineAsSingle(shape: DebugShape) {
    this.add(shape)
}

private fun MutableList<DebugShape>.addLineAsMultiple(line: Edge, shape: DebugShape) {
    val len = line.length
    val stroke = shape.strokeSize
    val stretched = line.scale(Vec2((len + stroke - 1f) / len), null)

    val norm = line.unitNormal()
    val start = (1 - stroke) * 0.5f // -(stroke - 1) / 2

    for (i in 0 until stroke.roundToInt()) {
        val lineElem = stretched.translate(norm * (start + i))
        this.addShapeAndCopyBrush(lineElem, shape)
    }
}

private fun MutableList<DebugShape>.addLineAsQuads(edge: Edge, shape: DebugShape) {
    val len = edge.length
    val stroke = shape.strokeSize

    val stretchedLen = len + stroke - 1f
    val rect = Rectangle(edge.center(), Vec2(stretchedLen, stroke), edge.toVector().angle())
    for (tri in rect.triangles) {
        // Change brush fill to "true" since using quads
        val brush = shape.copyBrushToStyle(LineStyle.QUADS)
        this.add(DebugShape(tri, brush))
    }
}

// Debug Shape Helper Methods

/**
 * Add a [mayonez.math.shapes.Shape] and copy the brush property of another
 * [mayonez.graphics.debug.DebugShape].
 */
internal fun MutableList<DebugShape>.addShapeAndCopyBrush(newShape: MShape, debugShape: DebugShape) {
    val copy = debugShape.copy(shape = newShape)
    this.add(copy)
}
