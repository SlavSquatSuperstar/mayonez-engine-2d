package mayonez.renderer.shader

import mayonez.graphics.*
import mayonez.util.*
import org.lwjgl.opengl.GL20

/**
 * The type of shader used by the GPU.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
internal enum class ShaderType(
    /** The shader ID in OpenGL. */
    val glShaderType: Int
) {

    /** A vertex shader that transforms an image to its screen position. */
    VERTEX(GL20.GL_VERTEX_SHADER),

    /**
     * A fragment (pixel) shader that calculates the color and texture of each
     * individual pixel of an image.
     */
    FRAGMENT(GL20.GL_FRAGMENT_SHADER);

    companion object {
        /**
         * Returns the [ShaderType] constant matching the given name,
         * case-insensitive.
         *
         * @param shaderName the name of the shader program
         * @return the shader type
         * @throws IllegalArgumentException if the shader name is invalid
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun findWithName(shaderName: String?): ShaderType {
            return StringUtils.findWithName(entries, shaderName)
                ?: throw IllegalArgumentException("Unexpected shader type \"$shaderName\"")
        }

    }

}