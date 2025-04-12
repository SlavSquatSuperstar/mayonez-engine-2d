package mayonez.renderer.batch

import mayonez.graphics.*
import java.nio.IntBuffer

// Vertex Attribute Aliases
private val POSITION: VertexAttribute = VertexAttribute.FLOAT2 // (x, y, 0)
private val TEX_COORD: VertexAttribute = VertexAttribute.FLOAT2 // (u, v)
private val COLOR: VertexAttribute = VertexAttribute.FLOAT4 // (r, g, b, a)
private val TEX_SLOT: VertexAttribute = VertexAttribute.INT // i

/**
 * Types of OpenGL primitive objects that can be submitted to the GPU. Each
 * object defines the element and attribute layout.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
enum class DrawPrimitive(
    val layout: ElementLayout, vararg val attributes: VertexAttribute
) {

    /**
     * A triangle with 3 vertices, each with attributes position and color.
     */
    TRIANGLE(
        ElementLayout.TRIANGLE,
        POSITION, COLOR
    ),

    /**
     * An untextured textured quadrangle with 4 vertices, each with attributes
     * position and color.
     *
     * Note that lines are drawn using quads, since glLineWidth() does not support
     * line widths above 1 on all platforms.
     *
     * Sources:
     * - [Drawing lines with shaders](https://stackoverflow.com/questions/3484260/opengl-line-width)
     * - [Drawing lines as triangles](https://stackoverflow.com/questions/101718/drawing-a-variable-width-line-in-opengl-no-gllinewidth)
     * - [Drawing OpenGL lines](https://stackoverflow.com/questions/14486291/how-to-draw-line-in-opengl)
     */
    QUAD(
        ElementLayout.QUAD,
        POSITION, COLOR
    ),

    /**
     * A textured quadrangle with 4 vertices, each with attributes position, color,
     * texture coordinate, and texture slot.
     */
    SPRITE(
        ElementLayout.QUAD,
        POSITION, COLOR, TEX_COORD, TEX_SLOT
    ),

    /**
     * A circle with 4 vertices, each with attributes position, local position,
     * color, and inner radius.
     *
     * Sources:
     * - [Drawing circles as triangles](https://www.youtube.com/watch?v=VEnglRKNHjU)
     * - [Drawing circles as quads](https://stackoverflow.com/questions/22444450/drawing-circle-with-opengl/50408198#50408198)
     * - [TheCherno Hazel Engine circle shader](https://github.com/TheCherno/Hazel/blob/master/Hazelnut/assets/shaders/Renderer2D_Circle.glsl)
     */
    CIRCLE(
        ElementLayout.QUAD,
        POSITION, POSITION, COLOR, VertexAttribute.FLOAT
    ),

    /**
     * An ellipse with 4 vertices, each with attributes position, local position,
     * color, and two inner radii.
     */
    ELLIPSE(
        ElementLayout.QUAD,
        POSITION, POSITION, COLOR, VertexAttribute.FLOAT2
    );

    val vertexCount: Int
        get() = layout.vertexCount

    val elementCount: Int
        get() = layout.elementCount

    val drawMode: Int
        get() = layout.drawMode

    /** The number of components for all attributes per vertex. */
    val totalComponents: Int = attributes.sumOf { it.components }

    /**
     * Adds indices to an element buffer array (EBO).
     *
     * @param elements an int buffer
     * @param index the vertex index
     */
    fun addIndices(elements: IntBuffer, index: Int) {
        for (e in layout.elementOrder) {
            elements.put(vertexCount * index + e)
        }
    }

}