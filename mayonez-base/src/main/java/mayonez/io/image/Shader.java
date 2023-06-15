package mayonez.io.image;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.graphics.camera.*;
import mayonez.io.*;
import mayonez.io.text.*;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

/**
 * A compiled OpenGL Shader program (.glsl) that tells the GPU how to draw an image, specifying the colors, brightness,
 * and texture. Only available in the GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class Shader extends Asset {

    private static final String VERTEX_SHADER_TYPE = "vertex";
    private static final String FRAGMENT_SHADER_TYPE = "fragment";

    private int shaderID;
    private boolean isActive;
    private String vertexSrc, fragmentSrc;

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

    private void parseShaderFile(String[] shaders) throws IOException {
        for (var shader : shaders) {
            shader = shader.strip();
            if (shader.equals("")) continue;

            var endLn = shader.indexOf("\n");
            var pattern = shader.substring(0, endLn).trim();

            shader = shader.substring(endLn + 1);
            switch (pattern) {
                case VERTEX_SHADER_TYPE -> vertexSrc = shader;
                case FRAGMENT_SHADER_TYPE -> fragmentSrc = shader;
                default -> throw new IOException("Unexpected shader type \"" + pattern + "\"");
            }
        }
    }

    private void compileShaderPrograms() {
        int vertexID = compileShader(GL_VERTEX_SHADER, vertexSrc, VERTEX_SHADER_TYPE);
        int fragmentID = compileShader(GL_FRAGMENT_SHADER, fragmentSrc, FRAGMENT_SHADER_TYPE);
        linkShaderPrograms(vertexID, fragmentID);
    }

    private int compileShader(int glShaderType, String shaderSrc, String shaderType) {
        var shaderID = glCreateShader(glShaderType);
        glShaderSource(shaderID, shaderSrc);
        glCompileShader(shaderID);
        checkCompileStatus(shaderID, shaderType);
        return shaderID;
    }

    private void checkCompileStatus(int shaderID, String shaderType) {
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            Logger.error("OpenGL: Could not compile %s shader in  %s", shaderType, getFilenameInQuotes());
            Logger.error("OpenGL: " + glGetShaderInfoLog(shaderID));
            Mayonez.stop(1);
        }
        Logger.debug("OpenGL: Compiled %s shader in  %s", shaderType, getFilenameInQuotes());
    }

    private void linkShaderPrograms(int vertexID, int fragmentID) {
        shaderID = glCreateProgram();
        glAttachShader(shaderID, vertexID);
        glAttachShader(shaderID, fragmentID);
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
