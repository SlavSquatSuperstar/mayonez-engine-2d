package mayonez.renderer.shader;

import mayonez.graphics.*;
import mayonez.util.*;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

/**
 * The type of .glsl shader program used by the GPU.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
enum ShaderType {

    /**
     * A vertex shader that transforms an image to its screen position.
     */
    VERTEX(GL_VERTEX_SHADER),

    /**
     * A fragment (pixel) shader that calculates the color and texture of each
     * individual pixel of an image.
     */
    FRAGMENT(GL_FRAGMENT_SHADER);

    /**
     * The shader ID in OpenGL.
     */
    final int glShaderType;

    ShaderType(int glShaderType) {
        this.glShaderType = glShaderType;
    }

    // Static Methods

    /**
     * Returns the ShaderType constant matching the given name,
     * case-insensitive.
     *
     * @param shaderName the name of the shader program
     * @return the shader type
     * @throws ShaderException if the shader name is invalid
     */
    static ShaderType findWithName(String shaderName) {
        var type = StringUtils.findWithName(values(), shaderName);
        if (type == null) {
            throw new ShaderException("Unexpected shader type \"$shaderName\"");
        }
        return type;
    }

}
