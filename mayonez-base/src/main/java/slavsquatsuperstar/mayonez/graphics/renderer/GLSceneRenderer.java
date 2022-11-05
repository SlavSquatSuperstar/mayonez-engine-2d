package slavsquatsuperstar.mayonez.graphics.renderer;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.graphics.GLCamera;
import slavsquatsuperstar.mayonez.graphics.sprites.GLSprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Draws all in-game textures and shapes onto the screen using LWJGL's OpenGL library.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLSceneRenderer extends GLRenderer implements SceneRenderer {

    private final int MAX_BATCH_SIZE = Preferences.getMaxBatchSize();
    private final List<GLSprite> sprites;

    public GLSceneRenderer() {
        super("assets/shaders/default.glsl");
        sprites = new ArrayList<>();
    }

    // Game Object Methods

    @Override
    public void setScene(Scene newScene) {
        batches.clear();
        newScene.getObjects().forEach(this::addObject);
    }

    @Override
    public void addObject(GameObject object) {
        GLSprite sprite = object.getComponent(GLSprite.class);
        if (sprite != null) sprites.add(sprite);
    }

    @Override
    public void removeObject(GameObject object) {
        GLSprite sprite = object.getComponent(GLSprite.class);
        if (sprite != null) sprites.remove(sprite);
    }

    // Renderer Methods

    @Override
    public void render(Graphics2D g2) {
        // Bind shader and upload uniforms
        shader.bind();
        GLCamera camera = (GLCamera) getCamera();
        shader.uploadMat4("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4("uView", camera.getViewMatrix());
        shader.uploadIntArray("uTextures", textureSlots);

        rebuffer(); // Rebuffer all sprites
        batches.forEach(RenderBatch::render); // Draw

        shader.unbind(); // Unbind everything
    }

    @Override
    protected void rebuffer() {
        batches.forEach(RenderBatch::clear); // Prepare batches
        for (GLSprite sprite : sprites) { // Rebuffer sprites into batches
            RenderBatch batch = getAvailableBatch(sprite.getTexture(), sprite.getObject().getZIndex());
            batch.addSprite(sprite); // Push vertices to batch
        }
        batches.forEach(RenderBatch::upload); // Finalize batches
        batches.sort(Comparator.comparingInt(RenderBatch::getZIndex)); // Sort batches by z-index
    }

    @Override
    protected RenderBatch createBatch(int zIndex) {
        return new RenderBatch(MAX_BATCH_SIZE, zIndex, DrawPrimitive.SPRITE);
    }

    @Override
    public void stop() {
        batches.forEach(RenderBatch::delete);
        sprites.clear();
    }

}
