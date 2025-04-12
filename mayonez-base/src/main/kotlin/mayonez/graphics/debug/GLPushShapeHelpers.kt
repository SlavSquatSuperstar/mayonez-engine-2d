package mayonez.graphics.debug

import mayonez.graphics.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.renderer.batch.*
import mayonez.renderer.gl.*

// Constants
private val GLOBAL_CIRCLE_VERTICES: Array<Vec2> =
    Rectangle.rectangleVerticesMinMax(Vec2(-0.5f), Vec2(0.5f))
private val LOCAL_CIRCLE_VERTICES: Array<Vec2> =
    Rectangle.rectangleVerticesMinMax(Vec2(-1f), Vec2(1f))

// Push Shape Methods

internal fun RenderBatch.pushShape(shape: MShape, color: GLColor, brush: ShapeBrush) {
    when (shape) {
        is Triangle -> this.pushPolygon(shape, color)
        is Quadrangle -> this.pushPolygon(shape, color)
        is Circle -> this.pushCircle(shape, color, brush)
        is Ellipse -> this.pushEllipse(shape, color, brush)
    }
}

private fun RenderBatch.pushPolygon(poly: MPolygon, color: GLColor) {
    for (v in poly.vertices) {
        pushVec2(v)
        pushVec4(color)
    }
}

private fun RenderBatch.pushCircle(circle: Circle, color: GLColor, brush: ShapeBrush) {
    val outerWidth = circle.radius * 2f
    val innerWidth = if (brush.fill) 0f else outerWidth - brush.strokeSize * 2f
    // Temporarily set stroke to double until fixed
    val relativeInnerWidth = innerWidth / outerWidth
    for (i in 0..<ElementLayout.QUAD.vertexCount) {
        pushVec2((GLOBAL_CIRCLE_VERTICES[i] * outerWidth) + circle.center())
        pushVec2(LOCAL_CIRCLE_VERTICES[i])
        pushVec4(color)
        pushFloat(relativeInnerWidth)
    }
}

private fun RenderBatch.pushEllipse(ellipse: Ellipse, color: GLColor, brush: ShapeBrush) {
    val outerSize = ellipse.size
    val innerSize = if (brush.fill) Vec2(0f)
    else outerSize - Vec2(brush.strokeSize * 2f)
    val relativeInnerSize = innerSize / outerSize
    for (i in 0..<ElementLayout.QUAD.vertexCount) {
        pushVec2((GLOBAL_CIRCLE_VERTICES[i] * outerSize).rotate(ellipse.angle) + ellipse.center())
        pushVec2(LOCAL_CIRCLE_VERTICES[i])
        pushVec4(color)
        pushVec2(relativeInnerSize)
    }
}