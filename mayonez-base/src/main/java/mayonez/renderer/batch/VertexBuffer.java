package mayonez.renderer.batch;

import mayonez.graphics.*;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

/**
 * A vertex buffer object (VBO) ID on the GPU. A VBO is an array of data passed
 * to the GPU for a draw call.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class VertexBuffer {

    private final DrawPrimitive primitive;
    private final VertexBufferArray vertices;
    private int vboID;

    VertexBuffer(DrawPrimitive primitive, VertexBufferArray vertices) {
        this.primitive = primitive;
        this.vertices = vertices;
        vboID = GL_NONE;
    }

    /**
     * Generates the vertex buffer on the GPU and binds it to the active VAO.
     */
    void generate() {
        vboID = glGenBuffers();
    }

    /**
     * Sets the layout of vertex attributes for the active VAO.
     */
    void setVertexLayout() {
        glBufferData(GL_ARRAY_BUFFER, vertices.getSizeBytes(), GL_DYNAMIC_DRAW);

        var ptrOffset = 0;
        var attributes = primitive.getAttributes();
        for (var i = 0; i < attributes.length; i++) {
            var attrib = attributes[i];
            glVertexAttribPointer(i, attrib.getComponents(), attrib.getGlType(), false,
                    primitive.getTotalComponents() * attrib.getComponentBytes(), ptrOffset);
            glEnableVertexAttribArray(i);
            ptrOffset += attrib.getTotalBytes();
        }
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
        vboID = GL_NONE;
    }

}
