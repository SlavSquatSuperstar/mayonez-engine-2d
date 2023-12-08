package mayonez.graphics.shader;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.camera.*;
import mayonez.assets.*;
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

    private int shaderID;
    private boolean active;

    public Shader(String filename) {
        super(filename);
        active = false;

        try {
            List<ShaderProgram> programs = readShaderPrograms();
            compileShaderPrograms(programs);
        } catch (ShaderException e) {
            Logger.printStackTrace(e);
            Mayonez.stop(ExitCode.ERROR);
        }
    }

    // Read Shader Helper Methods

    private List<ShaderProgram> readShaderPrograms() throws ShaderException {
        try {
            var source = new TextIOManager().read(openInputStream());
            var shaders = source.split("(#type)( )+"); // shaders indicated by "#type '<shader_type>'"
            return parseShaderPrograms(shaders);
        } catch (Exception e) {
            Logger.error("Could not read shader %s", getFilenameInQuotes());
            throw new ShaderException(e);
        }
    }

    private static ArrayList<ShaderProgram> parseShaderPrograms(String[] subPrograms) throws RuntimeException {
        var programs = new ArrayList<ShaderProgram>();
        for (var shader : subPrograms) {
            shader = shader.strip();
            if (shader.isEmpty()) continue;
            programs.add(readNextProgram(shader));
        }
        return programs;
    }

    private static ShaderProgram readNextProgram(String shader) throws RuntimeException {
        var firstNewLine = shader.indexOf("\n");
        var typeName = shader.substring(0, firstNewLine).trim();

        var shaderSource = shader.substring(firstNewLine + 1);
        var shaderType = ShaderType.findWithName(typeName);
        return new ShaderProgram(shaderType, shaderSource);
    }

    // Compile Shader Helper Methods

    private void compileShaderPrograms(List<ShaderProgram> programs) throws ShaderException {
        programs.forEach(this::compileShader);
        var programIDs = programs.stream().map(ShaderProgram::getID).toList();
        linkShaderPrograms(programIDs);
    }

    private void compileShader(ShaderProgram shader) throws ShaderException {
        try {
            shader.compileSource();
            Logger.debug("OpenGL: Compiled %s shader in %s", shader.getType(), getFilenameInQuotes());
        } catch (ShaderException e) {
            Logger.error("OpenGL: Could not compile %s shader in  %s", shader.getType(), getFilenameInQuotes());
            Logger.error("OpenGL: " + glGetShaderInfoLog(shaderID));
            throw e;
        }
    }

    private void linkShaderPrograms(List<Integer> programIDs) throws ShaderException {
        shaderID = glCreateProgram();
        programIDs.forEach(id -> glAttachShader(shaderID, id));
        glLinkProgram(shaderID);
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

    public void uploadUniforms(GLCamera camera) {
        uploadMat4("uProjection", camera.getProjectionMatrix());
        uploadMat4("uView", camera.getViewMatrix());
    }

    /**
     * Unbind this shader from the GPU.
     */
    public void unbind() {
        if (!active) return;
        glUseProgram(0);
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
