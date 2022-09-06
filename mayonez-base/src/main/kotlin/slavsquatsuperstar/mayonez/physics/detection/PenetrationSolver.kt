package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.graphics.DebugDraw
import slavsquatsuperstar.mayonez.physics.collision.CollisionInfo
import slavsquatsuperstar.mayonez.physics.shapes.Edge
import slavsquatsuperstar.mayonez.physics.shapes.Polygon
import slavsquatsuperstar.mayonez.physics.shapes.Shape
import slavsquatsuperstar.util.Colors
import kotlin.math.abs

/**
 * Takes a penetration normal and distance between two shapes and calculates contact points. Used by [EPASolver] and
 * [SATDetector].
 *
 * @author SlavSquatSuperstar
 */
class PenetrationSolver(private val penetration: Penetration) {

    /*
     * Sources
     * - https://dyn4j.org/2011/11/contact-points-using-clipping/
     * - https://github.com/erincatto/box2d-lite/tree/master/docs
     */
    fun solve(): CollisionInfo? {
        val collision: CollisionInfo
        val shape1 = penetration.shape1
        val shape2 = penetration.shape2
        val depth = penetration.depth

        // 1. Get the right normal direction
        val vecAB = shape2.center() - shape1.center()
        val normal = if (penetration.normal.dot(vecAB) > 0)
            penetration.normal else -penetration.normal // should point from shape1 to shape2

        // 2 Find the furthest features along normals
        val feature1 = shape1.getFurthestFeature(normal)
        val feature2 = shape2.getFurthestFeature(-normal)

        if (feature1 is Vec2) { // Case 1A: One point on first shape
            collision = CollisionInfo(shape1, shape2, normal, depth)
            collision.addContact(feature1)

            DebugDraw.drawVector(feature1, normal, Colors.RED)
            DebugDraw.drawPoint(feature1, Colors.RED)
        } else if (feature2 is Vec2) { // Case 1B: One point on second shape
            collision = CollisionInfo(shape1, shape2, normal, depth)
            collision.addContact(feature2)

            DebugDraw.drawVector(feature2, -normal, Colors.RED)
            DebugDraw.drawPoint(feature2, Colors.RED)
        } else if (feature1 is Edge && feature2 is Edge) { // Case 2: Two edges
            val flip = abs(feature1.toVector().dot(normal)) > abs(feature2.toVector().dot(normal))
            val refFeature = if (flip) feature2 else feature1
            val incFeature = if (flip) feature1 else feature2
            if (flip) normal.set(-normal)

            collision = if (flip) CollisionInfo(shape2, shape1, -normal, depth)
            else CollisionInfo(shape1, shape2, normal, depth)

            // 3. Clip incident edge
            val clippedEdge = incFeature.clipToSegment(refFeature) // clip incident edge

            DebugDraw.drawVector(refFeature.center(), normal, Colors.RED)
//            DebugDraw.drawShape(refFeature, Colors.RED)
            DebugDraw.drawVector(clippedEdge.center(), -normal, Colors.GREEN)
//            DebugDraw.drawShape(clippedEdge, Colors.GREEN)

            // 4. Calculate contact points
            val incEdgeLength = refFeature.start.dot(normal)
            for (pt in arrayOf(clippedEdge.start, clippedEdge.end))
                if (pt.dot(normal) <= incEdgeLength) collision.addContact(pt)
        } else {
            return null
        }

//        for (i in 0 until collision.numContacts()) DebugDraw.drawPoint(collision.getContact(i), Colors.RED)
//        return if (collision.numContacts() > 0) collision else null
        return null
    }

    companion object {
        private fun Shape.getFurthestFeature(direction: Vec2): Any {
            return if (this is Polygon) this.getFurthestEdge(direction)
            else this.supportPoint(direction)
        }

        private fun Polygon.getFurthestEdge(direction: Vec2): Edge {
            DebugDraw.drawPoint(supportPoint(direction), Colors.BLUE)

//            val dotProds = FloatArray(normals.size) { normals[it].dot(direction) }
//            return this.edges[MathUtils.maxIndex(*dotProds)]

            // find furthest vertex, then left and right edges
            val iLeft = MathUtils.maxIndex(*FloatArray(numVertices) { vertices[it].dot(direction) })
            val iRight =
                if (iLeft > 0) iLeft - 1 else numVertices - 1 // equivalent: (index - 1 + numVertices) % numVertices]
            val nLeft = normals[iLeft]
            val nRight = normals[iRight]
            DebugDraw.drawShape(edges[iLeft], Colors.LIGHT_BLUE)
            DebugDraw.drawShape(edges[iRight], Colors.DARK_BLUE)
            println("L=${nLeft.dot(direction)}, R=${nRight.dot(direction)}")

            return if (nLeft.dot(direction) > nRight.dot(direction)) edges[iLeft]
            else edges[iRight]
        }

    }

}