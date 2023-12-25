package mayonez.graphics.batch;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import org.joml.*;

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
    private final int maxBatchSize; // number of objects per batch
    private final int zIndex;

    // Vertex Parameters
    private final DrawPrimitive primitive;
    private final int vertexCount; // vertices per primitive
    private final int elementCount; // element per primitive (if dividing into triangles)
    private final int componentCount; // total components per vertex

    // GPU Resources
    /**
     * The vertex array object (VAO) ID for this batch.
     */
    private int vao;
    /**
     * The vertex buffer object (VBO) for this batch.
     */
    private final VertexBuffer vbo;
    /**
     * The index/element buffer object (IBO/EBO) for this batch.
     */
    private final IndexBuffer ebo;
    private final VertexArray vertices;
    private final TextureArray textures;

    public RenderBatch(int maxBatchSize, int zIndex, DrawPrimitive primitive) {
        this.maxBatchSize = maxBatchSize;
        this.zIndex = zIndex;

        this.primitive = primitive;
        this.vertexCount = primitive.getVertexCount();
        this.elementCount = primitive.getElementCount();
        this.componentCount = primitive.getComponentCount();

        // Renderer Fields
        textures = new TextureArray(MAX_TEXTURE_SLOTS);
        vertices = new VertexArray(maxBatchSize * vertexCount * componentCount);

        vbo = new VertexBuffer(primitive, vertices);
        ebo = new IndexBuffer(primitive, maxBatchSize);
        createBatch();
    }

    // Initialization Methods

    /**
     * Allocates GPU resources to this batch and generates the vertex buffers upon starting the scene.
     */
    private void createBatch() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vbo.generate();
        ebo.generate();
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
        vbo.upload();
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
        var numIndices = (vertices.size() * elementCount) / (componentCount * vertexCount);
        glDrawElements(primitive.getPrimitiveType(), numIndices, GL_UNSIGNED_INT, GL_NONE);
    }

    private void unbindVertices() {
        glBindVertexArray(GL_NONE);
        textures.unbindTextures();
    }

    /**
     * Free GPU resources upon stopping the scene.
     */
    public void deleteBatch() {
        vbo.delete();
        glDeleteVertexArrays(vao);
        ebo.delete();
    }

    // Helper Methods

    /**
     * Adds a texture to this render batch if the texture is not present and returns
     * the texture ID within the batch, which is not necessarily the OpenGL texture ID.
     *
     * @param tex the texture
     * @return the batch texture ID, 0 if color, otherwise 1-8
     */
    public int getIDForTexture(GLTexture tex) {
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
                primitive, vertices.size() / (componentCount), maxBatchSize, zIndex);
    }

}
