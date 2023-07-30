package mayonez.graphics;

import mayonez.annotations.*;

import java.util.*;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBufferSubData;

/**
 * A float array storing vertex data for a {@link mayonez.graphics.RenderBatch}, such as
 * position, color, and texture (UV) coordinates.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class VertexArray {

    private final float[] vertices;
    private int size; // Current vertex index

    VertexArray(int vertexCapacity) {
        this.vertices = new float[vertexCapacity];
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

    void push(Float... floats) {
        // TODO check for over capacity?
        for (var f : floats) vertices[size++] = f;
    }

    // Array Getters

    boolean hasRoom() {
        return size < vertices.length;
    }

    int capacity() {
        return vertices.length;
    }

    int size() {
        return size;
    }

}
