package mayonez.graphics.batch;

import mayonez.graphics.*;

import java.util.*;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBufferSubData;

/**
 * Stores vertex data, such as position, color, and texture (UV) coordinates, for a
 * {@link RenderBatch} in float format.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class VertexArray {

    private final float[] vertices;
    private int size; // Current vertex index

    VertexArray(int capacity) { // Total number of attribute components
        this.vertices = new float[capacity];
        size = 0;
    }

    // Array Operations

    void clear() {
        Arrays.fill(vertices, 0f);
        size = 0;
    }

    void upload() {
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
    }

    void push(float... floats) {
        // TODO check for over capacity?
        for (var f : floats) vertices[size++] = f;
    }

    // Array Getters

    boolean hasRoom() {
        return size < vertices.length;
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
        return (long) vertices.length * Float.BYTES;
    }

}
