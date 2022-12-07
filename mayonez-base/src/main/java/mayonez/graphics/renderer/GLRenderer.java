package mayonez.graphics.renderer;

import mayonez.Preferences;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.graphics.GLCamera;
import mayonez.io.Assets;
import mayonez.io.image.GLTexture;
import mayonez.io.image.Shader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Contains methods for uploading sprite and shape data to the GPU for renderers using OpenGL.
 */
@UsesEngine(EngineType.GL)
public abstract class GLRenderer implements Renderer {

    // GPU Resources
    private final List<RenderBatch> batches;
    protected final Shader shader;
    protected final int[] textureSlots; // support multiple texture IDs in batch
    private final int maxBatchSize;

    public GLRenderer(String shaderFile, int maxBatchSize) {
        this.batches = new ArrayList<>();
        this.shader = Assets.getShader(shaderFile);
        this.maxBatchSize = maxBatchSize;
        textureSlots = new int[Preferences.getMaxTextureSlots()];
        for (int i = 0; i < this.textureSlots.length; i++) this.textureSlots[i] = i; // ints 0-7
    }

    // Renderer Methods

    @Override
    public void start() {
        batches.clear();
    }

    @Override
    public final void render(Graphics2D g2) {
        bind();
        rebuffer();
        batches.forEach(RenderBatch::render); // Draw
        unbind();
    }

    /**
     * Upload resources to the GPU.
     */
    protected void bind() {
        // Bind shader and upload uniforms
        shader.bind();
        GLCamera camera = (GLCamera) getCamera();
        shader.uploadMat4("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4("uView", camera.getViewMatrix());
    }

    /**
     * Push all image data into batches.
     */
    protected final void rebuffer() {
        batches.forEach(RenderBatch::clear); // Prepare batches
        pushDataToBatch();
        batches.forEach(RenderBatch::upload); // Finalize batches
        batches.sort(Comparator.comparingInt(RenderBatch::getZIndex)); // Sort batches by z-index
    }

    /**
     * Clear resources form the GPU.
     */
    protected void unbind() {
        shader.unbind(); // Unbind everything
    }

    @Override
    public void stop() {
        batches.forEach(RenderBatch::delete);
    }

    // OpenGL Helper Methods

    /**
     * How to add image data to render batches.
     */
    protected abstract void pushDataToBatch();

    protected final RenderBatch getAvailableBatch(GLTexture texture, int zIndex, DrawPrimitive primitive) {
        for (RenderBatch batch : batches) {
            if (batch.hasRoom() && batch.getZIndex() == zIndex) { // has room for more vertices and z-index matches
                if (batch.hasTexture(texture) || batch.hasTextureRoom()) { // has room for texture
                    if (batch.getPrimitive().equals(primitive)) // uses the same primitive
                        return batch;
                }
            }
        }
        // Create new if all batches full
        RenderBatch batch = new RenderBatch(maxBatchSize, zIndex, primitive);
        batch.clear();
        batches.add(batch);
        return batch;
    }

}
