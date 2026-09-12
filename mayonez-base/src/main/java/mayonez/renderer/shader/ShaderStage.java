package mayonez.renderer.shader;

import mayonez.*;
import mayonez.application.*;
import mayonez.assets.Asset;
import mayonez.assets.text.TextIOUtils;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL20.*;

/**
 * A programmable stage within the Open rendering pipeline. Shader stages are
 * typically read from .glsl, .vert, or .frag source files. Officially, a
 * ShaderStage is considered an OpenGL shader, while a {@link Shader} is
 * referred to as an OpenGL program.
 *
 * @author SlavSquatSuperstar
 */
@UsesBackend(Backend.GL)
class ShaderStage extends Asset {

    /*
     * Look for sequence "uniform <type> <name> ;"
     * or "uniform <type> <name> [ n ] ;" or "uniform <type> [ n ] <name> ;"
     * May occur across multiple lines
     * Need at least one whitespace character before <type> and <name>,
     * Whitespace optional before [, n, ], and ;
     */
    private static final Pattern UNIFORM_PATTERN;

    static {
        var typeRegex = "uniform\\s+(\\w+)";
        var arrayRegex = "\\s*\\[\\s*\\d+\\s*]";
        var nameRegex = "\\s+(\\w+)";
        var endRegex = "\\s*;";

        var patterns = new String[]{
                typeRegex + nameRegex + endRegex,
                typeRegex + arrayRegex + nameRegex + endRegex,
                typeRegex + nameRegex + arrayRegex + endRegex,
        };
        UNIFORM_PATTERN = Pattern.compile(String.join("|", patterns), Pattern.MULTILINE);
    }

    private final ShaderType type; // Type of shader stage
    private String source; // GLSL source code
    private int shaderID; // ID of shader stage in OpenGL
    private final Set<String> uniforms;

    ShaderStage(String filename, ShaderType type) {
        super(filename);
        this.type = type;
        source = "";
        shaderID = GL_NONE;
        uniforms = new HashSet<>();
    }

    // Shader Methods

    /**
     * Read the source code and parse the uniform names from the .glsl file.
     *
     * @throws ShaderException if the file could not be read
     */
    void readSource() throws ShaderException {
        try (var input = openInputStream()) {
            source = TextIOUtils.readText(input);
            parseUniforms();
        } catch (IOException e) {
            throw new ShaderException("Could not read shader stage %s"
                    .formatted(getFilename()));
        }
    }

    private void parseUniforms() {
        uniforms.clear();
        // This may find matches inside comments but the location will simply be -1 later on
        var matcher = UNIFORM_PATTERN.matcher(source);
        while (matcher.find()) {
            /*
             * Get the uniform name
             *
             * Capturing groups are denoted with (<regex>)
             * Group 0 is the entire pattern
             * Group 1 is the uniform type
             * Group 2 is the uniform name
             *
             * Specifying alternates with | creates more groups
             * Non-matching alternates will have null groups
             * So filter them out at the end
             */
            uniforms.add(matcher.group(2));
            uniforms.add(matcher.group(4));
            uniforms.add(matcher.group(6));
        }
        uniforms.removeIf(Objects::isNull);
    }

    /**
     * Compile the shader from its source code.
     *
     * @throws ShaderException if the shader could not be compiled
     */
    void compileSource() {
        shaderID = glCreateShader(type.glShaderType);
        glShaderSource(shaderID, source);
        glCompileShader(shaderID);

        // Check compiled correctly
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_TRUE) {
            Logger.debug("Compiled %s shader stage %s", type.toString(), getFilename());
        } else {
            Logger.error("Could not compile %s shader stage %s", type.toString(), getFilename());
            if (glGetShaderi(shaderID, GL_INFO_LOG_LENGTH) > 0) {
                // Don't print an empty log
                Logger.error("OpenGL Log: " + glGetShaderInfoLog(shaderID));
            }
            throw new ShaderException("Error compiling shader stage");
        }
    }

    /**
     * Attaches this shader to a parent program.
     *
     * @param programID the parent program ID
     */
    void attachToProgram(int programID) {
        glAttachShader(programID, this.shaderID);
    }

    /**
     * Detaches this shader to a parent program.
     *
     * @param programID the parent program ID
     */
    void detachFromProgram(int programID) {
        glDetachShader(programID, this.shaderID);
    }

    /**
     * Cleans up the object compiled from this shader stage.
     */
    void delete() {
        // The shader must be detached first
        glDeleteShader(shaderID);
        source = "";
        shaderID = GL_NONE;
        uniforms.clear();
    }

    // Asset Methods

    @Override
    public void free() {
        // Automatically deleted after program linked
        delete();
    }


    // Getter Methods

    /**
     * Get the type of shader this stage represents.
     *
     * @return the shader type
     */
    ShaderType getType() {
        return type;
    }

    /**
     * Get the names of the uniforms present in this shader.
     *
     * @return the set of uniform names
     */
    Set<String> getUniforms() {
        return uniforms;
    }

}
