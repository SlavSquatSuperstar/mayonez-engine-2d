package mayonez.io.image;

import mayonez.Logger;
import mayonez.Mayonez;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.io.text.TextAsset;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

/**
 * A compiled GLSL program that determines how to draw an image, including the colors and brightness.
 * Only available in the GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class Shader extends TextAsset {

    private int shaderID;
    private boolean used = false;
    private String vertexSrc, fragmentSrc;

    public Shader(String filename) {
        super(filename);
        parseShader();
        compileShader();
    }

    private void parseShader() {
        try {
            // Read the shader file
            var source = super.read();
            var shaders = source.split("(#type)( )+"); // split into vertex and fragment

            // Parse the shader file
            for (var i = 1; i < shaders.length; i++) {
                // Find the first pattern after #type 'pattern'
                var shader = shaders[i];
                var endLn = shader.indexOf("\n");
                var pattern = shader.substring(0, endLn).trim();

                shader = shader.substring(endLn + 1);
                switch (pattern) {
                    case "vertex" -> vertexSrc = shader;
                    case "fragment" -> fragmentSrc = shader;
                    default -> throw new IOException("Unexpected shader type \"" + pattern + "\"");
                }
            }
        } catch (Exception e) {
            Logger.error("Could not read file \"%s\"", getFilename());
        }
    }

    private void compileShader() {
        // Compile Vertex Shader
        var vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);

        // Check for compile errors
        if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
            Logger.error("OpenGL: Could not compile \"%s\" vertex shader", getFilename());
            Logger.error("OpenGL: " + glGetShaderInfoLog(vertexID));
            Mayonez.stop(1);
        }
        Logger.debug("OpenGL: Compiled \"%s\" vertex shader", getFilename());

        // Repeat for Fragment Shader
        var fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);

        if (glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
            Logger.error("OpenGL: Could not compile \"defaultshader.glsl\" fragment shader");
            Logger.error("OpenGL: " + glGetShaderInfoLog(fragmentID));
            Mayonez.stop(1);
        }
        Logger.debug("OpenGL: Compiled \"%s\" fragment shader", getFilename());

        // Link shaders
        shaderID = glCreateProgram();
        glAttachShader(shaderID, vertexID);
        glAttachShader(shaderID, fragmentID);
        glLinkProgram(shaderID);

        if (glGetProgrami(shaderID, GL_LINK_STATUS) == GL_FALSE) {
            Logger.error("OpenGL: Could not link shader file \"%s\"", getFilename());
            Logger.error("OpenGL: " + glGetProgramInfoLog(shaderID));
            Mayonez.stop(1);
        }
        Logger.debug("OpenGL: Linked shader file \"%s\"", getFilename());
    }

    // Renderer Methods

    /**
     * Bind this shader to the GPU.
     */
    public void bind() {
        if (!used) {
            glUseProgram(shaderID);
            used = true;
        }
    }

    /**
     * Unbind this shader from the GPU.
     */
    public void unbind() {
        if (used) {
            glUseProgram(0);
            used = false;
        }
    }

    // Upload Methods

    public void uploadInt(String varName, int i) {
        glUniform1i(uploadVariable(varName), i);
    }

    public void uploadIntArray(String varName, int[] arr) {
        glUniform1iv(uploadVariable(varName), arr);
    }

    public void uploadFloat(String varName, float f) {
        glUniform1f(uploadVariable(varName), f);
    }

    public void uploadVec2(String varName, Vector2f vec) {
        glUniform2f(uploadVariable(varName), vec.x, vec.y);
    }

    public void uploadVec3(String varName, Vector3f vec) {
        glUniform3f(uploadVariable(varName), vec.x, vec.y, vec.z);
    }

    public void uploadVec4(String varName, Vector4f vec) {
        glUniform4f(uploadVariable(varName), vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadMat3(String varName, Matrix3f mat) {
        var matBuffer = BufferUtils.createFloatBuffer(9);
        mat.get(matBuffer);
        glUniformMatrix4fv(uploadVariable(varName), false, matBuffer);
    }

    public void uploadMat4(String varName, Matrix4f mat) {
        var matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer); // Compress matrix into 16x1 array
        glUniformMatrix4fv(uploadVariable(varName), false, matBuffer);
    }

    public void uploadTexture(String varName, int slot) {
        uploadInt(varName, slot);
    }

    private int uploadVariable(String varName) {
        bind();
        return glGetUniformLocation(shaderID, varName);
    }

}
