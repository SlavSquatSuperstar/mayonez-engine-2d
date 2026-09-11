package mayonez.renderer.shader;

import mayonez.*;
import mayonez.application.*;

import static org.lwjgl.opengl.GL20.*;

/**
 * A programmable stage within the Open rendering pipeline. Shader stages may
 * be combined into a single .glsl file or kept in separate files. Officially,
 * a ShaderStage is considered an OpenGL shader, while a {@link Shader} is
 * referred to as an OpenGL program.
 *
 * @author SlavSquatSuperstar
 */
@UsesBackend(Backend.GL)
class ShaderStage {

    private final String filename;
    private final String sourceCode; // Source code of GLSL shader
    private final ShaderType type; // Type of shader stage
    private int shaderID; // ID of shader stage in OpenGL

    ShaderStage(String filename, String sourceCode, ShaderType type) {
        this.filename = filename;
        this.sourceCode = sourceCode;
        this.type = type;
        shaderID = GL_NONE;
    }

    // Shader Methods

    /**
     * Compile the shader from its source code.
     *
     * @throws ShaderException if the shader could not be compiled
     */
    void compileSource() {
        shaderID = glCreateShader(type.glShaderType);
        glShaderSource(shaderID, sourceCode);
        glCompileShader(shaderID);

        // Check compiled correctly
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_TRUE) {
            Logger.debug("OpenGL: Compiled %s shader %s", type.toString(), filename);
        } else {
            Logger.error("OpenGL: Could not compile %s shader %s", type.toString(), filename);
            Logger.error("OpenGL: " + glGetShaderInfoLog(shaderID));
            throw new ShaderException("Error compiling shader stage");
        }
    }

    /**
     * Attaches this shader to a parent program.
     *
     * @param programID the parent program ID
     */
    void attachToProgram(int programID) {
        glAttachShader(programID, this.shaderID);
    }

    /**
     * Detaches this shader to a parent program.
     *
     * @param programID the parent program ID
     */
    void detachFromProgram(int programID) {
        glDetachShader(programID, this.shaderID);
    }

    /**
     * Cleans up the object compiled from this shader stage.
     */
    void delete() {
        // The shader must be detached first
        glDeleteShader(shaderID);
        shaderID = GL_NONE;
    }

    // Getter Methods

    ShaderType getType() {
        return type;
    }

}
