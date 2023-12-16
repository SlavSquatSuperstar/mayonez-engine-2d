package mayonez.graphics.batch;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

/**
 * Stores vertex information for many similar drawable objects and combines them into one large mesh,
 * allowing many sprites and shapes to be drawn in fewer GPU calls. Roughly analog to the {@link java.awt.Graphics2D}
 * class from the AWT engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class RenderBatch {

    // Batch Constants
    public static final int MAX_SPRITES = 100;
    public static final int MAX_LINES = 500;
    public static final int MAX_TRIANGLES = 1000;
    public static final int MAX_TEXTURE_SLOTS = 8;

    // Batch Characteristics
    private final int maxBatchSize;
    private final int zIndex;

    // Vertex Parameters
    private final DrawPrimitive primitive;
    private final int vertexCount; // vertices per primitive
    private final int elementCount; // element per primitive (if dividing into triangles)
    private final int vertexSize; // attributes per vertex

    // GPU Resources
    private int vao; // Vertex array object (VAO) ID for this batch
    private int vbo; // Vertex buffer object (VBO) ID for this batch
    private int ebo; // Element buffer object (EBO)/index buffer ID for this batch
    private final VertexArray vertices;
    private final TextureArray textures;

    public RenderBatch(int maxBatchSize, int zIndex, DrawPrimitive primitive) {
        this.maxBatchSize = maxBatchSize;
        this.zIndex = zIndex;

        this.primitive = primitive;
        this.vertexCount = primitive.getVertexCount();
        this.elementCount = primitive.getElementCount();
        this.vertexSize = primitive.getVertexSize();

        // Renderer Fields
        textures = new TextureArray(MAX_TEXTURE_SLOTS);
        vertices = new VertexArray(maxBatchSize * vertexCount * vertexSize);

        createBatch();
    }

    // Initialization Methods

    /**
     * Allocates GPU resources to this batch and generates the vertex buffers upon starting the scene.
     */
    private void createBatch() {
        generateVAOs();
        generateVBOs();
        generateEBOs();
        generateVertexIndexBuffer();
    }

    private void generateVAOs() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
    }

    private void generateVBOs() {
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.capacity() * Float.BYTES, GL_DYNAMIC_DRAW);
    }

    private void generateEBOs() {
        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, generateIndices(), GL_STATIC_DRAW);
    }

    private void generateVertexIndexBuffer() {
        var ptrOffset = 0;
        var attributes = primitive.getAttributes();
        for (var i = 0; i < attributes.length; i++) {
            var attrib = attributes[i];
            attrib.setVertexAttribute(i, vertexSize, ptrOffset);
            ptrOffset += attrib.getTotalSize();
        }
    }

    /**
     * Load element indices and define shapes for OpenGL to draw.
     *
     * @return an index array (int buffer)
     */
    private IntBuffer generateIndices() {
        var elements = BufferUtils.createIntBuffer(elementCount * maxBatchSize);
        for (var i = 0; i < maxBatchSize; i++) primitive.addIndices(elements, i);
        elements.flip(); // need to flip an int buffer
        return elements;
    }

    // Renderer Methods

    /**
     * Empties all sprite vertices and textures from the batch and readies it for buffering.
     */
    public void clearVertices() {
        vertices.clear();
        textures.clear();
    }

    /**
     * Upload all vertex data to the GPU after buffering.
     */
    public void uploadVertices() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        vertices.upload();
    }

    /**
     * Draw all vertices in the batch.
     */
    public void drawBatch() {
        bindVertices();
        drawVertices();
        unbindVertices();
    }

    private void bindVertices() {
        glBindVertexArray(vao);
        textures.bindTextures();
    }

    private void drawVertices() {
        var numVertices = (vertices.size() * elementCount) / (vertexSize * vertexCount);
        glDrawElements(primitive.getPrimitiveType(), numVertices, GL_UNSIGNED_INT, 0);
    }

    private void unbindVertices() {
        glBindVertexArray(GL_NONE);
        textures.unbindTextures();
    }

    /**
     * Free GPU resources upon stopping the scene.
     */
    public void deleteBatch() {
        glDeleteBuffers(ebo);
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }

    // Helper Methods

    /**
     * Adds a texture to this render batch if the texture is not present and returns
     * the texture ID within the batch, which is not necessarily the OpenGL texture ID.
     *
     * @param tex the texture
     * @return the batch texture ID, 0 if color, otherwise 1-8
     */
    public int addTextureAndGetID(GLTexture tex) {
        if (!hasTexture(tex)) textures.addTexture(tex); // add if don't have texture
        return textures.getTextureID(tex);
    }

    // Push Vertex Methods

    public void pushInt(int i) {
        vertices.push((float) i);
    }

    public void pushVec2(Vec2 v) {
        vertices.push(v.x, v.y);
    }

    public void pushVec4(Vector4f v) {
        vertices.push(v.x, v.y, v.z, v.w);
    }

    // Getters

    public DrawPrimitive getPrimitive() {
        return primitive;
    }

    public int getZIndex() {
        return zIndex;
    }

    /**
     * If the render batch contains this texture (can always render colors)
     *
     * @param tex the texture, or null if drawing a color
     * @return if this texture is used by the batch
     */
    public boolean hasTexture(GLTexture tex) {
        return textures.containsTexture(tex);
    }

    /**
     * If the render batch has capacity for another texture.
     *
     * @return if there are unused texture slots
     */
    public boolean hasTextureRoom() {
        return textures.hasRoom();
    }

    /**
     * If the render batch has capacity for another primitive object.
     *
     * @return if there are still unused vertices
     */
    public boolean hasVertexRoom() {
        return vertices.hasRoom();
    }

    @Override
    public String toString() {
        return String.format("Render Batch (Type: %s, Capacity: %d/%d, Z-Index: %d)",
                primitive, vertices.size() / (vertexSize), maxBatchSize, zIndex);
    }
}
