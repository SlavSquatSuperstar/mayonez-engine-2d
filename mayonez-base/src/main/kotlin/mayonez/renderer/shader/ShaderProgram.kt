package mayonez.renderer.shader

import mayonez.graphics.*
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

/**
 * An individual sub-program within a .glsl [Shader] file.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
internal class ShaderProgram(
    private val type: ShaderType,
    /** The .glsl source code of the shader sub-program. */
    private val source: String
) {

    /** The ID of the shader sub-program in OpenGL. */
    private val programID: Int = GL20.glCreateShader(type.glShaderType)

    /** The type of the shader sub-program in OpenGL. */
    val typeName: String
        get() = type.toString()

    /**
     * Compile the shader program from its source code.
     *
     * @throws ShaderException if the program could not be compiled
     */
    @Throws(ShaderException::class)
    fun compileSource() {
        GL20.glShaderSource(programID, source)
        GL20.glCompileShader(programID)
        if (!checkCompiledCorrectly(programID)) {
            throw ShaderException("Error compiling shader program")
        }
    }

    private fun checkCompiledCorrectly(id: Int): Boolean {
        return GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) != GL11.GL_FALSE
    }

    /**
     * Links this shader to a parent shader program.
     *
     * @param shaderID the parent shader ID
     */
    fun linkToProgram(shaderID: Int) {
        GL20.glAttachShader(shaderID, programID)
    }

    /** Cleans up the object compiled from this shader sub-program. */
    fun delete() {
        GL20.glDeleteShader(programID)
    }

}