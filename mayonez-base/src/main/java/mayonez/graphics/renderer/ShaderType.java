package mayonez.graphics.renderer;

import mayonez.annotations.*;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

/**
 * The type of shader used by the GPU.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
enum ShaderType {

    /**
     * A vertex shader that calculates the scaling of an image.
     */
    VERTEX(GL_VERTEX_SHADER, "vertex"),

    /**
     * A fragment (pixel) shader that calculates the color and texture of each
     * individual pixel of an image.
     */
    FRAGMENT(GL_FRAGMENT_SHADER, "fragment");

    /**
     * The shader ID in OpenGL.
     */
    final int glShaderType;

    /**
     * The name of the shader type in GLSL.
     */
    final String shaderName;

    ShaderType(int glShaderType, String shaderName) {
        this.glShaderType = glShaderType;
        this.shaderName = shaderName;
    }

    /**
     * Returns the {@link ShaderType} constant matching the given name, case-insensitive.
     *
     * @param shaderName the name of the shader program
     * @return the shader type
     * @throws IllegalArgumentException if the shader name is invalid
     */
    static ShaderType getType(String shaderName) throws IllegalArgumentException {
        for (var type : values()) {
            if (type.name().equalsIgnoreCase(shaderName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unexpected shader type \"%s\"".formatted(shaderName));
    }
}
