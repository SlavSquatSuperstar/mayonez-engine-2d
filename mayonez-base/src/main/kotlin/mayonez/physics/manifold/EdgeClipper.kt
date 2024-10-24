package mayonez.physics.manifold

import mayonez.math.*
import mayonez.math.shapes.*
import kotlin.math.*

// Edge Clipping Helper Methods

/**
 * Clips this to segment to two planes extending perpendicular from another
 * segment's endpoints. The resulting edge is this edge's projection on the
 * other.
 */
internal fun Edge.clipToSegment(segment: Edge): Edge {
    val edge = Ray(start, toVector() / length)
    val (contact1, contact2) = edge.getIntersectionsWithPlanes(segment)
    if (contact1 == null || contact2 == null) return Edge(start, end) // plane is parallel to line

    // Clip edge to new endpoints
    val distances = Interval(
        (contact1 - start).dot(edge.direction),
        (contact2 - start).dot(edge.direction)
    )
    val minDist = max(0f, distances.min)
    val maxDist = min(length, distances.max)
    return Edge(edge.getPoint(minDist), edge.getPoint(maxDist))
}

private fun Ray.getIntersectionsWithPlanes(segment: Edge): Pair<Vec2?, Vec2?> {
    val planeDir = segment.unitNormalLeft()
    val plane1 = Ray(segment.start, planeDir)
    val plane2 = Ray(segment.end, planeDir)

    val contact1 = this.getIntersection(plane1)
    val contact2 = this.getIntersection(plane2)
    return Pair(contact1, contact2)
}