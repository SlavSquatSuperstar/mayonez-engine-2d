package mayonez.graphics.shader

import mayonez.annotations.*
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

    /** A vertex shader that calculates the scaling of an image. */
    VERTEX(GL20.GL_VERTEX_SHADER),

    /**
     * A fragment (pixel) shader that calculates the color and texture of each
     * individual pixel of an image.
     */
    FRAGMENT(GL20.GL_FRAGMENT_SHADER);

    /** The name of the shader type that GLSL uses. */
    override fun toString(): String {
        return StringUtils.capitalizeFirstWord(this.name)
    }

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
            return StringUtils.findConstantWithName(entries.toTypedArray(), shaderName)
                ?: throw IllegalArgumentException("Unexpected shader type \"$shaderName\"")
        }

    }

}