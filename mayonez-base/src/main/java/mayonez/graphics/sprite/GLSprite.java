package mayonez.graphics.sprite;

import mayonez.math.Vec2;
import mayonez.GameObject;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.io.image.GLTexture;
import mayonez.util.Color;
import mayonez.util.Colors;

/**
 * A component that draws an image at a {@link GameObject}'s position using the GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLSprite extends Sprite {

    private final GLTexture texture;

    /**
     * Create a new GLSprite that renders an entire texture.
     *
     * @param texture a texture
     */
    GLSprite(GLTexture texture) {
        super(Colors.WHITE);
        this.texture = texture;
    }

    /**
     * Create a new GLSprite that only renders a color.
     *
     * @param color a color
     */
    GLSprite(Color color) {
        super(color);
        texture = null;
    }

    // Getters and Setters

    @Override
    public GLTexture getTexture() {
        return texture;
    }

    public Vec2[] getTexCoords() {
        return (texture == null) ? GLTexture.DEFAULT_TEX_COORDS : texture.getTexCoords();
    }

    @Override
    public GLSprite copy() {
        return (texture == null) ? new GLSprite(color) : new GLSprite(texture);
    }

}
