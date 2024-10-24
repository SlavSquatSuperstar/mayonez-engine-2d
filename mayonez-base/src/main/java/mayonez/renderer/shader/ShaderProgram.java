package mayonez.renderer.shader;

import mayonez.*;
import mayonez.graphics.*;

import static org.lwjgl.opengl.GL20.*;

/**
 * An individual sub-program within a .glsl shader file.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class ShaderProgram {

    private final String sourceCode; // Source code of .glsl sub-program
    private final ShaderType type; // Type of shader sub-program
    private final int programID; // ID of shader sub-program in OpenGL

    ShaderProgram(String sourceCode, ShaderType type) {
        this.sourceCode = sourceCode;
        this.type = type;
        programID = glCreateShader(type.glShaderType);
    }

    // Shader Program Methods

    /**
     * Compile the shader program from its source code.
     *
     * @throws ShaderException if the program could not be compiled
     */
    void compileSource() {
        glShaderSource(programID, sourceCode);
        glCompileShader(programID);

        if (!checkCompiledCorrectly(programID)) {
            Logger.error("OpenGL: Could not compile %s shader", type.toString());
            throw new ShaderException("Error compiling shader program");
        } else {
            Logger.debug("OpenGL: Compiled %s shader", type.toString());
        }
    }

    private static boolean checkCompiledCorrectly(int programID) {
        return glGetShaderi(programID, GL_COMPILE_STATUS) != GL_FALSE;
    }

    /**
     * Links this shader to a parent shader program.
     *
     * @param shaderID the parent shader ID
     */
    void linkToProgram(int shaderID) {
        glAttachShader(shaderID, programID);
    }

    /**
     * Cleans up the object compiled from this shader sub-program.
     */
    void delete() {
        glDeleteShader(programID);
    }

}
