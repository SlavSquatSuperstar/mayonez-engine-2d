package mayonez.graphics.batch;

import mayonez.graphics.*;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * A vertex array object (VAO) ID on the GPU. A VAO stores the layouts of a vertex
 * and index buffer between draw calls.
 *
 * @author SlavSquatSuperstar
 */
// TODO also create VBO and IBO
@UsesEngine(EngineType.GL)
class VertexArray {

    private int vaoID;

    VertexArray() {
        vaoID = GL_NONE;
    }

    /**
     * Generates the vertex array on the GPU.
     */
    void generate() {
        vaoID = glGenVertexArrays();
    }

    /**
     * Attaches a vertex buffer to this vertex array. The VBO will be enabled
     * whenever this VAO is made active.
     *
     * @param vbo the VBO to use the layout with.
     */
    void setVertexLayout(VertexBuffer vbo) {
        this.bind();
        vbo.bind();
        vbo.setVertexLayout();
    }

    /**
     * Attaches an index buffer to this vertex array. The IBO will be enabled
     * whenever this VAO is made active.
     *
     * @param ibo the index buffer
     */
    void setElementLayout(IndexBuffer ibo) {
        this.bind();
        ibo.bind();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ibo.getElementIndices(), GL_STATIC_DRAW);
    }

    /**
     * Binds the array object to the GPU.
     */
    void bind() {
        glBindVertexArray(vaoID);
    }

    /**
     * Unbinds the array object to the GPU.
     */
    void unbind() {
        glBindVertexArray(GL_NONE);
    }

    /**
     * Deletes the vertex array from the GPU.
     */
    void delete() {
        glDeleteVertexArrays(vaoID);
        vaoID = GL_NONE;
    }

}
