package mayonez.graphics.batch;

import mayonez.graphics.*;

import static org.lwjgl.opengl.GL15.*;

/**
 * A vertex buffer object (VBO) ID on the GPU. A VBO is an array of data passed
 * to the GPU for a draw call.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class VertexBuffer {

    private final VertexBufferArray vertices;
    private int vboID;

    VertexBuffer(VertexBufferArray vertices) {
        this.vertices = vertices;
    }

    /**
     * Generates the vertex buffer on the GPU and binds it to the active VAO.
     */
    void generate() {
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.getSizeBytes(), GL_DYNAMIC_DRAW);
    }

    /**
     * Binds the buffer object to the GPU.
     */
    void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
    }

    /**
     * Unbinds the buffer object from the GPU.
     */
    void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
    }

    /**
     * Deletes the vertex buffer from the GPU.
     */
    void delete() {
        glDeleteBuffers(vboID);
    }

}
