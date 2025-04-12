package mayonez.renderer.gl

import mayonez.graphics.debug.*
import mayonez.math.*
import mayonez.math.shapes.*

// Helper Classes

/**
 * A four-sided polygon able to be divided into two triangles.
 */
internal class Quadrangle(v1: Vec2, v2: Vec2, v3: Vec2, v4: Vec2) :
    MPolygon(false, *orderedVertices(arrayOf(v1, v2, v3, v4)))

// Shape to Parts Methods

/**
 * Break down this shape into its simplest components for rendering.
 *
 * @return an array of primitive shapes
 */
internal fun DebugShape.getDrawParts(zoom: Float): List<DebugShape> {
    return when (val shape = this.shape) {
        // Draw edges as triangles
        is Edge -> listOf(shape.getDrawShape(this.brush, zoom))
        // Draw polygon edges/triangles as triangles
        is MPolygon -> shape.getDrawShapes(this.brush, zoom)
        // Draw circles as quads
        is Circle -> listOf(shape.getDrawShape(this.brush, zoom))
        // Draw ellipses as quads
        is Ellipse -> listOf(shape.getDrawShape(this.brush, zoom))
        // Don't draw other shapes
        else -> emptyList()
    }
}

private fun MPolygon.getDrawShape(brush: ShapeBrush): DebugShape {
    return DebugShape(this, brush.copy(fill = true))
}

// Edge to Parts Methods

private fun Edge.getDrawShape(brush: ShapeBrush, zoom: Float): DebugShape {
    return this.getQuad(brush, zoom).getDrawShape(brush)
}

internal fun Edge.getQuad(brush: ShapeBrush, zoom: Float): MPolygon {
    val stroke = brush.strokeSize / zoom // Apparent width in pixels
    val rect = Rectangle(this.center(), Vec2(this.length + stroke, stroke), this.toVector().angle())
    val vertices = rect.vertices
    return Quadrangle(vertices[0], vertices[1], vertices[2], vertices[3])
}

// Polygon to Parts Methods

private fun MPolygon.getDrawShapes(brush: ShapeBrush, zoom: Float): List<DebugShape> {
    return if (brush.fill) this.triangles.map { it.getDrawShape(brush) }
    else this.getEdgeQuads(brush, zoom).map { it.getDrawShape(brush) }
}

internal fun MPolygon.getEdgeQuads(brush: ShapeBrush, zoom: Float): List<MPolygon> {
    // Get edge directions
    val edges = this.edges
    val normals = edges.map { it.unitNormalRight() } // Outward facing
    val dirs = edges.map { it.toVector().unit() } // CCW facing
    val halfStroke = 0.5f * brush.strokeSize / zoom // Apparent width in pixels

    // Get quads
    // Cut the inner corners (miter joint)
    val quads = ArrayList<Quadrangle>()
    for (i in edges.indices) {
        val outerStart = edges[i].start + (-dirs[i] + normals[i]) * halfStroke // Vertex 0
        val outerEnd = edges[i].end + (dirs[i] + normals[i]) * halfStroke // Vertex 1
        val innerEnd = edges[i].end + (-dirs[i] - normals[i]) * halfStroke // Vertex 2
        val innerStart = edges[i].start + (dirs[i] - normals[i]) * halfStroke // Vertex 3
        quads.add(Quadrangle(outerStart, outerEnd, innerEnd, innerStart))
    }
    return quads
}

// Circle/Ellipse to Parts Methods

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
