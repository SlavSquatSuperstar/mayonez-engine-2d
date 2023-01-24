package mayonez.graphics;

import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.math.IntMath;
import mayonez.util.StringUtils;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

/**
 * Types of primitive objects submitted to the GPU. Only supported by GL.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public enum DrawPrimitive {
    /**
     * An object with 2 vertices with attributes Pos (2), Color (4)
     */
    LINE(2, 2, GL11.GL_LINES, 2, 4) {
        @Override
        public void addIndices(IntBuffer elements, int index) {
            for (var v = 0; v <= 1; v++) elements.put(vertexCount * index + v);
        }
    },
    /**
     * An object with 3 vertices with attributes Pos (2), Color (4)
     */
    TRIANGLE(3, 3, GL11.GL_TRIANGLES, 2, 4) {
        @Override
        public void addIndices(IntBuffer elements, int index) {
            // Use counterclockwise winding
            for (var v = 0; v <= 2; v++) elements.put(vertexCount * index + v);
        }
    },
    /**
     * An object with 4 vertices with attributes Pos (2), Color (4), Tex Coords (2), Tex ID (1)
     */
    SPRITE(4, 6, GL11.GL_TRIANGLES, 2, 4, 2, 1) {
        @Override
        public void addIndices(IntBuffer elements, int index) {
            // Split quad into two triangles
            int[] triangleVertices = {0, 1, 2, 0, 2, 3};
            for (var v : triangleVertices) elements.put(vertexCount * index + v);
        }
    };

    // Vertex Parameters
    public final int vertexCount; // Vertices defined by primitive
    public final int elementCount; // Elements needed by GL (i.e. split quad into 2 triangles)
    public final int primitiveType; // Primitive ID used by OpenGL
    public final int[] attributeSizes; // Floats inside each attribute
    public final int vertexSize; // Floats inside one vertex

    DrawPrimitive(int vertexCount, int elementCount, int primitiveType, int... attributeSizes) {
        this.vertexCount = vertexCount;
        this.elementCount = elementCount;
        this.primitiveType = primitiveType;
        this.attributeSizes = attributeSizes;
        vertexSize = IntMath.sum(attributeSizes);
    }

    /**
     * Adds indices to an element buffer array (EBO).
     *
     * @param elements an int buffer
     * @param index    the vertex index
     */
    public abstract void addIndices(IntBuffer elements, int index);

    @Override
    public String toString() {
        return StringUtils.capitalize(this.name());
    }
}
