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
    /** The number of floats or integers in the attribute. */
    @JvmField val count: Int,
    /** The size in bytes of the data type. */
    @JvmField val bytes: Int,
    /** The data type used by OpenGL. */
    @JvmField val glType: Int
) {

    /** The position of a vertex, (x, y). */
    POSITION(2, Float.SIZE_BYTES, GL_FLOAT),

    /** The color of a vertex, (r, g, b, a). */
    COLOR(4, Float.SIZE_BYTES, GL_FLOAT),

    /** The texture (UV) coordinates of a vertex, (u, v). */
    TEX_COORD(2, Float.SIZE_BYTES, GL_FLOAT),

    /** The texture ID of a vertex, id. */
    TEX_ID(1, Float.SIZE_BYTES, GL_FLOAT); // Better to use as float since everything else is a float

    val totalSize: Int = count * bytes
    
    fun setVertexAttribute(index: Int, vertexSize: Int, offset: Int) {
        glVertexAttribPointer(index, count, glType, false, vertexSize * bytes, offset.toLong())
        glEnableVertexAttribArray(index)
    }

}