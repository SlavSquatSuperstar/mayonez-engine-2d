package slavsquatsuperstar.mayonezgl.renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;
import slavsquatsuperstar.fileio.Assets;
import slavsquatsuperstar.fileio.TextFile;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonezgl.GameGL;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private String filename;
    private int shaderProgramID;
    private boolean used = false;
    private String vertexSrc, fragmentSrc;

    public Shader(String filename) {
        this.filename = filename;
        parse();
        compile();
    }

    private void parse() {
        try {
            // Read the shader file
            TextFile shaderFile = Assets.getAsset(filename, TextFile.class);
            String source = shaderFile.readText();
            String[] shaders = source.split("(#type)( )+"); // split into vertex and fragment

            // Parse the shader file
            for (int i = 1; i < shaders.length; i++) {
                // Find the first pattern after #type 'pattern'
                String shader = shaders[i];
                int endLn = shader.indexOf("\n");
                String pattern = shader.substring(0, endLn).trim();

                shader = shader.substring(endLn + 1);
                switch (pattern) {
                    case "vertex":
                        vertexSrc = shader;
                        break;
                    case "fragment":
                        fragmentSrc = shader;
                        break;
                    default:
                        throw new IOException("Unexpected shader type \"" + pattern + "\"");
                }
            }
        } catch (Exception e) {
            Logger.warn("Shader: Could not read file \"%s\"", filename);
        }
    }

    private void compile() {
        // Compile Vertex Shader
        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);

        // Check for compile errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            Logger.warn("Error: Could not compile \"defaultshader.glsl\" vertex shader");
            Logger.warn(glGetShaderInfoLog(vertexID));
            GameGL.instance().stop(1);
        }
        Logger.trace("Successfully compiled \"defaultshader.glsl\" vertex shader");

        // Repeat for Fragment Shader
        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            Logger.warn("Error: Could not compile \"defaultshader.glsl\" fragment shader");
            Logger.warn(glGetShaderInfoLog(fragmentID));
            GameGL.instance().stop(1);
        }
        Logger.trace("Successfully compiled \"%s\" fragment shader", filename);

        // Link shaders
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            Logger.warn("Error: Could not link \"%s\" shaders", filename);
            Logger.warn(glGetProgramInfoLog(shaderProgramID));
            GameGL.instance().stop(1);
        }
        Logger.trace("Successfully linked \"%s\" shaders", filename);
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

    public void uploadTexture(String varName, int slot) {
        glUniform1i(uploadVariable(varName), slot);
    }

    private int uploadVariable(String varName) {
        bind();
        return glGetUniformLocation(shaderProgramID, varName);
    }

}
