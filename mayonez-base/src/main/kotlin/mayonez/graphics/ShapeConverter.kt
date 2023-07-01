package mayonez.graphics

import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.util.*
import java.awt.geom.*
import kotlin.math.*

// AWT Shape Conversion Methods

/** Convert a [mayonez.math.shapes.Shape] to an equivalent [java.awt.Shape]. */
internal fun MShape.toAWTShape(): JShape? {
    return when (this) {
        is Edge -> Line2D.Float(start.x, start.y, end.x, end.y)
        is Ellipse -> this.toAWTEllipse()
        is Rectangle -> this.toAWTRectangle()
        is Polygon -> toAWTPolygon()
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
    val poly = JPolygon()
    this.vertices.forEach {
        poly.addPoint(it.x.roundToInt(), it.y.roundToInt())
    }
    return poly
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