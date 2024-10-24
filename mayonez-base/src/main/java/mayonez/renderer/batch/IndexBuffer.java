package mayonez.renderer.batch;

import mayonez.graphics.*;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

/**
 * An index/element buffer object (IBO/EBO) on the GPU. An IBO allows vertices
 * to be reused for complex shapes.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class IndexBuffer {

    private final DrawPrimitive primitive; // TODO make fields method arguments
    private final int maxBatchSize;
    private int eboID;

    IndexBuffer(DrawPrimitive primitive, int maxBatchSize) {
        this.primitive = primitive;
        this.maxBatchSize = maxBatchSize;
        eboID = GL_NONE;
    }

    /**
     * Generates the index buffer on the GPU.
     */
    void generate() {
        eboID = glGenBuffers();
    }

    /**
     * Load element indices and define shapes for OpenGL to draw.
     *
     * @return an index array (int buffer)
     */
    IntBuffer getElementIndices() {
        var numIndices = primitive.getElementCount() * maxBatchSize;
        var elements = BufferUtils.createIntBuffer(numIndices);
        for (var i = 0; i < maxBatchSize; i++) primitive.addIndices(elements, i);
        return elements.flip(); // need to flip an int buffer
    }

    /**
     * Binds the index buffer to the GPU.
     */
    void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
    }

    /**
     * Unbinds the index buffer from the GPU.
     */
    void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_NONE);
    }

    /**
     * Deletes the index buffer from the GPU.
     */
    void delete() {
        glDeleteBuffers(eboID);
        eboID = GL_NONE;
    }

}
