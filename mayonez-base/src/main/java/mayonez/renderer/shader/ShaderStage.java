package mayonez.renderer.shader;

import mayonez.*;
import mayonez.application.*;
import mayonez.assets.Asset;
import mayonez.assets.FilePath;
import mayonez.assets.text.TextIOUtils;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;

/**
 * A programmable stage within the Open rendering pipeline. Shader stages are
 * typically read from .glsl, .vert, or .frag source files. Officially, a
 * ShaderStage is considered an OpenGL shader, while a {@link Shader} is
 * referred to as an OpenGL program.
 *
 * @author SlavSquatSuperstar
 */
@UsesBackend(Backend.GL)
class ShaderStage extends Asset {

    private final ShaderType type; // Type of shader stage
    private int shaderID; // ID of shader stage in OpenGL
    // TODO parse uniforms

    ShaderStage(String filename, ShaderType type) {
        super(filename);
        this.type = type;
        shaderID = GL_NONE;
    }

    // Shader Methods

    /**
     * Read the source code from the .glsl file.
     *
     * @return the source code
     * @throws ShaderException if the file could not be read
     */
    String readSource() throws ShaderException {
        try (var input = openInputStream()) {
            return TextIOUtils.readText(input);
        } catch (IOException e) {
            throw new ShaderException("Could not read shader source: %s"
                    .formatted(getFilenameInQuotes()));
        }
    }

    /**
     * Compile the shader from its source code.
     *
     * @throws ShaderException if the shader could not be compiled
     */
    void compileSource() {
        shaderID = glCreateShader(type.glShaderType);
        var source = readSource();
        glShaderSource(shaderID, source);
        glCompileShader(shaderID);

        // Check compiled correctly
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_TRUE) {
            Logger.debug("OpenGL: Compiled %s shader %s", type.toString(), getFilename());
        } else {
            Logger.error("OpenGL: Could not compile %s shader %s", type.toString(), getFilename());
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
