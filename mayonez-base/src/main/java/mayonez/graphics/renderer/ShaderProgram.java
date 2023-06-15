package mayonez.graphics.renderer;

import mayonez.annotations.*;

import static org.lwjgl.opengl.GL20.*;

/**
 * An individual sub-program within a .glsl shader file.
 */
@UsesEngine(EngineType.GL)
class ShaderProgram {

    /**
     * The type of the shader program.
     */
    private final ShaderType type;

    /**
     * The .glsl source code of the shader program.
     */
    private final String source;

    /**
     * The ID of the shader program.
     */
    final int id;

    ShaderProgram(ShaderType type, String source) {
        this.type = type;
        this.source = source;
        id = glCreateShader(type.glShaderType);
    }

    void compileSource() throws RuntimeException {
        glShaderSource(id, source);
        glCompileShader(id);
        if (!checkCompiledCorrectly(id)) throw new RuntimeException(("Error compiling shader program"));
    }

    private boolean checkCompiledCorrectly(int id) {
        return glGetShaderi(id, GL_COMPILE_STATUS) != GL_FALSE;
    }

    /**
     * Get the ID of the shader sub-program in OpenGL.
     *
     * @return the shader ID.
     */
    int getID() {
        return id;
    }

    /**
     * Get the type of the shader sub-program in OpenGL.
     *
     * @return the shader type
     */
    String getType() {
        return type.shaderName;
    }
}
