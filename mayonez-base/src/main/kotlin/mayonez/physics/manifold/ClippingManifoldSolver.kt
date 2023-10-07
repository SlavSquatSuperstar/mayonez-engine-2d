package mayonez.physics.manifold

import mayonez.math.*
import mayonez.math.shapes.*
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
        val feature1 = shape1.getFurthestFeature(normal)
        val feature2 = shape2.getFurthestFeature(-normal)

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

private fun Shape.getFurthestFeature(direction: Vec2): Any {
    return if (this is Polygon) this.getFurthestEdge(direction)
    else this.supportPoint(direction)
}

private fun Polygon.getFurthestEdge(direction: Vec2): Edge {
    // Find the furthest vertex and check left and right edges
    val dots = vertices.map { it.dot(direction) }.toFloatArray()
    val farthest = FloatMath.maxIndex(*dots)
    val leftEdge = edges[farthest] // use this edge index
    val rightEdge = edges[if (farthest > 0) farthest - 1 else numVertices - 1]  // get previous edge index

    // Check which normal is more perpendicular
    return if (
        leftEdge.unitNormalRight().dot(direction) > rightEdge.unitNormalRight().dot(direction)
    ) leftEdge else rightEdge
}

// Manifold Helper Methods (1 Point)

private fun createManifoldWithOnePoint(
    shape1: Shape, shape2: Shape, point: Vec2, penetration: Penetration
): Manifold {
    val (normal, depth) = penetration
    val manifold = Manifold(shape1, shape2, normal, depth)
    manifold.addContact(point)
    return manifold
}

private fun shouldUseShape2Normal(feature1: Edge, feature2: Edge, normal: Vec2): Boolean {
    return feature1.getLengthAlongNormal(normal) > feature2.getLengthAlongNormal(normal)
}

private fun Edge.getLengthAlongNormal(normal: Vec2): Float {
    return abs(this.toVector().dot(normal))
}

// Manifold Helper Methods (2 Points)

private fun createManifoldWithTwoPoints(
    shape1: Shape, shape2: Shape,
    edge1: Edge, edge2: Edge,
    penetration: Penetration
): Manifold {
    val (normal, depth) = penetration

    val swapShapes = shouldUseShape2Normal(edge1, edge2, normal)
    val (refEdge, incEdge) = getEdgesFromFeatures(swapShapes, edge1, edge2)

    val colNormal = if (swapShapes) -normal else normal
    val manifold = createManifoldForTwoEdges(shape1, shape2, swapShapes, colNormal, depth)

    // Clip incident edge
    val clippedEdge = incEdge.clipToSegment(refEdge)
    val incShape = if (swapShapes) shape2 else shape1
    manifold.addContactsFromEdges(incShape, refEdge, clippedEdge, colNormal)
    return manifold
}

private fun getEdgesFromFeatures(
    swapShapes: Boolean, feature1: Edge, feature2: Edge
): Pair<Edge, Edge> {
    return if (swapShapes) Pair(feature2, feature1) else Pair(feature1, feature2)
}

private fun createManifoldForTwoEdges(
    shape1: Shape, shape2: Shape, swapShapes: Boolean,
    colNormal: Vec2, depth: Float
): Manifold {
    return if (swapShapes) Manifold(shape2, shape1, -colNormal, depth)
    else Manifold(shape1, shape2, colNormal, depth)
}

private fun Manifold.addContactsFromEdges(
    incShape: Shape, refEdge: Edge, clippedEdge: Edge, colNormal: Vec2
) {
    val incEdgeLength = refEdge.start.dot(colNormal)
    for (pt in arrayOf(clippedEdge.start, clippedEdge.end)) {
        if (pt.isClippedInsideShape(incShape, colNormal, incEdgeLength)) {
            this.addContact(pt)
        }
    }
}

private fun Vec2.isClippedInsideShape(
    incShape: Shape, colNormal: Vec2, incEdgeLength: Float
): Boolean {
    return (dot(colNormal) <= incEdgeLength) && (this in incShape)
}