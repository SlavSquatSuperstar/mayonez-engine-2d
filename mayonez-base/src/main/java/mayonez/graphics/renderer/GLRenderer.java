package mayonez.graphics.renderer;

import mayonez.Preferences;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.graphics.GLCamera;
import mayonez.graphics.DrawPrimitive;
import mayonez.graphics.RenderBatch;
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

    public GLRenderer(String shaderFile) {
        this.batches = new ArrayList<>();
        this.shader = Assets.getShader(shaderFile);
        textureSlots = new int[Preferences.getMaxTextureSlots()];
        for (var i = 0; i < this.textureSlots.length; i++) this.textureSlots[i] = i; // ints 0-7
    }

    // Renderer Methods

    @Override
    public void start() {
        batches.clear();
    }

    @Override
    public final void render(Graphics2D g2) {
        preRender(); // Upload to GPU
        rebuffer(); // Sort everything into batches
        batches.forEach(RenderBatch::render); // Draw everything
        postRender(); // Free from GPU
    }

    /**
     * Upload resources to the GPU.
     */
    protected void preRender() {
        // Bind shader and upload uniforms
        shader.bind();
        var camera = (GLCamera) getCamera();
        shader.uploadMat4("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4("uView", camera.getViewMatrix());
    }

    /**
     * Push all image data into batches.
     */
    protected final void rebuffer() {
        batches.forEach(RenderBatch::clear); // Prepare batches
        createBatches();
        batches.forEach(RenderBatch::upload); // Finalize batches
        batches.sort(Comparator.comparingInt(RenderBatch::getZIndex)); // Sort batches by z-index
    }

    /**
     * Clear resources form the GPU.
     */
    protected void postRender() {
        shader.unbind(); // Unbind everything
    }

    @Override
    public void stop() {
        batches.forEach(RenderBatch::delete);
    }

    // Batch Helper Methods

    /**
     * Sort image data into render batches.
     */
    protected abstract void createBatches();

    protected final RenderBatch getAvailableBatch(GLTexture texture, int zIndex, DrawPrimitive primitive, int maxBatchSize) {
        for (RenderBatch batch : batches) {
            var samePrimitive = batch.getPrimitive() == primitive;
            var sameZIndex = batch.getZIndex() == zIndex;
            var fitsTexture = batch.hasTexture(texture) || batch.hasTextureRoom();
            var fitsVertices = batch.hasRoom();
            if (samePrimitive && sameZIndex && fitsTexture && fitsVertices) return batch;
        }
        // Create new if all batches full
        RenderBatch batch = new RenderBatch(maxBatchSize, zIndex, primitive);
        batch.clear();
        batches.add(batch);
        return batch;
    }

}
