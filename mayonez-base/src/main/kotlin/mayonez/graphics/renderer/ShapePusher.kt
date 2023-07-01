package mayonez.graphics.renderer

import mayonez.graphics.*
import mayonez.graphics.sprites.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.util.*
import kotlin.math.*

// GL Shape Conversion Methods

/**
 * Break down this shape into its simplest components (lines or triangles).
 *
 * @return an array of primitive shapes
 */
internal fun DebugShape.splitIntoParts(): Array<out MShape> {
    return when (val shape = this.shape) {
        is Edge -> arrayOf(shape) // add line directly
        is Polygon -> shape.splitIntoParts(this.fill) // else break into lines or triangles
        is Ellipse -> shape.toPolygon().splitIntoParts(this.fill)
        else -> emptyArray()
    }
}

private fun MPolygon.splitIntoParts(fill: Boolean): Array<out MShape> {
    return if (fill) this.triangles else this.edges
}

// GL Add Shape Methods

internal fun MutableList<DebugShape>.addLine(edge: Edge, shape: DebugShape, lineStyle: LineStyle) {
    when (lineStyle) {
        LineStyle.SINGLE -> this.add(shape)
        LineStyle.MULTIPLE -> this.addLineAsMultiple(edge, shape)
        LineStyle.QUADS -> this.addLineAsQuads(edge, shape)
    }
}

private fun MutableList<DebugShape>.addLineAsMultiple(line: Edge, shape: DebugShape) {
    val len = line.length
    val stroke = DebugShape.STROKE_SIZE
    val stretched = line.scale(Vec2((len + stroke - 1f) / len), null)

    val norm = line.unitNormal()
    val start = (1 - stroke) * 0.5f // -(stroke - 1) / 2

    for (i in 0 until stroke.roundToInt()) {
        val lineElem = stretched.translate(norm * (start + i))
        this.addShapeCopy(lineElem, shape)
    }
}

private fun MutableList<DebugShape>.addLineAsQuads(edge: Edge, shape: DebugShape) {
    val len = edge.length
    val stroke = DebugShape.STROKE_SIZE

    val stretchedLen = len + stroke - 1f
    val rect = Rectangle(edge.center(), Vec2(stretchedLen, stroke), edge.toVector().angle())
    for (tri in rect.triangles) {
        this.add(DebugShape(tri, shape.color, true, DrawPriority.LINE))
    }
}

// Debug Shape Helper Methods

/**
 * Add a [mayonez.graphics.DebugShape] copy but updates its shape property.
 */
internal fun MutableList<DebugShape>.addShapeCopy(newShape: MShape, debugShape: DebugShape) {
    val copy = DebugShape(newShape, debugShape.color, debugShape.fill, debugShape.zIndex)
    this.add(copy)
}

internal fun ShapeSprite.toDebugShape(): DebugShape {
    return DebugShape(this.colliderShape, this.color, this.fill, this.zIndex)
}