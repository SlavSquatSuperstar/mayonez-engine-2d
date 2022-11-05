package slavsquatsuperstar.mayonez.graphics.renderer;

import org.lwjgl.opengl.GL11;
import slavsquatsuperstar.math.MathUtils;

public enum DrawPrimitive {
    /* Pos (2), Color (4), Tex Coords (2), Tex ID (1) */
    SPRITE(4, 6, GL11.GL_TRIANGLES, 2, 4, 2, 1),
    /* Pos (3), Color (3) */
    LINE(2, 2, GL11.GL_LINES, 2, 4);

    // Vertex Parameters
    final int vertexCount; // Vertices defined by primitive
    final int elementCount; // Elements needed by GL (i.e. split quad into 2 triangles)
    final int primitiveType; // Primitive ID used by OpenGL
    final int[] attributeSizes; // floats inside each attribute
    final int vertexSize; // Floats inside one vertex

    DrawPrimitive(int vertexCount, int elementCount, int primitiveType, int... attributeSizes) {
        this.vertexCount = vertexCount;
        this.elementCount = elementCount;
        this.primitiveType = primitiveType;
        this.attributeSizes = attributeSizes;
        vertexSize = MathUtils.sum(attributeSizes);
    }

}
