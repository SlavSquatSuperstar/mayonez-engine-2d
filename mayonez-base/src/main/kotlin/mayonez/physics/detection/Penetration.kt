package mayonez.physics.detection

import mayonez.math.FloatMath
import mayonez.math.Vec2
import mayonez.physics.resolution.Manifold
import mayonez.physics.shapes.Edge
import mayonez.physics.shapes.Polygon
import mayonez.physics.shapes.Shape
import kotlin.math.abs

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

        // 1. Point the normla in the correct direction
        val vecAB = shape2.center() - shape1.center()
        if (normal.dot(vecAB) < 0f) normal.set(-normal)

        // 2 Find the farthest features along normals
        val feature1 = shape1.getFarthestFeature(normal)
        val feature2 = shape2.getFarthestFeature(-normal)

        if (feature1 is Vec2) { // Case 1A: One point on first shape (circle vs polygon)
            collision = Manifold(shape1, shape2, normal, depth)
            collision.addContact(feature1)
        } else if (feature2 is Vec2) { // Case 1B: One point on second shape (polygon vs circle)
            collision = Manifold(shape2, shape1, normal, depth)
            collision.addContact(feature2)
        } else if (feature1 is Edge && feature2 is Edge) { // Case 2: Two edges (polygon vs polygon)
            // swap shapes if using shape2 normal
            val flip = abs(feature1.toVector().dot(normal)) > abs(feature2.toVector().dot(normal))
            val refEdge = if (flip) feature2 else feature1
            val incEdge = if (flip) feature1 else feature2

            val colNormal = if (flip) -normal else normal
            collision = if (flip) Manifold(shape2, shape1, -colNormal, depth)
            else Manifold(shape1, shape2, colNormal, depth)

            // 3. Clip incident edge
            val clippedEdge = incEdge.clipToSegment(refEdge)

            // 4. Calculate contact points
            val incEdgeLength = refEdge.start.dot(colNormal)
            val incShape = if (flip) shape2 else shape1
            for (pt in arrayOf(clippedEdge.start, clippedEdge.end))
            // Clipping points inside shape
                if (pt.dot(colNormal) <= incEdgeLength && pt in incShape) collision.addContact(pt)
        } else {
            return null
        }
        return if (collision.numContacts() > 0) collision else null
    }

    // Penetration Helper Methods

    companion object {
        private fun Shape.getFarthestFeature(direction: Vec2): Any {
            return if (this is Polygon) this.getFarthestEdge(direction)
            else this.supportPoint(direction)
        }

        private fun Polygon.getFarthestEdge(direction: Vec2): Edge {
            // Find farthest vertex and check left and right edges
            val farthest = FloatMath.maxIndex(*FloatArray(numVertices) { vertices[it].dot(direction) })
            val leftEdge = edges[farthest] // use this edge index
            val rightEdge = edges[if (farthest > 0) farthest - 1 else numVertices - 1]  // get previous edge index

            // Check which normal is more perpendicular
            return if (leftEdge.unitNormal().dot(direction) > rightEdge.unitNormal().dot(direction))
                leftEdge else rightEdge
        }
    }

    override fun toString(): String = "Penetration $normal, $depth"

}