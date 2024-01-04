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

    private final DrawPrimitive primitive;
    private int vaoID;

    public VertexArray(DrawPrimitive primitive) {
        this.primitive = primitive;
    }

    /**
     * Generates the vertex array on the GPU.
     */
    void generate() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
    }

    /**
     * Sets the layout of vertex attributes for the active VAO.
     *
     * @param vbo the VBO to use the layout with.
     * @param primitive the primitive that defines the vertex count and layout
     */
    void setVertexLayout(VertexBuffer vbo, DrawPrimitive primitive) {
        this.bind();
        vbo.bind();

        var ptrOffset = 0;
        var attributes = primitive.getAttributes();
        for (var i = 0; i < attributes.length; i++) {
            var attrib = attributes[i];
            attrib.setVertexAttribute(i, primitive.getComponentCount(), ptrOffset);
            ptrOffset += attrib.getSizeBytes();
        }
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
