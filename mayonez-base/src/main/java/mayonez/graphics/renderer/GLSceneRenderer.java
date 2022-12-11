package mayonez.graphics.renderer;

import mayonez.*;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.graphics.sprite.GLSprite;
import mayonez.graphics.sprite.Sprite;
import mayonez.math.Vec2;
import mayonez.physics.shapes.Rectangle;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Draws all in-game textures onto the screen using LWJGL's OpenGL library.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLSceneRenderer extends GLRenderer implements SceneRenderer {

    private static final int MAX_BATCH_SIZE = Preferences.getMaxBatchSize();
    private final List<GLSprite> sprites;
    private final RenderBatch bgBatch;
    private Sprite background;

    public GLSceneRenderer() {
        super("assets/shaders/default.glsl", MAX_BATCH_SIZE);
        sprites = new ArrayList<>();
        bgBatch = new RenderBatch(1, 0, DrawPrimitive.SPRITE);
    }

    // Game Object Methods

    @Override
    public void setScene(Scene newScene) {
        newScene.getObjects().forEach(this::addObject);
        background = newScene.getBackground();
        background.setTransform(Transform.scaleInstance(newScene.getSize()));
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
    public void start() {
        super.start();
        bgBatch.clear();
    }

    @Override
    protected void preRender() {
        super.preRender();
        shader.uploadIntArray("uTextures", textureSlots);
        drawBackground();
    }

    private void drawBackground() {
        if (background.getTexture() == null) {
            Vector4f bgColor = background.getColor().toGL();
            glClearColor(bgColor.x, bgColor.y, bgColor.z, 1f);
            glClear(GL_COLOR_BUFFER_BIT);
        } else {
            // Rebuffer and draw the background
            bgBatch.clear();
            addSprite(bgBatch, (GLSprite) background);
            bgBatch.upload();
            bgBatch.render();
        }
    }

    @Override
    protected void createBatches() {
        sprites.forEach(spr -> {
            if (!spr.isEnabled()) return;
            RenderBatch batch = getAvailableBatch(spr.getTexture(), spr.getGameObject().getZIndex(), DrawPrimitive.SPRITE);
            addSprite(batch, spr); // Push vertices to batch
        });
    }

    @Override
    public void stop() {
        super.stop();
        bgBatch.delete();
        sprites.clear();
    }

    // Batch Helper Methods

    /**
     * Adds a sprite to a render batch and pushes its vertex data and texture.
     *
     * @param batch the batch
     * @param spr   the sprite
     */
    public void addSprite(RenderBatch batch, GLSprite spr) {
        // Add sprite vertex data
        Transform objXf = spr.getTransform().combine(spr.getSpriteTransform());
        Vector4f color = spr.getColor().toGL();
        Vec2[] texCoords = spr.getTexCoords();
        int texID = batch.addTexture(spr.getTexture());

        // Render sprite at object center and rotate according to object
        Vec2[] sprVertices = Rectangle.rectangleVertices(new Vec2(0), new Vec2(1), objXf.rotation);
        for (int i = 0; i < sprVertices.length; i++) {
            // sprite_pos = (obj_pos + vert_pos * obj_scale) * world_scale
            Vec2 sprPos = objXf.position.add(sprVertices[i].mul(objXf.scale)).mul(SceneManager.getCurrentScene().getScale());
            batch.pushVec2(sprPos);
            batch.pushVec4(color);
            batch.pushVec2(texCoords[i]);
            batch.pushInt(texID);
        }
    }

}
