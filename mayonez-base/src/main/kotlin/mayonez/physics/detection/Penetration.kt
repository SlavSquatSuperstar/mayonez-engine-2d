package mayonez.physics.detection

import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.physics.resolution.*
import kotlin.math.*

/**
 * Describes how far and in which direction two colliding shapes are
 * overlapping and calculates the contact points (collision manifold)
 * between them finding and clipping intersecting features. Solving for
 * contacts is the third and final step in collision detection, and is
 * performed after obtaining the penetration. Used by [CollisionDetector].
 *
 * @author SlavSquatSuperstar
 */
class Penetration(val normal: Vec2, val depth: Float) {

    /**
     * Calculates the contact points between two shapes.
     *
     * Sources
     * - https://dyn4j.org/2011/11/contact-points-using-clipping/
     * - https://github.com/erincatto/box2d-lite/blob/master/docs/GDC2006_Catto_Erin_PhysicsTutorial.pdf
     *
     * @param shape1 the first shape
     * @param shape2 the second shape
     * @return the collision manifold, or null if no collision
     */
    fun getContacts(shape1: Shape, shape2: Shape): Manifold? {
        val collision: Manifold

        // 1. Point the normal in the correct direction
        val vecAB = shape2.center() - shape1.center()
        if (normal.dot(vecAB) < 0f) normal.set(-normal)

        // 2. Find the farthest features along normals
        val feature1 = shape1.getFurthestFeature(normal)
        val feature2 = shape2.getFurthestFeature(-normal)

        when {
            // Case 1A: One point on first shape (circle vs polygon)
            feature1 is Vec2 -> collision = createManifoldWithOnePoint(shape1, shape2, feature1)

            // Case 1B: One point on second shape (polygon vs circle)
            feature2 is Vec2 -> collision = createManifoldWithOnePoint(shape2, shape1, feature2)

            // Case 2: Two intersecting edges (polygon vs polygon)
            feature1 is Edge && feature2 is Edge -> {
                val swapShapes = shouldUseShape2Normal(feature1, feature2)
                val (refEdge, incEdge) = getEdgesFromFeatures(swapShapes, feature1, feature2)

                val colNormal = if (swapShapes) -normal else normal
                collision = createManifoldForTwoEdges(swapShapes, shape2, shape1, colNormal)

                // 3. Clip incident edge
                val clippedEdge = incEdge.clipToSegment(refEdge)

                // 4. Calculate contact points
                collision.addContactsFromEdges(refEdge, colNormal, swapShapes, shape1, shape2, clippedEdge)
            }

            else -> return null
        }
        return if (collision.numContacts() > 0) collision else null
    }

    // Penetration Helper Methods

    private fun Shape.getFurthestFeature(direction: Vec2): Any {
        return if (this is Polygon) this.getFurthestEdge(direction)
        else this.supportPoint(direction)
    }

    private fun Polygon.getFurthestEdge(direction: Vec2): Edge {
        // Find the furthest vertex and check left and right edges
        val farthest = FloatMath.maxIndex(*FloatArray(numVertices) { vertices[it].dot(direction) })
        val leftEdge = edges[farthest] // use this edge index
        val rightEdge = edges[if (farthest > 0) farthest - 1 else numVertices - 1]  // get previous edge index

        // Check which normal is more perpendicular
        return if (leftEdge.unitNormal().dot(direction) > rightEdge.unitNormal().dot(direction))
            leftEdge else rightEdge
    }

    // Manifold Helper Methods (1 Point)

    private fun createManifoldWithOnePoint(
        shape1: Shape, shape2: Shape, point: Vec2
    ): Manifold {
        val manifold = Manifold(shape1, shape2, normal, depth)
        manifold.addContact(point)
        return manifold
    }

    private fun shouldUseShape2Normal(feature1: Edge, feature2: Edge): Boolean {
        return feature1.getLengthAlongNormal(normal) > feature2.getLengthAlongNormal(normal)
    }

    private fun Edge.getLengthAlongNormal(normal: Vec2): Float {
        return abs(this.toVector().dot(normal))
    }

    // Manifold Helper Methods (2 Points)

    private fun getEdgesFromFeatures(
        flip: Boolean, feature1: Edge, feature2: Edge
    ): Pair<Edge, Edge> {
        return if (flip) Pair(feature2, feature1) else Pair(feature1, feature2)
    }

    private fun createManifoldForTwoEdges(
        flip: Boolean, shape2: Shape, shape1: Shape, colNormal: Vec2
    ): Manifold {
        return if (flip) Manifold(shape2, shape1, -colNormal, depth)
        else Manifold(shape1, shape2, colNormal, depth)
    }

    private fun Manifold.addContactsFromEdges(
        refEdge: Edge, colNormal: Vec2, swapShapes: Boolean,
        shape1: Shape, shape2: Shape, clippedEdge: Edge,
    ) {
        val incEdgeLength = refEdge.start.dot(colNormal)
        val incShape = if (swapShapes) shape2 else shape1
        for (pt in arrayOf(clippedEdge.start, clippedEdge.end)) {
            if (pt.isClippedInsideShape(colNormal, incEdgeLength, incShape)) {
                this.addContact(pt)
            }
        }
    }

    private fun Vec2.isClippedInsideShape(
        colNormal: Vec2, incEdgeLength: Float, incShape: Shape
    ): Boolean {
        return (dot(colNormal) <= incEdgeLength) && (this in incShape)
    }

    override fun toString(): String = "Penetration $normal, $depth"

}