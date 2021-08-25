package slavsquatsuperstar.mayonezgl.renderer;

import slavsquatsuperstar.fileio.TextFile;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonezgl.GameGL;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

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

    public void parse() {
        try {
            // Read the shader file
            TextFile shaderFile = new TextFile(filename, true);
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

        Logger.log("vertex = %s", vertexSrc);
        Logger.log("fragment = %s", fragmentSrc);
    }

    public void compile() {
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
        glUseProgram(shaderProgramID);
    }

    public void detach() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        glUseProgram(0);
    }

}
