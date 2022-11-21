package mayonez.graphics.renderer;

import mayonez.*;
import org.joml.Vector4f;
import mayonez.math.Vec2;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.graphics.sprite.GLSprite;
import mayonez.physics.shapes.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Draws all in-game textures and shapes onto the screen using LWJGL's OpenGL library.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLSceneRenderer extends GLRenderer implements SceneRenderer {

    private static final int MAX_BATCH_SIZE = Preferences.getMaxBatchSize();
    private final List<GLSprite> sprites;

    public GLSceneRenderer() {
        super("assets/shaders/default.glsl", MAX_BATCH_SIZE);
        sprites = new ArrayList<>();
    }

    // Game Object Methods

    @Override
    public void setScene(Scene newScene) {
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
    protected void bind() {
        super.bind();
        shader.uploadIntArray("uTextures", textureSlots);
    }

    @Override
    protected void pushDataToBatch() {
        sprites.forEach(spr -> {
            if (!spr.isEnabled()) return;
            RenderBatch batch = getAvailableBatch(spr.getTexture(), spr.getObject().getZIndex(), DrawPrimitive.SPRITE);
            addSprite(batch, spr); // Push vertices to batch
        });
    }

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

    @Override
    public void stop() {
        super.stop();
        sprites.clear();
    }

}
