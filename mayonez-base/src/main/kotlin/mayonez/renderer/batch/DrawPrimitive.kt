package mayonez.renderer.batch

import mayonez.graphics.*
import org.lwjgl.opengl.GL11
import java.nio.IntBuffer

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
            for (v in 0..<elementCount) {
                elements.put(vertexCount * index + v)
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
            for (v in 0..<elementCount) {
                elements.put(vertexCount * index + v)
            }
        }
    },

    // TODO non-sprite quad

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
            val triangleVertices = intArrayOf(0, 1, 2, 0, 2, 3)
            for (v in triangleVertices) {
                elements.put(vertexCount * index + v)
            }
        }
    };

    /** The number of components for all attributes per vertex. */
    val componentCount: Int = attributes.sumOf { it.count }

    /**
     * Adds indices to an element buffer array (EBO).
     *
     * @param elements an int buffer
     * @param index the vertex index
     */
    abstract fun addIndices(elements: IntBuffer, index: Int)

}