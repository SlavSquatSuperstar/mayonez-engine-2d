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
    return when (val shape = this.shape) {
        // Draw edges as triangles
        is Edge -> shape.getDrawShapes(this.brush, zoom)
        // Draw polygon edges/triangles as triangles
        is Polygon -> shape.getDrawShapes(this.brush, zoom)
        // Draw circles as quads
        is Circle -> listOf(shape.getDrawShape(this.brush, zoom))
        // Draw ellipses as quads
        is Ellipse -> listOf(shape.getDrawShape(this.brush, zoom))
        // Don't draw other shapes
        else -> emptyList()
    }
}

private fun Triangle.getDrawShape(brush: ShapeBrush): DebugShape {
    return DebugShape(this, brush.copy(fill = true))
}

// Edge to Parts Methods

private fun Edge.getDrawShapes(brush: ShapeBrush, zoom: Float): List<DebugShape> {
    return this.getTriangles(brush, zoom).map { it.getDrawShape(brush) }
}

// TODO draw as quads
internal fun Edge.getTriangles(brush: ShapeBrush, zoom: Float): List<Triangle> {
    val stroke = brush.strokeSize / zoom // Apparent width in pixels
    // TODO looks bad for large strokes
    val rect = Rectangle(this.center(), Vec2(this.length + stroke, stroke), this.toVector().angle())
    return rect.triangles.toList()
}

// Polygon to Parts Methods

private fun MPolygon.getDrawShapes(brush: ShapeBrush, zoom: Float): List<DebugShape> {
    return if (brush.fill) this.triangles.map { it.getDrawShape(brush) }
    else this.getEdgeTriangles(brush, zoom).map { it.getDrawShape(brush) }
}

// TODO draw as quads
internal fun MPolygon.getEdgeTriangles(brush: ShapeBrush, zoom: Float): List<Triangle> {
    // Get edge directions
    val edges = this.edges
    val normals = edges.map { it.unitNormalRight() } // Outward facing
    val dirs = edges.map { it.toVector().unit() } // CCW facing
    val halfStroke = 0.5f * brush.strokeSize / zoom // Apparent width in pixels

    // Get quad triangles
    // Cut the inner corners (miter joint)
    val triangles = ArrayList<Triangle>()
    for (i in edges.indices) {
        val outerStart = edges[i].start + (-dirs[i] + normals[i]) * halfStroke // Vertex 0
        val outerEnd = edges[i].end + (dirs[i] + normals[i]) * halfStroke // Vertex 1
        val innerEnd = edges[i].end + (-dirs[i] - normals[i]) * halfStroke // Vertex 2
        val innerStart = edges[i].start + (dirs[i] - normals[i]) * halfStroke // Vertex 3
        triangles.add(Triangle(outerStart, outerEnd, innerEnd))
        triangles.add(Triangle(outerStart, innerEnd, innerStart))
    }
    return triangles
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
