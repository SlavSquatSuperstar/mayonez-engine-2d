package mayonez.renderer.shader;

import mayonez.*;
import mayonez.application.*;
import mayonez.assets.*;
import mayonez.graphics.*;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.util.*;

import static org.lwjgl.opengl.GL20.*;

/**
 * A compiled program written in the OpenGL Shading Language (GLSL) composed of multiple
 * {@link ShaderStage}s. A <a href="https://www.khronos.org/opengl/wiki/Shader">shader</a>
 * tells the GPU how to draw an image, specifying the colors, brightness, and texture.
 * Shaders require at least a vertex and a fragment stage.
 *
 * @author SlavSquatSuperstar
 */
@UsesBackend(Backend.GL)
public class Shader extends Asset {

    private int programID;
    private final Map<String, Integer> uniformLocations;

    public Shader(String filename) {
        super(filename);
        uniformLocations = new HashMap<>();
        createShader(filename);
    }

    public Shader(String[] stageFilenames) {
        super(String.join("+", stageFilenames));
        uniformLocations = new HashMap<>();
        createShader(stageFilenames);
    }

    // Read Shader Methods

    // TODO don't delete stages if from multiple files
    private void createShader(String... stageFilenames) {
        Logger.debug("Creating shader from files %s", Arrays.toString(stageFilenames));

        if (!GLHelper.isGLInitialized()) {
            Logger.warn("OpenGL capabilities are not initialized");
            programID = GL_NONE;
            return;
        }

        List<ShaderStage> stages = new ArrayList<>();
        try {
            for (var filename : stageFilenames) {
                var source = ShaderParser.readShaderSource(FilePath.fromFilename(filename));
                var stageSources = ShaderParser.parseShaderStages(source);
                stages.addAll(stageSources);
            }
            stages.forEach(ShaderStage::compileSource);
            programID = glCreateProgram();
            linkShaderStages(stages);
        } catch (ShaderException e) {
            Logger.printStackTrace(e);
            programID = GL_NONE;
        } finally {
            // Clean up intermediate stages
            stages.forEach(s -> s.detachFromProgram(programID));
            stages.forEach(ShaderStage::delete);
        }
    }

    // Compile Shader Methods

    private void linkShaderStages(List<ShaderStage> stages) throws ShaderException {
        stages.forEach(s -> s.attachToProgram(programID));
        glLinkProgram(programID);
        glValidateProgram(programID);

        // Check link status
        if (programLinkedSuccessfully(programID)) {
            Logger.debug("OpenGL: Finished linking shader file %s", getFilenameInQuotes());
        } else {
            Logger.error("OpenGL: Could not link shader file %s", getFilenameInQuotes());
            Logger.error("OpenGL: " + glGetProgramInfoLog(programID));
            Logger.error("Shaders must have least a vertex and fragment stage");
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
        programID = GL_NONE;
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
