package mayonez.renderer.shader;

import mayonez.*;
import mayonez.application.*;
import mayonez.assets.*;
import mayonez.assets.text.JSONFile;
import mayonez.graphics.*;
import mayonez.util.Record;
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
    private final Record shaderDefinition;
    private final List<ShaderStage> stages;
    private final Map<String, Integer> uniformLocations;

    public Shader(String filename) {
        super(filename);
        shaderDefinition = new JSONFile(filename).readJSON();
        stages = new ArrayList<>();
        uniformLocations = new HashMap<>();
    }

    // Create Shader Methods

    /**
     * Read the shader stages and parse the uniforms without compiling the source code.
     *
     * @throws ShaderException if any shaders are missing or could not be read
     */
    void readShader() throws ShaderException {
        Logger.debug("Reading shader definition %s", getFilename());

        // Read shaders and parse uniforms
        // Don't need OpenGL to be initialized yet
        stages.clear();
        uniformLocations.clear();

        var shaderTypes = ShaderType.values();
        for (var type : shaderTypes) {
            var filename = shaderDefinition.getString(type.name);
            // Check if stage present
            if (filename.isEmpty()) {
                throw new ShaderException("%s is missing %s shader"
                        .formatted(getFilename(), type.name));
            }

            var stage = new ShaderStage(filename, type);
            stage.readSource();
            stage.getUniforms().forEach(
                    // Store locations as -1 for now
                    uniform -> uniformLocations.put(uniform, -1)
            );
            stages.add(stage);
        }
    }

    /**
     * Compile the shader source code, link the program, and cache the uniform locations.
     */
    void createShader() {
        readShader();

        // Check OpenGL initialized before compiling
        if (!GLHelper.isGLInitialized()) {
            Logger.error("OpenGL capabilities are not initialized");
            programID = GL_NONE;
            return;
        }

        try {
            // Compile shaders
            stages.forEach(ShaderStage::compileSource);

            // Link program
            programID = glCreateProgram();
            linkShaderStages();

            // Cache uniform locations
            uniformLocations.keySet().forEach(uniform -> {
                int location = glGetUniformLocation(programID, uniform);
                uniformLocations.put(uniform, location);
            });
            Logger.log("Uniforms: " + uniformLocations);
        } catch (ShaderException e) {
            Logger.printStackTrace(e);
            programID = GL_NONE;
        } finally {
            // Clean up intermediate stages
            stages.forEach(stage -> {
                stage.detachFromProgram(programID);
                stage.delete();
            });
            stages.clear();
        }
    }

    private void linkShaderStages() throws ShaderException {
        stages.forEach(s -> s.attachToProgram(programID));
        glLinkProgram(programID);
        glValidateProgram(programID);

        // Check linked correctly
        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_TRUE) {
            Logger.debug("OpenGL: Linked shader file %s", getFilenameInQuotes());
        } else {
            Logger.error("OpenGL: Could not link shader file %s", getFilenameInQuotes());
            Logger.error("OpenGL: " + glGetProgramInfoLog(programID));
            Logger.error("Shaders must have least a vertex and fragment stage");
            throw new ShaderException("Error linking shader file");
        }
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

    // Getter Methods

    /**
     * Get the intermediate shader stages in this program.
     *
     * @return the list of stages
     */
    List<ShaderStage> getStages() {
        return stages;
    }

    /**
     * Get the uniform names and locations in this program.
     *
     * @return the map of uniforms
     */
    Map<String, Integer> getUniformLocations() {
        return uniformLocations;
    }

}
