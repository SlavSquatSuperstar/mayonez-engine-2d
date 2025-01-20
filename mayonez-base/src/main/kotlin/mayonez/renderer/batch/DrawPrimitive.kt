package mayonez.renderer.batch

import mayonez.graphics.*
import java.nio.IntBuffer

/**
 * Types of OpenGL primitive objects that can be submitted to the GPU. Each
 * object defines the attribute layout and count of vertices.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
enum class DrawPrimitive(
    val layout: ElementLayout, vararg val attributes: VertexAttribute
) {
    /** An object with 2 vertices, each with attributes position and color. */
    LINE(ElementLayout.LINE, VertexAttribute.POSITION, VertexAttribute.COLOR),

    /** An object with 3 vertices, each with attributes position and color. */
    TRIANGLE(ElementLayout.TRIANGLE, VertexAttribute.POSITION, VertexAttribute.COLOR),

    /**
     * An object with 4 vertices, each with attributes position, color, tex coords,
     * and tex slot.
     */
    SPRITE(
        ElementLayout.QUAD, VertexAttribute.POSITION, VertexAttribute.COLOR,
        VertexAttribute.TEX_COORD, VertexAttribute.TEX_SLOT
    );

    val vertexCount: Int
        get() = layout.vertexCount

    val elementCount: Int
        get() = layout.elementCount

    val drawMode: Int
        get() = layout.drawMode

    /** The number of components for all attributes per vertex. */
    val totalComponents: Int = attributes.sumOf { it.components }

    /**
     * Adds indices to an element buffer array (EBO).
     *
     * @param elements an int buffer
     * @param index the vertex index
     */
    fun addIndices(elements: IntBuffer, index: Int) {
        for (e in layout.elementOrder) {
            elements.put(vertexCount * index + e)
        }
    }

}