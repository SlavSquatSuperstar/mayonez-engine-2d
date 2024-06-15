package mayonez.renderer.batch

import mayonez.graphics.*
import org.lwjgl.opengl.GL11.GL_FLOAT

/**
 * A feature of an individual vertex, such as position or color, passed to the GPU.
 * Each attribute defines a count and data type for its components.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
enum class VertexAttribute(
    /** The number of components (floats or integers) in the attribute. */
    val components: Int,
    /** The number of bytes per component. */
    val componentBytes: Int,
    /** The data type used by OpenGL. */
    val glType: Int
) {

    /** The position of a vertex, (x, y). */
    POSITION(2, Float.SIZE_BYTES, GL_FLOAT),

    /** The color of a vertex, (r, g, b, a). */
    COLOR(4, Float.SIZE_BYTES, GL_FLOAT),

    /** The texture (UV) coordinates of a vertex, (u, v). */
    TEX_COORD(2, Float.SIZE_BYTES, GL_FLOAT),

    /** The texture ID of a vertex, id. */
    TEX_ID(1, Float.SIZE_BYTES, GL_FLOAT);

    /** The total size in bytes of this attribute. */
    val totalBytes: Int
        get() = components * componentBytes

}
