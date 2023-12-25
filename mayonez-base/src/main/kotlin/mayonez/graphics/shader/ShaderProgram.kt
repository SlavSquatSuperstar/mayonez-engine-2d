package mayonez.graphics.shader

import mayonez.*
import mayonez.annotations.*
import mayonez.graphics.*
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.*

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
    private val programID: Int = glCreateShader(type.glShaderType)

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
        glShaderSource(programID, source)
        glCompileShader(programID)
        if (!checkCompiledCorrectly(programID)) {
            throw ShaderException("Error compiling shader program")
        }
    }

    private fun checkCompiledCorrectly(id: Int): Boolean {
        return glGetShaderi(id, GL_COMPILE_STATUS) != GL_FALSE
    }

    /**
     * Links this shader to a parent shader program.
     *
     * @param shaderID the parent shader ID
     */
    fun linkToProgram(shaderID: Int) {
        glAttachShader(shaderID, programID)
    }

    /** Cleans up the object compiled from this shader sub-program. */
    fun delete() {
        glDeleteShader(programID)
    }

}