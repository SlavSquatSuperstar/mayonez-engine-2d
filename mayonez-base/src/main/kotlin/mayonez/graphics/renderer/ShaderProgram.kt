package mayonez.graphics.renderer

import mayonez.annotations.*
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.*

/**
 * An individual sub-program within a .glsl [Shader] file.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
internal class ShaderProgram(
    type: ShaderType,
    /** The .glsl source code of the shader program. */
    private val source: String
) {

    /** The ID of the shader sub-program in OpenGL. */
    val id: Int = glCreateShader(type.glShaderType)
        @JvmName("getID")
        get

    /** The type of the shader sub-program in OpenGL. */
    val type: String = type.shaderName

    @Throws(ShaderException::class)
    fun compileSource() {
        glShaderSource(id, source)
        glCompileShader(id)
        if (!checkCompiledCorrectly(id)) {
            throw ShaderException("Error compiling shader program")
        }
    }

    private fun checkCompiledCorrectly(id: Int): Boolean {
        return glGetShaderi(id, GL_COMPILE_STATUS) != GL_FALSE
    }

}
