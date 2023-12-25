package mayonez.graphics.batch;

import mayonez.graphics.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBufferSubData;

/**
 * Stores vertex data, such as position, color, and texture (UV) coordinates, for a
 * {@link VertexBuffer} in float format.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class VertexBufferArray {

    // Primitive Fields
    private final DrawPrimitive primitive;
    private final int totalComponentCount; // Components per primitive

    // Array Fields
    private final float[] vertexData;
    private int size; // Current vertex index

    VertexBufferArray(DrawPrimitive primitive, int maxBatchSize) {
        this.primitive = primitive;
        totalComponentCount = primitive.getVertexCount() * primitive.getComponentCount();

        var capacity = maxBatchSize * totalComponentCount;
        this.vertexData = new float[capacity];
        size = 0;
    }

    // Array Operations

    /**
     * Clear all vertices from the buffer.
     */
    void clear() {
        Arrays.fill(vertexData, 0f);
        size = 0;
    }

    void draw() {
        var numIndices = (size * primitive.getElementCount()) / totalComponentCount;
        glDrawElements(primitive.getPrimitiveType(), numIndices, GL_UNSIGNED_INT, GL_NONE);
    }

    /**
     * Uploads all vertices in the buffer to the GPU.
     */
    void upload() {
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertexData);
    }

    /**
     * Push any number of floats to the buffer.
     *
     * @param floats the floats
     */
    void push(float... floats) {
        // TODO check for over capacity?
        for (var f : floats) vertexData[size++] = f;
    }

    // Array Getters

    boolean hasRoom() {
        return size < vertexData.length;
    }

    /**
     * The current number of components in the array.
     *
     * @return the size
     */
    int size() {
        return size;
    }

    /**
     * Get the size of the vertex buffer in bytes.
     *
     * @return the capacity in bytes
     */
    long getBufferSizeBytes() {
        return (long) vertexData.length * Float.BYTES;
    }

}
