package mayonez.renderer.batch

import mayonez.application.*
import org.lwjgl.opengl.GL11.GL_FLOAT

/**
 * A feature of an individual vertex, such as position or color, passed to the GPU.
 * Each attribute defines a count and data type for its components.
 *
 * @author SlavSquatSuperstar
 */
@UsesBackend(Backend.GL)
enum class VertexAttribute(
    /** The number of components (floats or integers) in the attribute. */
    val components: Int,
    /** The number of bytes per component. */
    val componentBytes: Int,
    /** The data type used by OpenGL. */
    val glType: Int
) {

    /** A single integer. */
    INT(1, Float.SIZE_BYTES, GL_FLOAT),

    /** A single float. */
    FLOAT(1, Float.SIZE_BYTES, GL_FLOAT),

    /** Two related floats. */
    FLOAT2(2, Float.SIZE_BYTES, GL_FLOAT),

    /** Four related floats. */
    FLOAT4(4, Float.SIZE_BYTES, GL_FLOAT);

    /** The total size in bytes of this attribute. */
    val totalBytes: Int
        get() = components * componentBytes

}
