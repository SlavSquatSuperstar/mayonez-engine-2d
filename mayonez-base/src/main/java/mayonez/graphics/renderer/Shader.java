package mayonez.graphics.renderer;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.graphics.camera.*;
import mayonez.io.*;
import mayonez.io.text.*;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.util.*;

import static org.lwjgl.opengl.GL11.GL_FALSE;
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
    private boolean isActive;
    private List<ShaderProgram> programs;

    public Shader(String filename) {
        super(filename);
        isActive = false;
        readShaderFile();
        compileShaderPrograms();
    }

    private void readShaderFile() {
        try {
            var source = new TextIOManager().read(openInputStream());
            var shaders = source.split("(#type)( )+"); // shaders indicated by "#type '<shader_type>'"
            parseShaderFile(shaders);
        } catch (Exception e) {
            Logger.error("Could not read shader %s", getFilenameInQuotes());
            Mayonez.stop(1);
        }
    }

    private void parseShaderFile(String[] subPrograms) throws IllegalArgumentException {
        programs = new ArrayList<>();
        for (var shader : subPrograms) {
            shader = shader.strip();
            if (shader.equals("")) continue;

            programs.add(readNextProgram(shader));
        }
    }

    private static ShaderProgram readNextProgram(String shader) throws IllegalArgumentException {
        var firstNewLine = shader.indexOf("\n");
        var typeName = shader.substring(0, firstNewLine).trim();

        var shaderSource = shader.substring(firstNewLine + 1);
        var shaderType = ShaderType.getType(typeName);
        return new ShaderProgram(shaderType, shaderSource);
    }

    private void compileShaderPrograms() {
        programs.forEach(this::compileShader);
        var programIDs = programs.stream().map(ShaderProgram::getID).toList();
        linkShaderPrograms(programIDs);
    }

    private void compileShader(ShaderProgram shader) {
        try {
            shader.compileSource();
            Logger.debug("OpenGL: Compiled %s shader in %s", shader.getType(), getFilenameInQuotes());
        } catch (RuntimeException e) {
            Logger.error("OpenGL: Could not compile %s shader in  %s", shader.getType(), getFilenameInQuotes());
            Logger.error("OpenGL: " + glGetShaderInfoLog(shaderID));
            Mayonez.stop(1);
        }
    }

    private void linkShaderPrograms(List<Integer> programIDs) {
        shaderID = glCreateProgram();
        programIDs.forEach(id -> glAttachShader(shaderID, id));
        glLinkProgram(shaderID);
        checkLinkStatus(shaderID);
    }

    private void checkLinkStatus(int shaderID) {
        if (glGetProgrami(shaderID, GL_LINK_STATUS) == GL_FALSE) {
            Logger.error("OpenGL: Could not link shader file %s", getFilenameInQuotes());
            Logger.error("OpenGL: " + glGetProgramInfoLog(shaderID));
            Mayonez.stop(1);
        }
        Logger.debug("OpenGL: Linked shader file %s", getFilenameInQuotes());
    }

    // Renderer Methods

    /**
     * Bind this shader to the GPU.
     */
    public void bind() {
        if (!isActive) {
            glUseProgram(shaderID);
            isActive = true;
        }
    }

    public void uploadUniforms(GLCamera camera) {
        uploadMat4("uProjection", camera.getProjectionMatrix());
        uploadMat4("uView", camera.getViewMatrix());
    }

    /**
     * Unbind this shader from the GPU.
     */
    public void unbind() {
        if (isActive) {
            glUseProgram(0);
            isActive = false;
        }
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

    // Helper Methods

    private String getFilenameInQuotes() {
        return String.format("\"%s\"", getFilename());
    }

}
