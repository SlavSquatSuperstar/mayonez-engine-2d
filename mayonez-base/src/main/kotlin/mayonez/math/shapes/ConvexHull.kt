package mayonez.math.shapes

import mayonez.math.*

/**
 * Creates a convex hull, ordered counterclockwise, from an array of
 * vertices using the Jarvis March (gift wrap) algorithm. The convex
 * hull is the smallest polygon that contains all the vertices.
 *
 * Sources:
 * - [GeeksForGeeks](https://www.geeksforgeeks.org/convex-hull-using-jarvis-algorithm-or-wrapping/)
 * - [AlgorithmTutor](https://algorithmtutor.com/Computational-Geometry/Convex-Hull-Algorithms-Jarvis-s-March/)
 */
fun orderedConvexHull(vertices: Array<Vec2>): Array<Vec2> {
    val size = vertices.size
    if (size < MIN_VERTICES_COUNT) return vertices.copyOf()

    val hull = ArrayList<Vec2>(size)

    val leftMost = vertices.getMinCoordsPointIdx()
    hull.add(vertices[leftMost])

    hull.addConvexHullPoints(leftMost, vertices)
    return hull.toTypedArray()
}

private fun ArrayList<Vec2>.addConvexHullPoints(
    vFirst: Int, vertices: Array<Vec2>
) {
    val numVertices = vertices.size
    var vStart = vFirst
    while (this.size <= numVertices) {
        var vCurr = (vStart + 1) % numVertices // current most clockwise point (next point in hull)
        for (i in vertices.indices) { // find the most clockwise point
            if (i == vStart || i == vCurr) continue
            // find the most clockwise point from current point
            vCurr = vertices.findNextClockwisePoint(vStart, vCurr, i)
        }
        vStart = vCurr // update start point
        if (vStart == vFirst) break // loop until reaching leftmost point
        else this.add(vertices[vCurr]) // add most clockwise point to hull
    }
}

private fun Array<Vec2>.findNextClockwisePoint(
    vStart: Int, vCurr: Int, vNext: Int
): Int {
    val ptA = this[vStart]
    val ptB = this[vCurr]
    val ptC = this[vNext]

    return when (getOrientation(ptA, ptB, ptC)) {
        Orientation.COUNTERCLOCKWISE -> vNext
        Orientation.CLOCKWISE -> vCurr
        Orientation.COLLINEAR -> {
            val nextPointIsFurther = ptA.distanceSq(ptC) > ptA.distanceSq(ptB)
            if (nextPointIsFurther) vNext
            else vCurr
        }
    }
}

private fun getOrientation(ptA: Vec2, ptB: Vec2, ptC: Vec2): Orientation {
    /*
     * To determine the orientation of three points A, B, C:
     * Find the cross product of AB and BC.
     * - If positive, then counterclockwise
     * - If negative, then clockwise
     * - If zero, then collinear
     */
    val vecAB = ptB - ptA
    val vecBC = ptC - ptB
    val orientation = vecAB.cross(vecBC)
    return when {
        orientation < 0f -> Orientation.COUNTERCLOCKWISE
        orientation > 0f -> Orientation.CLOCKWISE
        else -> Orientation.COLLINEAR
    }
}

private enum class Orientation {
    COUNTERCLOCKWISE,
    CLOCKWISE,
    COLLINEAR
}