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
@UsesEngine(EngineType.GL)
class VertexArray {

    private int vaoID;

    /**
     * Generates the vertex array on the GPU.
     */
    void generate() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
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
    }

}
