package mayonez.graphics.renderer

import mayonez.annotations.*
import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER

/**
 * The type of shader used by the GPU.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
internal enum class ShaderType(
    /** The shader ID in OpenGL. */
    val glShaderType: Int,
    /** The name of the shader type in GLSL. */
    val shaderName: String
) {

    /** A vertex shader that calculates the scaling of an image. */
    VERTEX(GL_VERTEX_SHADER, "vertex"),

    /**
     * A fragment (pixel) shader that calculates the color and texture of each
     * individual pixel of an image.
     */
    FRAGMENT(GL_FRAGMENT_SHADER, "fragment");

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
        fun getType(shaderName: String?): ShaderType {
            for (type in ShaderType.values()) {
                if (type.name.equals(shaderName, ignoreCase = true)) {
                    return type
                }
            }
            throw IllegalArgumentException("Unexpected shader type \"$shaderName\"")
        }
    }

}
