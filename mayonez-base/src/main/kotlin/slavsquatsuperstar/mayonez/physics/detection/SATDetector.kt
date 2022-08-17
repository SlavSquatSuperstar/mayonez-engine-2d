package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Range
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics.collision.CollisionInfo
import slavsquatsuperstar.mayonez.physics.shapes.*

/**
 * Detects if two shapes are colliding and finds their contacts using the separating-axis theorem.
 *
 * @author SlavSquatSuperstar
 */
class SATDetector(private val shape1: Shape, private val shape2: Shape) {

    private val type: CollisionType

    init {
        type = when {
            shape1 is Circle && shape2 is Circle -> CollisionType.CIRCLE_CIRCLE
            shape1 is Circle && shape2 is Polygon -> CollisionType.CIRCLE_POLYGON
            shape1 is Polygon && shape2 is Circle -> CollisionType.POLYGON_CIRCLE
            shape1 is Polygon && shape2 is Polygon -> CollisionType.POLYGON_POLYGON
            else -> CollisionType.INVALID
        }
    }

    fun detect(): CollisionInfo? {
        return when (type) {
            CollisionType.INVALID -> null // cannot solve using SAT
            CollisionType.CIRCLE_CIRCLE -> CircleDetector.detect(shape1 as Circle, shape2 as Circle)
            CollisionType.CIRCLE_POLYGON -> detectCirclePolygon(shape1 as Circle, shape2 as Polygon)
            CollisionType.POLYGON_CIRCLE -> detectCirclePolygon(shape2 as Circle, shape1 as Polygon)?.flip()
            CollisionType.POLYGON_POLYGON -> detectPolygonPolygon(shape1 as Polygon, shape2 as Polygon)
        }
    }

    // Circle vs Polygon: 1 contact point
    private fun detectCirclePolygon(circle: Circle, polygon: Polygon): CollisionInfo? {
        val closestToCircle = polygon.nearestPoint(circle.center()) // Point from shape deepest in circle
        if (closestToCircle !in circle) return null

        val depth = circle.radius - closestToCircle.distance(circle.center())
        val result = CollisionInfo(circle, polygon, closestToCircle - circle.center(), depth)
        result.addContact(closestToCircle)
        return result
    }

    // Polygon vs Polygon: 1-2 contact points
    private fun detectPolygonPolygon(polygon1: Polygon, polygon2: Polygon): CollisionInfo? {
        // 1. Store edges and normals in structs and calculate overlaps
        val poly1 = SATPolygon(polygon1, polygon2)
        val poly2 = SATPolygon(polygon2, polygon1)

        // 2. Test for a separating axis
        if (poly1.minOverlap > 0 || poly2.minOverlap > 0) return null // positive separation = no intersection

        // 3. Set the polygon with lesser overlap as reference frame
        val reference: SATPolygon // the "stationary" shape
        val incident: SATPolygon // the "incoming" shape
        if (poly1.minOverlap >= poly2.minOverlap) {
            reference = poly1
            incident = poly2
        } else {
            incident = poly1
            reference = poly2
        }

        // 4. Calculate reference and incident edges
        val minOverlapIdx = reference.minIdx
        val overlap = reference.minOverlap
        val colNormal = reference.normals[minOverlapIdx]
        if (reference.poly === polygon2) colNormal.set(-colNormal) // flip normal if using normal of other shape
        val refEdge = reference.edges[minOverlapIdx]

        // Get most negative dot product = edge normal facing towards collision normal the most
        val dotProds = FloatArray(incident.normals.size) { colNormal.dot(incident.normals[it]) }
        val minDotIdx = MathUtils.minIndex(*dotProds)
        val incEdge = incident.edges[minDotIdx]

        // 5. Calculate contact points
        val collision = CollisionInfo(reference.poly, incident.poly, colNormal, -overlap)
        val clippedEdge = incEdge.clip(refEdge)
        val normalFace = refEdge.start.dot(colNormal)

        for (pt in arrayOf(clippedEdge.start, clippedEdge.end))
            if (pt.dot(colNormal) <= normalFace) collision.addContact(pt)
        return collision
    }

    // SAT Helpers

    companion object {

        fun Polygon.nearestPoint(position: Vec2): Vec2 {
            if (position in this) return position
            val distances = FloatArray(edges.size) { edges[it].distance(position) }
            val nearestEdge = edges[MathUtils.minIndex(*distances)]
            return nearestEdge.nearestPoint(position)
        }

        /*
         * Projected min and max
         * Positive is in axis direction
         * (-) ---> (+)
         */
        private fun Polygon.getIntervalOnAxis(axis: Vec2): Range {
            val projections = FloatArray(this.numVertices) { this.vertices[it].dot(axis) }
            return Range(MathUtils.min(*projections), MathUtils.max(*projections))
        }

        // TODO combine separation and overlap into one function (differentiate with positive and negative)
        /*
         * How much separation on the axis
         * Positive means separation, and negative means overlap
         */
        private fun Polygon.getSeparationOnAxis(polygon: Polygon, axis: Vec2): Float {
            val normalFace = this.getIntervalOnAxis(axis).max
            val nearestVertex = polygon.getIntervalOnAxis(axis).min
            return nearestVertex - normalFace
        }

        private fun Edge.clip(segment: Edge): Edge {
            val rayDir = segment.unitNormal()
            val plane1 = Ray(segment.start, rayDir)
            val plane2 = Ray(segment.end, rayDir)

            val edge = Ray(this).normalize()
            val contact1 = edge.getIntersection(plane1)
            val contact2 = edge.getIntersection(plane2)

            if (contact1 == null || contact2 == null) return Edge(start, end)
            // get distances
            val distances = Range((contact1 - start).dot(edge.direction), (contact2 - start).dot(edge.direction))
            val min = 0f.coerceAtLeast(distances.min)
            val max = length.coerceAtMost(distances.max)

            return Edge(edge.getPoint(min), edge.getPoint(max))
        }

    }

    private class SATPolygon(val poly: Polygon, other: Polygon) {
        val edges = poly.edges
        val normals = poly.normals

        // Find axis with greatest separation (least negative overlap)
        val overlaps = FloatArray(normals.size) { poly.getSeparationOnAxis(other, normals[it]) }
        val minIdx = MathUtils.maxIndex(*overlaps)
        val minOverlap = overlaps[minIdx]
    }

    // Enum

    private enum class CollisionType {
        CIRCLE_CIRCLE,
        CIRCLE_POLYGON,
        POLYGON_CIRCLE,
        POLYGON_POLYGON,
        INVALID
    }


}