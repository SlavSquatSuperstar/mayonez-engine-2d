package mayonez.graphics.batch

import mayonez.graphics.*
import org.lwjgl.opengl.GL11.GL_FLOAT
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer

/**
 * Defines the datatype and length of an attribute of an individual vertex.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
enum class VertexAttribute(
    /** The number of components (floats or integers) in the attribute. */
    val count: Int,
    /** The size in bytes of the data type. */
    private val bytes: Int,
    /** The data type used by OpenGL. */
    private val glType: Int
) {

    /** The position of a vertex, (x, y). */
    POSITION(2, Float.SIZE_BYTES, GL_FLOAT),

    /** The color of a vertex, (r, g, b, a). */
    COLOR(4, Float.SIZE_BYTES, GL_FLOAT),

    /** The texture (UV) coordinates of a vertex, (u, v). */
    TEX_COORD(2, Float.SIZE_BYTES, GL_FLOAT),

    /** The texture ID of a vertex, id. */
    TEX_ID(1, Float.SIZE_BYTES, GL_FLOAT); // Better to use as float since everything else is a float

    /** The total size in bytes of this attribute. */
    val sizeBytes: Int = count * bytes

    /**
     * Tell the GPU which parts of the vertex belong to this attribute.
     *
     * @param index the index of the attribute in the vertex
     * @param componentCount the total number of components in the vertex
     * @param offset the offset in bytes from the vertex start
     */
    fun setVertexAttribute(index: Int, componentCount: Int, offset: Int) {
        glVertexAttribPointer(index, count, glType, false, componentCount * bytes, offset.toLong())
        glEnableVertexAttribArray(index)
    }

}