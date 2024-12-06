package mayonez.graphics.debug

import mayonez.math.*
import mayonez.math.shapes.*
import java.awt.geom.*

// Java AWT Type Aliases

/** The [java.awt.Shape] class defined by the JDK. */
private typealias JShape = java.awt.Shape

// AWT Shape Conversion Methods

/** Convert a [mayonez.math.shapes.Shape] to an equivalent [java.awt.Shape]. */
internal fun MShape.toAWTShape(): JShape? {
    return when (this) {
        is Edge -> Line2D.Float(start.x, start.y, end.x, end.y)
        is Ellipse -> this.toAWTEllipse()
        is Rectangle -> this.toAWTRectangle()
        is MPolygon -> this.toAWTPolygon()
        else -> null
    }
}

private fun Ellipse.toAWTEllipse(): JShape {
    val min = this.center() - (this.size * 0.5f)
    val ellipse = Ellipse2D.Float(min.x, min.y, size.x, size.y)
    return if (this is Circle || this.isAxisAligned) ellipse
    else ellipse.rotate(this.angle, this.center())
}

private fun Rectangle.toAWTRectangle(): JShape {
    val min = this.min()
    val rect = Rectangle2D.Float(min.x, min.y, this.width, this.height)
    return if (this.isAxisAligned) rect
    else rect.rotate(this.angle, this.center())
}

private fun Polygon.toAWTPolygon(): JShape {
    // Add first point
    var path = Path2D.Float(Path2D.WIND_NON_ZERO, this.numVertices)
    val point1 = this.vertices[0]
    path.moveTo(point1.x, point1.y)
    // Add edges
    for (i in 1 until this.numVertices) {
        val point = this.vertices[i]
        path.lineTo(point.x, point.y)
    }
    // Add last edge
    path.lineTo(point1.x, point1.y)
    return path
}

/**
 * Rotate a [java.awt.Shape] by a given angle around its center point.
 *
 * @param angle the rotation angle*
 * @param center the shape's center
 * @return the rotated shape
 */
private fun JShape.rotate(angle: Float, center: Vec2): JShape {
    val rotXf = AffineTransform.getRotateInstance(
        Math.toRadians(angle.toDouble()),
        center.x.toDouble(), center.y.toDouble()
    )
    return rotXf.createTransformedShape(this)
}