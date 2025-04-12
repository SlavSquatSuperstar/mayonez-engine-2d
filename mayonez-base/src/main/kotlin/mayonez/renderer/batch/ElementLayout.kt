package mayonez.renderer.batch

import mayonez.graphics.*
import org.lwjgl.opengl.GL11

/**
 * The number and order of vertices drawn per OpenGL primitive object.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
enum class ElementLayout(
    val vertexCount: Int, // Vertices defined by primitive
    val elementCount: Int, // Vertices needed by GL to draw
    val drawMode: Int, // Primitive ID used by OpenGL
    val elementOrder: IntArray
) {

    /** A triangle with 3 vertices. */
    TRIANGLE(3, 3, GL11.GL_TRIANGLES, intArrayOf(0, 1, 2)),

    /** A quadrangle with 4 vertices, made up of 2 triangles. */
    QUAD(4, 6, GL11.GL_TRIANGLES, intArrayOf(0, 1, 2, 0, 2, 3))

}