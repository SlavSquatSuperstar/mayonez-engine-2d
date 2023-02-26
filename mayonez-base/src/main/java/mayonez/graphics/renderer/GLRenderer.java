package mayonez.graphics.renderer;

import mayonez.Preferences;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.graphics.GLCamera;
import mayonez.graphics.GLRenderable;
import mayonez.graphics.RenderBatch;
import mayonez.io.Assets;
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
        preRender();
        rebuffer();
        batches.forEach(RenderBatch::render); // Draw everything
        postRender();
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
     * Sort all image data into batches.
     */
    protected final void rebuffer() {
        batches.forEach(RenderBatch::clear); // Prepare batches
        createBatches();
        batches.forEach(RenderBatch::upload); // Finalize batches
        batches.sort(Comparator.comparingInt(RenderBatch::getZIndex)); // Sort batches by z-index
    }

    /**
     * Free resources from the GPU.
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

    protected final RenderBatch getAvailableBatch(GLRenderable object) {
        for (RenderBatch batch : batches) {
            if (doesBatchFit(batch, object)) return batch;
        }
        // If all batches full
        return createNewBatch(object);
    }

    private boolean doesBatchFit(RenderBatch batch, GLRenderable object) {
        var fitsVertices = batch.hasRoom();
        var samePrimitive = batch.getPrimitive() == object.getPrimitive();
        var sameZIndex = batch.getZIndex() == object.getZIndex();
        var fitsTexture = batch.hasTexture(object.getTexture()) || batch.hasTextureRoom();
        return fitsVertices && samePrimitive && sameZIndex && fitsTexture;
    }

    private RenderBatch createNewBatch(GLRenderable object) {
        RenderBatch batch = new RenderBatch(object.getBatchSize(), object.getZIndex(), object.getPrimitive());
        batch.clear();
        batches.add(batch);
        return batch;
    }

}
