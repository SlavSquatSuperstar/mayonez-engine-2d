package slavsquatsuperstar.mayonez.graphics.renderer;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.graphics.GLCamera;
import slavsquatsuperstar.mayonez.graphics.GLSprite;
import slavsquatsuperstar.mayonez.io.Assets;
import slavsquatsuperstar.mayonez.io.GLTexture;
import slavsquatsuperstar.mayonez.io.Shader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws all in-game textures and shapes onto the screen using LWJGL's OpenGL library.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLRenderer extends Renderer {

    private final int MAX_BATCH_SIZE = Preferences.getMaxBatchSize();
    private final int MAX_TEXTURE_SLOTS = Preferences.getMaxTextureSlots();
    private final List<GLSprite> sprites;
    private final List<RenderBatch> batches;

    private final Shader shader;
    private GLCamera camera;
    // Remove sprites with deleted game objects

    public GLRenderer() {
        sprites = new ArrayList<>();
        batches = new ArrayList<>();
        shader = Assets.getShader("assets/shaders/default.glsl");
    }

    // Game Loop Methods

    @Override
    public void render(Graphics2D g2) {
        // Bind shader and upload uniforms
        shader.bind();
        shader.uploadMat4("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4("uView", camera.getViewMatrix());

        rebuffer(); // Rebuffer

        batches.forEach(RenderBatch::render); // Draw

        shader.unbind(); // Unbind everything
    }

    public void rebuffer() {
        batches.forEach(RenderBatch::clear); // Clear Batches
        for (GLSprite sprite : sprites) { // Rebuffer batches
            RenderBatch batch = getAvailableBatch(sprite.getTexture(), sprite.getParent().getZIndex());
            batch.pushSpriteData(sprite); // Push vertices to batch
        }
        batches.forEach(RenderBatch::upload); // Finalize Batches
    }

    @Override
    public void stop() {
        batches.forEach(RenderBatch::delete);
    }

    // Game Object Methods

    @Override
    public void setScene(Scene newScene) {
        camera = (GLCamera) newScene.getCamera();
        batches.clear();
        newScene.getObjects(null).forEach(this::addObject);
    }

    public void addObject(GameObject object) {
        GLSprite sprite = object.getComponent(GLSprite.class);
        if (sprite != null) sprites.add(sprite);
    }

    @Override
    public void removeObject(GameObject object) {
        GLSprite sprite = object.getComponent(GLSprite.class);
        if (sprite != null) sprites.remove(sprite);
    }

    // Helper Methods

    private RenderBatch getAvailableBatch(GLTexture texture, int zIndex) {
        for (RenderBatch batch : batches) {
            if (batch.hasSpriteRoom() && batch.getZIndex() == zIndex) { // has room for sprite (vertices)
                if (batch.hasTextureRoom() || batch.hasTexture(texture) || texture == null) // has room for texture or using color
                    return batch;
            }
        }
        // All batches full
        RenderBatch batch = new RenderBatch(MAX_BATCH_SIZE, MAX_TEXTURE_SLOTS, zIndex);
        batch.clear();
        batches.add(batch);
        return batch;
    }
}
