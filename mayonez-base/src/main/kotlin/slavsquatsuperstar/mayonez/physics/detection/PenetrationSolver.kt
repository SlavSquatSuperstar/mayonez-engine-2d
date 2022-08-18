package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics.collision.CollisionInfo
import slavsquatsuperstar.mayonez.physics.shapes.Edge
import slavsquatsuperstar.mayonez.physics.shapes.Polygon
import slavsquatsuperstar.mayonez.physics.shapes.Shape
import kotlin.math.abs

class PenetrationSolver(
    private val shape1: Shape, private val shape2: Shape,
    private val axis: Vec2, private val overlap: Float
) {

    /*
     * Sources
     * - https://dyn4j.org/2011/11/contact-points-using-clipping/
     * - https://github.com/erincatto/box2d-lite/tree/master/docs
     */
    fun solve(): CollisionInfo? {
        val collision: CollisionInfo

        // 1. Get the right normal direction
        val vecAB = shape2.center() - shape1.center()
        val normal = if (axis.dot(vecAB) > 0) axis else -axis // should point from shape1 to shape2

        // 2 Find the furthest features along normals
        val feature1 = shape1.getFurthestFeature(normal)
        val feature2 = shape2.getFurthestFeature(-normal)

        if (feature1 is Vec2) { // Case 1A: One point on first shape
            collision = CollisionInfo(shape1, shape2, normal, overlap)
            collision.addContact(feature1)
        } else if (feature2 is Vec2) { // Case 1B: One point on second shape
            collision = CollisionInfo(shape1, shape2, normal, overlap)
            collision.addContact(feature2)
        } else if (feature1 is Edge && feature2 is Edge) { // Case 2: Two edges
            val flip = abs(feature1.toVector().dot(normal)) > abs(feature2.toVector().dot(normal))
            val refFeature = if (flip) feature2 else feature1
            val incFeature = if (flip) feature1 else feature2
            if (flip) normal.set(-normal)
            collision = if (flip) CollisionInfo(shape2, shape1, -normal, overlap)
            else CollisionInfo(shape1, shape2, normal, overlap)

            // 3. Clip incident edge
            val clippedEdge = incFeature.clipToSegment(refFeature) // clip incident edge

//            DebugDraw.drawVector(refFeature.center(), normal, Colors.RED)
//            DebugDraw.drawShape(refFeature, Colors.RED)
//            DebugDraw.drawVector(clippedEdge.center(), -normal, Colors.GREEN)
//            DebugDraw.drawShape(clippedEdge, Colors.GREEN)

            // 4. Calculate contact points
            val incEdgeLength = refFeature.start.dot(normal)
            for (pt in arrayOf(clippedEdge.start, clippedEdge.end))
                if (pt.dot(normal) <= incEdgeLength) collision.addContact(pt)
        } else{
            return null
        }

//        for (i in 0 until collision.numContacts()) DebugDraw.drawPoint(collision.getContact(i), Colors.BLACK)
        return if (collision.numContacts() > 0) collision else null
//        return null
    }

    companion object {
        private fun Shape.getFurthestFeature(direction: Vec2): Any {
            return if (this is Polygon) this.getFurthestEdge(direction)
            else this.supportPoint(direction)
        }

        private fun Polygon.getFurthestEdge(direction: Vec2): Edge {
            val dotProds = FloatArray(this.normals.size) { this.normals[it].dot(direction) }
            return this.edges[MathUtils.maxIndex(*dotProds)]
        }

    }

}