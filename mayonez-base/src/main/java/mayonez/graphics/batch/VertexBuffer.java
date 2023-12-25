package mayonez.graphics.batch;

import mayonez.graphics.*;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;

/**
 * A vertex buffer object (VBO) ID on the GPU. A VBO is an array of data passed
 * to the GPU for a draw call.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
 class VertexBuffer {

    private final DrawPrimitive primitive;
    private final VertexArray vertices;
    private int vboID;

    VertexBuffer(DrawPrimitive primitive, VertexArray vertices) {
        this.primitive = primitive;
        this.vertices = vertices;
    }

    /**
     * Generates the vertex buffer on the GPU.
     */
    void generate() {
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.getBufferSizeBytes(), GL_DYNAMIC_DRAW);
        generateVertexIndexBuffer();
    }

    private void generateVertexIndexBuffer() {
        var ptrOffset = 0;
        var attributes = primitive.getAttributes();
        for (var i = 0; i < attributes.length; i++) {
            var attrib = attributes[i];
            attrib.setVertexAttribute(i, primitive.getComponentCount(), ptrOffset);
            ptrOffset += attrib.getSizeBytes();
        }
    }

    /**
     * Uploads all vertices in the buffer to the GPU.
     */
    void upload() {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        vertices.upload();
    }

    /**
     * Deletes the vertex buffer from the GPU.
     */
    void delete() {
        glDeleteBuffers(vboID);
    }

}
