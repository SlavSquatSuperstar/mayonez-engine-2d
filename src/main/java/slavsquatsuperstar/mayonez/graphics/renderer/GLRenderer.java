package slavsquatsuperstar.mayonez.graphics.renderer;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.fileio.GLTexture;
import slavsquatsuperstar.mayonez.graphics.GLCamera;
import slavsquatsuperstar.mayonez.graphics.GLSprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws all in-game textures and shapes onto the screen using LWJGL's OpenGL library.
 */
public final class GLRenderer extends Renderer {

    private final int MAX_BATCH_SIZE = Preferences.MAX_BATCH_SIZE;
    private final List<RenderBatch> batches;
    private GLCamera camera;
    private boolean checkForDestroyed = false; // Remove sprites with deleted game objects

    public GLRenderer() {
        batches = new ArrayList<>();
    }

    // Game Loop Methods

    @Override
    public void render(Graphics2D g2) {
        batches.forEach(RenderBatch::render);
        if (checkForDestroyed) batches.forEach(RenderBatch::removeDestroyedSprites);
    }

    // Game Object Methods

    @Override
    public void setScene(Scene newScene) {
        camera = (GLCamera) newScene.getCamera();
        batches.clear();
        newScene.getObjects(null).forEach(this::addObject);
    }

    public void addObject(GameObject o) {
        GLSprite spr = o.getComponent(GLSprite.class);
        if (spr != null) {
            for (RenderBatch batch : batches) {
                if (batch.hasRoom()) { // has room for sprite
                    GLTexture tex = spr.getTexture();
                    // has texture or room for another texture
                    if (tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom())) {
                        batch.addSprite(spr);
                        return;
                    }
                }
            }
            // Sprite not added
            RenderBatch batch = new RenderBatch(MAX_BATCH_SIZE, camera);
            batch.start();
            batches.add(batch);
            batch.addSprite(spr);
        }
    }

    @Override
    public void removeObject(GameObject o) {
        checkForDestroyed = true;
        // tell renderer to look for sprites with null parent in next frame
    }

}
