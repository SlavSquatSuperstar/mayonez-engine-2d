package mayonez.renderer.shader;

import mayonez.*;
import mayonez.assets.*;
import mayonez.graphics.*;
import mayonez.io.*;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.util.*;

import static org.lwjgl.opengl.GL20.*;

/**
 * A compiled OpenGL Shader program (.glsl) used by the GL engine. A shader tells
 * the GPU how to draw an image, specifying the colors, brightness, and texture.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class Shader extends Asset {

    private final int shaderID;

    public Shader(String filename) {
        super(filename);

        if (GLHelper.isGLInitialized()) {
            shaderID = glCreateProgram();
        } else {
            shaderID = GL_NONE;
            return;
        }

        try {
            Logger.debug("Creating shader from file %s", getFilenameInQuotes());
            List<ShaderProgram> programs = readShaderPrograms();
            programs.forEach(this::compileShader);
            linkShaderPrograms(programs);
            programs.forEach(ShaderProgram::delete); // Clean up intermediate programs
        } catch (ShaderException e) {
            Logger.printStackTrace(e);
        }
    }

    // Read Shader Methods

    private List<ShaderProgram> readShaderPrograms() throws ShaderException {
        try {
            var source = TextIOUtils.readText(openInputStream());
            var shaders = source.split("(#type)( )+"); // shaders indicated by "#type <shader_type>"
            return parseShaderPrograms(shaders);
        } catch (Exception e) {
            Logger.error("Could not parse shader file %s", getFilenameInQuotes());
            throw new ShaderException(e);
        }
    }

    private static List<ShaderProgram> parseShaderPrograms(String[] subPrograms) throws ShaderException {
        var programs = new ArrayList<ShaderProgram>();
        for (var shader : subPrograms) {
            var src = shader.strip();
            if (src.isEmpty()) continue;
            programs.add(readShaderProgram(src));
        }
        return programs;
    }

    private static ShaderProgram readShaderProgram(String shaderSource) throws ShaderException {
        var firstNewLine = shaderSource.indexOf("\n");
        var typeName = shaderSource.substring(0, firstNewLine).trim();

        var programSource = shaderSource.substring(firstNewLine + 1);
        var shaderType = ShaderType.findWithName(typeName);
        return new ShaderProgram(programSource, shaderType);
    }

    // Compile Shader Methods

    private void compileShader(ShaderProgram shader) throws ShaderException {
        try {
            shader.compileSource();
        } catch (ShaderException e) {
            Logger.error("OpenGL: " + glGetShaderInfoLog(shaderID));
            throw e;
        }
    }

    private void linkShaderPrograms(List<ShaderProgram> programs) throws ShaderException {
        programs.forEach(p -> p.linkToProgram(shaderID));
        glLinkProgram(shaderID);
        glValidateProgram(shaderID);

        // Check link status
        if (shaderLinkedSuccessfully(shaderID)) {
            Logger.debug("OpenGL: Finished linking shader file %s", getFilenameInQuotes());
        } else {
            Logger.error("OpenGL: Could not link shader file %s", getFilenameInQuotes());
            Logger.error("OpenGL: " + glGetProgramInfoLog(shaderID));
            throw new ShaderException("Error linking shader file");
        }
    }

    private static boolean shaderLinkedSuccessfully(int shaderID) {
        return glGetProgrami(shaderID, GL_LINK_STATUS) != GL_FALSE;
    }

    // Renderer Methods

    /**
     * Bind this shader to the GPU.
     */
    public void bind() {
        glUseProgram(shaderID);
    }

    /**
     * Unbind this shader from the GPU.
     */
    public void unbind() {
        glUseProgram(GL_NONE);
    }

    /**
     * Delete this shader program from the GPU.
     */
    public void delete() {
        if (GLHelper.isGLInitialized()) {
            glDeleteProgram(shaderID);
        }
    }

    // Uniform Methods

    public void uploadIntArray(String varName, int[] arr) {
        glUniform1iv(getVariableLocation(varName), arr);
    }

    public void uploadMat4(String varName, Matrix4f mat) {
        var matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer); // Compress matrix into 16x1 array
        glUniformMatrix4fv(getVariableLocation(varName), false, matBuffer);
    }

    /**
     * Gets the ID for a uniform (variable) to be uploaded for the next draw call.
     *
     * @param varName the variable name in the shader
     * @return the variable location
     */
    private int getVariableLocation(String varName) {
        return glGetUniformLocation(shaderID, varName);
    }

    // Asset Methods

    @Override
    public void free() {
        delete();
    }

}
