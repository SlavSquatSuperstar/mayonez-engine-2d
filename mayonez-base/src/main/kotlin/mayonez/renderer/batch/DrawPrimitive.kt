package mayonez.renderer.batch

import mayonez.graphics.*
import org.lwjgl.opengl.GL11
import java.nio.IntBuffer

private val QUAD_ELEMENT_ORDER = intArrayOf(0, 1, 2, 0, 2, 3)

/**
 * Types of OpenGL primitive objects that can be submitted to the GPU. Each
 * object defines the attribute layout and count of vertices.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
enum class DrawPrimitive(
    val vertexCount: Int, // Vertices defined by primitive
    val elementCount: Int, // Vertices needed by GL to draw
    val primitiveType: Int, // Primitive ID used by OpenGL
    vararg val attributes: VertexAttribute // Floats inside each attribute
) {

    /** An object with 2 vertices with attributes position and color. */
    LINE(
        2, 2, GL11.GL_LINES,
        VertexAttribute.POSITION, VertexAttribute.COLOR
    ) {
        override fun addIndices(elements: IntBuffer, index: Int) {
            for (e in 0..<elementCount) {
                elements.put(vertexCount * index + e)
            }
        }
    },

    /** An object with 3 vertices with attributes position and color. */
    TRIANGLE(
        3, 3, GL11.GL_TRIANGLES,
        VertexAttribute.POSITION, VertexAttribute.COLOR
    ) {
        override fun addIndices(elements: IntBuffer, index: Int) {
            // Use counterclockwise winding
            for (e in 0..<elementCount) {
                elements.put(vertexCount * index + e)
            }
        }
    },

    // TODO no texture quad

    /**
     * An object with 4 vertices with attributes position, color, tex coords,
     * and tex ID.
     */
    SPRITE(
        4, 6, GL11.GL_TRIANGLES,
        VertexAttribute.POSITION, VertexAttribute.COLOR, VertexAttribute.TEX_COORD, VertexAttribute.TEX_ID
    ) {
        override fun addIndices(elements: IntBuffer, index: Int) {
            // Split quad into two triangles so 6 vertices
            for (e in QUAD_ELEMENT_ORDER) {
                elements.put(vertexCount * index + e)
            }
        }
    };

    /** The number of components for all attributes per vertex. */
    val totalComponents: Int = attributes.sumOf { it.components }

    /**
     * Adds indices to an element buffer array (EBO).
     *
     * @param elements an int buffer
     * @param index the vertex index
     */
    abstract fun addIndices(elements: IntBuffer, index: Int)

}