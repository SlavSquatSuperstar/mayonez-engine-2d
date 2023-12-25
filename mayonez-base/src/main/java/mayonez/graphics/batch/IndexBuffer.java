package mayonez.graphics.batch;

import mayonez.graphics.*;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

/**
 * An index/element buffer object (IBO/EBO) on the GPU. An IBO allows vertices
 * to be reused for complex shapes.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class IndexBuffer {

    private final DrawPrimitive primitive;
    private final int maxBatchSize;
    private int eboID;

    IndexBuffer(DrawPrimitive primitive, int maxBatchSize) {
        this.primitive = primitive;
        this.maxBatchSize = maxBatchSize;
    }

    /**
     * Generates the index buffer on the GPU.
     */
    void generate() {
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, generateIndices(), GL_STATIC_DRAW);
    }

    /**
     * Load element indices and define shapes for OpenGL to draw.
     *
     * @return an index array (int buffer)
     */
    private IntBuffer generateIndices() {
        var bufferSize = primitive.getElementCount() * maxBatchSize;
        var elements = BufferUtils.createIntBuffer(bufferSize);
        for (var i = 0; i < maxBatchSize; i++) primitive.addIndices(elements, i);
        return elements.flip(); // need to flip an int buffer
    }

    /**
     * Deletes the index buffer from the GPU.
     */
    void delete() {
        glDeleteBuffers(eboID);
    }

}
