package mayonez.graphics

import mayonez.annotations.*
import mayonez.math.*
import mayonez.util.*
import org.lwjgl.opengl.GL11
import java.nio.IntBuffer

/**
 * Types of OpenGL primitive objects that can be submitted to the GPU.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
enum class DrawPrimitive(
    // Vertex Parameters
    val vertexCount: Int, // Vertices defined by primitive
    val elementCount: Int, // Elements needed by GL (i.e. split quad into 2 triangles)
    val primitiveType: Int, // Primitive ID used by OpenGL
    vararg val attributeSizes: Int // Floats inside each attribute
) {

    /** An object with 2 vertices with attributes: Pos (2), Color (4) */
    LINE(2, 2, GL11.GL_LINES, 2, 4) {
        override fun addIndices(elements: IntBuffer, index: Int) {
            for (v in 0 until elementCount) {
                elements.put(vertexCount * index + v)
            }
        }
    },

    /** An object with 3 vertices with attributes: Pos (2), Color (4) */
    TRIANGLE(3, 3, GL11.GL_TRIANGLES, 2, 4) {
        override fun addIndices(elements: IntBuffer, index: Int) {
            // Use counterclockwise winding
            for (v in 0 until elementCount) {
                elements.put(vertexCount * index + v)
            }
        }
    },

    /**
     * An object with 4 vertices with attributes: Pos (2), Color (4), Tex
     * Coords (2), Tex ID (1)
     */
    SPRITE(4, 6, GL11.GL_TRIANGLES, 2, 4, 2, 1) {
        override fun addIndices(elements: IntBuffer, index: Int) {
            // Split quad into two triangles
            val triangleVertices = intArrayOf(0, 1, 2, 0, 2, 3)
            for (v in triangleVertices) {
                elements.put(vertexCount * index + v)
            }
        }
    };

    val vertexSize: Int = IntMath.sum(*attributeSizes)// Floats inside one vertex

    /**
     * Adds indices to an element buffer array (EBO).
     *
     * @param elements an int buffer
     * @param index the vertex index
     */
    abstract fun addIndices(elements: IntBuffer, index: Int)

    override fun toString(): String = StringUtils.capitalizeFirstWord(name)

}