package mayonez.physics.detection

import mayonez.math.*
import mayonez.math.shapes.*
import kotlin.math.*

// SAT Helpers

/** The faces, or outward unit normal vectors of each edge in this polygon. */
internal val Polygon.normals: Array<Vec2>
    get() = this.edges.map { it.unitNormalLeft() }.toTypedArray()

/** Whether two intervals overlap each other on this axis. */
internal fun Vec2.hasOverlap(poly1: Polygon, poly2: Polygon): Boolean {
    val range1 = poly1.projectOnAxis(this)
    val range2 = poly2.projectOnAxis(this)
    return range1.overlaps(range2)
}

/**
 * Calculate the overlap between two intervals on this axis, or return null
 * if they do not overlap.
 */
internal fun Vec2.getOverlap(poly1: Polygon, poly2: Polygon): Float? {
    val range1 = poly1.projectOnAxis(this)
    val range2 = poly2.projectOnAxis(this)
    if (!range1.overlaps(range2)) return null
    return min(range2.max - range1.min, range1.max - range2.min)
}

internal fun Polygon.projectOnAxis(axis: Vec2): Interval { // positive is in axis direction
    val projections = this.vertices.map { it.dot(axis) }.toFloatArray()
    return Interval(FloatMath.min(*projections), FloatMath.max(*projections))
}

/** Whether two intervals overlap each other. */
internal fun Interval.overlaps(other: Interval): Boolean {
    return (this.min <= other.max) && (this.max >= other.min)
}