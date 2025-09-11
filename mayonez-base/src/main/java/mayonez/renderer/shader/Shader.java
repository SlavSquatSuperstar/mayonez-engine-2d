package mayonez.renderer.shader;

import mayonez.*;
import mayonez.assets.*;
import mayonez.assets.text.*;
import mayonez.graphics.*;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.util.*;

import static org.lwjgl.opengl.GL20.*;

/**
 * A compiled program written in the OpenGL Shading Language (GLSL) composed of multiple
 * {@link ShaderStage}s. A <a href="https://www.khronos.org/opengl/wiki/Shader">shader</a>
 * tells the GPU how to draw an image, specifying the colors, brightness, and texture.
 * Shaders require at least a vertex and fragment stage.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class Shader extends Asset {

    private final int programID;
    private final Map<String, Integer> uniformLocations;

    public Shader(String filename) {
        super(filename);
        if (GLHelper.isGLInitialized()) {
            programID = glCreateProgram();
            create();
        } else {
            programID = GL_NONE;
        }
        uniformLocations = new HashMap<>();
    }

    // Read Shader Methods

    private void create() {
        try {
            Logger.debug("Creating shader from file %s", getFilenameInQuotes());
            List<ShaderStage> programs = readShaderPrograms();
            programs.forEach(this::compileShader);
            linkShaderStages(programs);
            programs.forEach(ShaderStage::delete); // Clean up intermediate programs
        } catch (ShaderException e) {
            Logger.printStackTrace(e);
        }
    }

    private List<ShaderStage> readShaderPrograms() throws ShaderException {
        try {
            var source = TextIOUtils.readText(openInputStream());
            var shaders = source.split("(#type)( )+"); // shaders indicated by "#type <shader_type>"
            return parseShaderPrograms(shaders);
        } catch (Exception e) {
            Logger.error("Could not parse shader file %s", getFilenameInQuotes());
            throw new ShaderException(e);
        }
    }

    private static List<ShaderStage> parseShaderPrograms(String[] subPrograms) throws ShaderException {
        var programs = new ArrayList<ShaderStage>();
        for (var shader : subPrograms) {
            var src = shader.strip();
            if (src.isEmpty()) continue;
            programs.add(readShaderProgram(src));
        }
        return programs;
    }

    private static ShaderStage readShaderProgram(String stageSource) throws ShaderException {
        var firstNewLine = stageSource.indexOf("\n");
        var typeName = stageSource.substring(0, firstNewLine).trim();

        var programSource = stageSource.substring(firstNewLine + 1);
        var shaderType = ShaderType.findWithName(typeName);
        return new ShaderStage(programSource, shaderType);
    }

    // Compile Shader Methods

    private void compileShader(ShaderStage stage) throws ShaderException {
        try {
            stage.compileSource();
        } catch (ShaderException e) {
            Logger.error("OpenGL: " + glGetShaderInfoLog(programID));
            throw e;
        }
    }

    private void linkShaderStages(List<ShaderStage> stages) throws ShaderException {
        stages.forEach(p -> p.linkToProgram(programID));
        glLinkProgram(programID);
        glValidateProgram(programID);

        // Check link status
        if (programLinkedSuccessfully(programID)) {
            Logger.debug("OpenGL: Finished linking shader file %s", getFilenameInQuotes());
        } else {
            Logger.error("OpenGL: Could not link shader file %s", getFilenameInQuotes());
            Logger.error("OpenGL: " + glGetProgramInfoLog(programID));
            throw new ShaderException("Error linking shader file");
        }
    }

    private static boolean programLinkedSuccessfully(int shaderID) {
        return glGetProgrami(shaderID, GL_LINK_STATUS) != GL_FALSE;
    }

    // Renderer Methods

    /**
     * Bind this shader to the GPU.
     */
    public void bind() {
        glUseProgram(programID);
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
    private void delete() {
        if (GLHelper.isGLInitialized()) {
            glDeleteProgram(programID);
        }
        uniformLocations.clear();
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
        if (uniformLocations.containsKey(varName)) {
            return uniformLocations.get(varName);
        } else {
            var location = glGetUniformLocation(programID, varName);
            uniformLocations.put(varName, location);
            return location;
        }
    }

    // Asset Methods

    @Override
    public void free() {
        delete();
    }

}
