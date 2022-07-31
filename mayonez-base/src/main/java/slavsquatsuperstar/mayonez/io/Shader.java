package slavsquatsuperstar.mayonez.io;

import org.joml.*;
import org.lwjgl.BufferUtils;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.annotations.EngineType;

import java.io.IOException;
import java.nio.FloatBuffer;

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

    private int shaderProgramID;
    private boolean used = false;
    private String vertexSrc, fragmentSrc;

    // todo should maybe not provide constructors
    public Shader(String filename) {
        super(filename);
        parseShader();
        compileShader();
    }

    private void parseShader() {
        try {
            // Read the shader file
            String source = super.read();
            String[] shaders = source.split("(#type)( )+"); // split into vertex and fragment

            // Parse the shader file
            for (int i = 1; i < shaders.length; i++) {
                // Find the first pattern after #type 'pattern'
                String shader = shaders[i];
                int endLn = shader.indexOf("\n");
                String pattern = shader.substring(0, endLn).trim();

                shader = shader.substring(endLn + 1);
                switch (pattern) {
                    case "vertex" -> vertexSrc = shader;
                    case "fragment" -> fragmentSrc = shader;
                    default -> throw new IOException("Unexpected shader type \"" + pattern + "\"");
                }
            }
        } catch (Exception e) {
            Logger.error("Shader: Could not read file \"%s\"", getFilename());
        }
    }

    private void compileShader() {
        // Compile Vertex Shader
        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);

        // Check for compile errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            Logger.error("Shader: Could not compile \"defaultshader.glsl\" vertex shader");
            Logger.error("OpenGL: " + glGetShaderInfoLog(vertexID));
            Mayonez.stop(1);
        }
        Logger.debug("Successfully compiled \"defaultshader.glsl\" vertex shader");

        // Repeat for Fragment Shader
        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            Logger.error("Shader: Could not compile \"defaultshader.glsl\" fragment shader");
            Logger.error("OpenGL: " + glGetShaderInfoLog(fragmentID));
            Mayonez.stop(1);
        }
        Logger.debug("Successfully compiled \"%s\" fragment shader", getFilename());

        // Link shaders
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            Logger.error("Error: Could not link \"%s\" shaders", getFilename());
            Logger.error("OpenGL: " + glGetProgramInfoLog(shaderProgramID));
            Mayonez.stop(1);
        }
        Logger.debug("Successfully linked \"%s\" shaders", getFilename());
    }

    public void bind() {
        if (!used) {
            glUseProgram(shaderProgramID);
            used = true;
        }
    }

    public void unbind() {
        glUseProgram(0);
        used = false;
    }

    public void uploadMat4(String varName, Matrix4f mat) {
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer); // Compress matrix into 16x1 array
        glUniformMatrix4fv(uploadVariable(varName), false, matBuffer);
    }

    public void uploadMat3(String varName, Matrix3f mat) {
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat.get(matBuffer);
        glUniformMatrix4fv(uploadVariable(varName), false, matBuffer);
    }

    public void uploadVec4(String varName, Vector4f vec) {
        glUniform4f(uploadVariable(varName), vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3(String varName, Vector3f vec) {
        glUniform3f(uploadVariable(varName), vec.x, vec.y, vec.z);
    }

    public void uploadVec2(String varName, Vector2f vec) {
        glUniform2f(uploadVariable(varName), vec.x, vec.y);
    }

    public void uploadFloat(String varName, float f) {
        glUniform1f(uploadVariable(varName), f);
    }

    public void uploadInt(String varName, int i) {
        glUniform1i(uploadVariable(varName), i);
    }

    public void uploadIntArray(String varName, int[] arr) {
        glUniform1iv(uploadVariable(varName), arr);
    }

    public void uploadTexture(String varName, int slot) {
        uploadInt(varName, slot);
    }

    private int uploadVariable(String varName) {
        bind();
        return glGetUniformLocation(shaderProgramID, varName);
    }

}
