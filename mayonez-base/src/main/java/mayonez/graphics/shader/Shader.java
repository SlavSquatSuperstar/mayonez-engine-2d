package mayonez.graphics.shader;

import mayonez.*;
import mayonez.assets.*;
import mayonez.graphics.*;
import mayonez.io.text.*;
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
    private boolean active;

    public Shader(String filename) {
        super(filename);
        shaderID = glCreateProgram();
        active = false;

        try {
            List<ShaderProgram> programs = readShaderPrograms();
            programs.forEach(this::compileShader);
            linkShaderPrograms(programs);
            programs.forEach(ShaderProgram::delete); // Clean up intermediate programs
        } catch (ShaderException e) {
            // TODO does not stop because still initializing assets
            Logger.printStackTrace(e);
            Mayonez.stop(ExitCode.ERROR);
        }
    }

    // Read Shader Helper Methods

    private List<ShaderProgram> readShaderPrograms() throws ShaderException {
        try {
            var source = new TextIOManager().read(openInputStream());
            var shaders = source.split("(#type)( )+"); // shaders indicated by "#type <shader_type>"
            return parseShaderPrograms(shaders);
        } catch (Exception e) {
            Logger.error("Could not read shader %s", getFilenameInQuotes());
            throw new ShaderException(e);
        }
    }

    private List<ShaderProgram> parseShaderPrograms(String[] subPrograms) throws RuntimeException {
        var programs = new ArrayList<ShaderProgram>();
        for (var shader : subPrograms) {
            shader = shader.strip();
            if (shader.isEmpty()) continue;
            programs.add(readNextProgram(shader));
        }
        return programs;
    }

    private ShaderProgram readNextProgram(String shaderSource) throws RuntimeException {
        var firstNewLine = shaderSource.indexOf("\n");
        var typeName = shaderSource.substring(0, firstNewLine).trim();

        var programSource = shaderSource.substring(firstNewLine + 1);
        var shaderType = ShaderType.findWithName(typeName);
        return new ShaderProgram(shaderType, programSource);
    }

    // Compile Shader Helper Methods

    private void compileShader(ShaderProgram shader) throws ShaderException {
        try {
            shader.compileSource();
            Logger.debug("OpenGL: Compiled %s shader in %s", shader.getTypeName(), getFilenameInQuotes());
        } catch (ShaderException e) {
            Logger.error("OpenGL: Could not compile %s shader in %s", shader.getTypeName(), getFilenameInQuotes());
            Logger.error("OpenGL: " + glGetShaderInfoLog(shaderID));
            throw e;
        }
    }

    private void linkShaderPrograms(List<ShaderProgram> programs) throws ShaderException {
        programs.forEach(p -> p.linkToProgram(shaderID));
        glLinkProgram(shaderID);
        glValidateProgram(shaderID);
        checkLinkStatus(shaderID);
    }

    private void checkLinkStatus(int shaderID) throws ShaderException {
        if (glGetProgrami(shaderID, GL_LINK_STATUS) == GL_FALSE) {
            Logger.error("OpenGL: Could not link shader file %s", getFilenameInQuotes());
            Logger.error("OpenGL: " + glGetProgramInfoLog(shaderID));
            throw new ShaderException("Error linking shader file");
        }
        Logger.debug("OpenGL: Linked shader file %s", getFilenameInQuotes());
    }

    // Renderer Methods

    /**
     * Bind this shader to the GPU.
     */
    public void bind() {
        if (active) return;
        glUseProgram(shaderID);
        active = true;
    }

    /**
     * Unbind this shader from the GPU.
     */
    public void unbind() {
        if (!active) return;
        glUseProgram(GL_NONE);
        active = false;
    }

    // Upload Methods

    public void uploadIntArray(String varName, int[] arr) {
        glUniform1iv(uploadVariable(varName), arr);
    }

    public void uploadMat4(String varName, Matrix4f mat) {
        var matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer); // Compress matrix into 16x1 array
        glUniformMatrix4fv(uploadVariable(varName), false, matBuffer);
    }

    private int uploadVariable(String varName) {
        bind();
        return glGetUniformLocation(shaderID, varName);
    }

}
