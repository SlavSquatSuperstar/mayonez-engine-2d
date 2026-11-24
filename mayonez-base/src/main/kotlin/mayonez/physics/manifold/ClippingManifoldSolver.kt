package mayonez.physics.manifold

import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.math.shapes.Polygon.Companion.prev
import mayonez.physics.detection.*
import kotlin.math.*

/**
 * Calculates the contact points between two intersecting shapes finding
 * and clipping intersecting features.
 *
 * Sources:
 * - [dyn4j](https://dyn4j.org/2011/11/contact-points-using-clipping/)
 * - [box2d](https://github.com/erincatto/box2d-lite/blob/master/docs/GDC2006_Catto_Erin_PhysicsTutorial.pdf)
 *
 * @author SlavSquatSuperstar
 */
internal class ClippingManifoldSolver : ContactSolver {

    override fun getContacts(shape1: Shape, shape2: Shape, penetration: Penetration?): Manifold? {
        // 1. Point the normal in the correct direction
        val normal = penetration?.normal ?: return null
        val vecAB = shape2.center() - shape1.center()
        if (normal.dot(vecAB) < 0f) normal.set(-normal)

        // 2. Find the farthest features along normals
        val feature1 = shape1.getFarthestFeature(normal)
        val feature2 = shape2.getFarthestFeature(-normal)

        // 3. Calculate contact points
        val collision = when {
            // Case 1A: One point on first shape (circle vs polygon)
            feature1 is Vec2 ->
                createManifoldWithOnePoint(shape1, shape2, feature1, penetration)

            // Case 1B: One point on second shape (polygon vs circle)
            feature2 is Vec2 ->
                createManifoldWithOnePoint(shape2, shape1, feature2, penetration)

            // Case 2: Two intersecting edges (polygon vs polygon)
            feature1 is Edge && feature2 is Edge ->
                createManifoldWithTwoPoints(shape1, shape2, feature1, feature2, penetration)

            else -> return null
        }
        return if (collision.numContacts() > 0) collision else null
    }

}

// Feature Helper Methods

private fun Shape.getFarthestFeature(direction: Vec2): Any {
    return when (this) {
        is Polygon -> this.getFarthestEdge(direction)
        is Edge -> this
        else -> this.supportPoint(direction)
    }
}

private fun Polygon.getFarthestEdge(direction: Vec2): Edge {
    /*
     * Find the farthest (max) vertex from the origin by comparing the dot
     * products. Take the max vertex's left and right edges and select the edge
     * that is more perpendicular to the normal.
     */
    val vertexDistances = vertices.map { it.dot(direction) }.toFloatArray()
    val farthest = MathUtils.maxIndex(*vertexDistances)
    val leftEdge = edges[farthest] // (v, v_next) (should flip)
    val rightEdge = edges[farthest.prev(numVertices)] // (v_prev, v)

    return if (
        leftEdge.unitNormalRight().dot(direction)
        >= rightEdge.unitNormalRight().dot(direction)
    ) leftEdge else rightEdge
}

// Manifold Helper Methods

private fun createManifoldWithOnePoint(
    shape1: Shape, shape2: Shape, point: Vec2, penetration: Penetration
): Manifold {
    val (normal, depth) = penetration
    val manifold = Manifold(shape1, shape2, normal, depth)
    manifold.addContact(point)
    return manifold
}

private fun createManifoldWithTwoPoints(
    shape1: Shape, shape2: Shape,
    edge1: Edge, edge2: Edge,
    penetration: Penetration
): Manifold {
    val (normal, depth) = penetration

    // Determine incident and reference shapes/features
    val swapShapes = edge1.getLengthAlongNormal(normal) > edge2.getLengthAlongNormal(normal)
    val (refEdge, incEdge) = getEdgesFromFeatures(swapShapes, edge1, edge2)
    val (refShape, incShape) = getShapesFromFeatures(swapShapes, shape1, shape2)
    val manifold = Manifold(refShape, incShape, normal, depth)

    // Clip incident edge
    val clippedEdge = incEdge.clipToSegment(refEdge)

    // Add contacts from edges
    val colNormal = if (swapShapes) -normal else normal
    val incEdgeLength = refEdge.start.dot(colNormal)
    for (pt in arrayOf(clippedEdge.start, clippedEdge.end)) {
        // Is point clipping inside shape
        if ((pt.dot(colNormal) <= incEdgeLength) && (pt in incShape)) {
            manifold.addContact(pt)
        }
    }
    return manifold
}

private fun getEdgesFromFeatures(
    swapShapes: Boolean, feature1: Edge, feature2: Edge
): Pair<Edge, Edge> {
    return if (swapShapes) Pair(feature2, feature1) else Pair(feature1, feature2)
}

private fun getShapesFromFeatures(
    swapShapes: Boolean, shape1: Shape, shape2: Shape
): Pair<Shape, Shape> {
    return if (swapShapes) Pair(shape2, shape1) else Pair(shape1, shape2)
}

private fun Edge.getLengthAlongNormal(normal: Vec2): Float {
    return abs(this.toVector().dot(normal))
}
