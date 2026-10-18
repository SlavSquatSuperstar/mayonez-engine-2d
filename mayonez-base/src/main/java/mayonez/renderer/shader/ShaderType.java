package mayonez.renderer.shader;

import mayonez.application.*;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

/**
 * The type of .glsl shader stage used by the GPU.
 *
 * @author SlavSquatSuperstar
 */
@UsesBackend(Backend.GL)
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
    final String name;

    ShaderType(int glShaderType) {
        this.glShaderType = glShaderType;
        this.name = name().toLowerCase();
    }

    @Override
    public String toString() {
        return name;
    }

}
