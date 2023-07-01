package mayonez.graphics.shader

import mayonez.annotations.*
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

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
    val id: Int = GL20.glCreateShader(type.glShaderType)
        @JvmName("getID")
        get

    /** The type of the shader sub-program in OpenGL. */
    val type: String = type.toString()

    @Throws(ShaderException::class)
    fun compileSource() {
        GL20.glShaderSource(id, source)
        GL20.glCompileShader(id)
        if (!checkCompiledCorrectly(id)) {
            throw ShaderException("Error compiling shader program")
        }
    }

    private fun checkCompiledCorrectly(id: Int): Boolean {
        return GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) != GL11.GL_FALSE
    }

}