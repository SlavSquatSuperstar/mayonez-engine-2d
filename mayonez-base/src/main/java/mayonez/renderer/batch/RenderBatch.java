package mayonez.renderer.batch;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.renderer.gl.*;
import org.joml.*;

/**
 * Stores vertex information for many similar drawable objects and combines them
 * into one large mesh, allowing many sprites and shapes to be drawn in fewer
 * GPU calls. Roughly analogous to the {@link java.awt.Graphics2D} class from
 * the AWT engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class RenderBatch {

    // TODO store shader per batch

    // Batch Characteristics
    private final DrawPrimitive primitive;
    private final int maxBatchObjects;

    // Renderer Data
    private final VertexBufferArray vertices;
    private final TextureArray textures;

    // GPU Resources
    private final VertexArray vao; // VAO for this batch
    private final VertexBuffer vbo; // VBO for this batch
    private final IndexBuffer ibo; // EBO/IBO for this batch

    public RenderBatch(DrawPrimitive primitive, int maxBatchObjects, int maxTextureSlots) {
        this.primitive = primitive;
        this.maxBatchObjects = maxBatchObjects;

        // Renderer Fields
        textures = new TextureArray(maxTextureSlots);
        vertices = new VertexBufferArray(primitive, maxBatchObjects);

        // GPU Fields
        vao = new VertexArray();
        vbo = new VertexBuffer(primitive, vertices);
        ibo = new IndexBuffer(primitive, maxBatchObjects);
        createBatch();
    }

    // Initialization Methods

    /**
     * Allocates GPU resources to this batch and generates the arrays and buffers upon starting the scene.
     */
    private void createBatch() {
        if (!GLHelper.isGLInitialized()) return;
        vao.generate();
        vbo.generate();
        ibo.generate();

        vao.setVertexLayout(vbo);
        vao.setElementLayout(ibo);
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
     * Upload all vertex data and tell the GPU to draw all images buffered to
     * this batch.
     */
    public void drawBatch() {
        // Finalize batch
        vbo.bind();
        vao.bind();
        textures.bindTextures();
        vertices.draw();
    }

    /**
     * Free GPU resources upon stopping the scene.
     */
    public void deleteBatch() {
        clearVertices();

        // Unbind everything
        vbo.unbind();
        ibo.unbind();
        vao.unbind();
        textures.unbindTextures();

        // Delete everything
        vbo.delete();
        ibo.delete();
        vao.delete();
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

    /**
     * Adds a texture to this render batch if the texture is not present and returns
     * the texture slot for the batch's shader, which is not necessarily the OpenGL texture ID.
     *
     * @param tex the texture
     * @return the batch texture ID, 0 if color, otherwise 1-8
     */
    public int getTextureSlot(GLTexture tex) {
        if (!hasTexture(tex)) textures.addTexture(tex); // add if don't have texture
        return textures.getTextureSlot(tex);
    }

    // Capacity Getters

    /**
     * If this render batch can fit the given object. An object fits
     * if it uses the same primitive as this batch and this batch has room
     * for more vertices and the object's texture.
     *
     * @param r the renderable object
     * @return if the object fits
     */
    public boolean canFitObject(GLRenderable r) {
        return hasVertexRoom()
                && (this.primitive == r.getPrimitive())
                && (this.hasTexture(r.getTexture()) || this.hasTextureRoom());
    }

    /**
     * If this render batch uses the given texture. Null textures (drawing
     * colors only) always return true.
     *
     * @param tex the texture, or null if drawing a color
     * @return if this texture is used by the batch
     */
    protected boolean hasTexture(GLTexture tex) {
        return textures.containsTexture(tex);
    }

    /**
     * If this render batch has capacity for another texture.
     *
     * @return if there are unused texture slots
     */
    protected boolean hasTextureRoom() {
        return textures.hasRoom();
    }

    /**
     * If this render batch has capacity for another primitive object.
     *
     * @return if there are still unused vertices
     */
    protected boolean hasVertexRoom() {
        return vertices.hasRoom();
    }

    // Batch Getters

    public DrawPrimitive getPrimitive() {
        return primitive;
    }

    public int getNumBatchObjects() {
        return vertices.size() /
                (primitive.getVertexCount() * primitive.getTotalComponents());
    }

    public int getMaxBatchObjects() {
        return maxBatchObjects;
    }

    /**
     * The draw order of this render batch. Batches with lower draw orders are
     * drawn first, and batches with higher orders are layered on top of others.
     *
     * @return the draw order
     */
    public int getDrawOrder() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("RenderBatch (Type: %s, Capacity: %d/%d)",
                primitive, getNumBatchObjects(), maxBatchObjects);
    }

}
